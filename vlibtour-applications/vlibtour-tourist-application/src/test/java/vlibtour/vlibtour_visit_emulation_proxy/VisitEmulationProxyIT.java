// CHECKSTYLE:OFF
/**
This file is part of the course CSC5002.

Copyright (C) 2017-2023 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Chantal Taconet and Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_visit_emulation_proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static vlibtour.vlibtour_common.Log.EMULATION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import vlibtour.vlibtour_tourist_application.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.GPSPosition;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_tourist_application.visit_emulation_proxy.VisitEmulationProxy;
import vlibtour.vlibtour_visit_emulation_server.VisitEmulationServer;

final class VisitEmulationProxyIT {
	private static Process process;

	@BeforeAll
	static void setUp() throws IOException, InterruptedException, URISyntaxException {
		Runtime r = Runtime.getRuntime();
		Process wc = r.exec("netstat -nlp");
		wc.waitFor();
		try (BufferedReader b = new BufferedReader(new InputStreamReader(wc.getInputStream()))) {
			String line = "";
			while ((line = b.readLine()) != null) {
				if (line.contains("127.0.0.1:8888")) {
					String sub = line.substring(StringUtils.lastIndexOfAny(line, "LISTEN") + "LISTEN".length()).trim();
					String p = sub.substring(0, sub.indexOf("/"));
					EMULATION.error("{}",
							() -> "Warning: Already a REST server on address 127.0.0.1:8888 => kill -9 " + p);
					r.exec("kill -9 " + p);
				}
			}
		}
		process = new ProcessBuilder("java", "-cp",
				System.getProperty("java.class.path"),
				VisitEmulationServer.class.getCanonicalName(), "Dalton",
				"ParisBigTour").inheritIO().start();
		System.out.println("process: " + process.info() + " " + process.isAlive());
		Thread.sleep(1000);
	}

	@Test
	void testnotnull() {
		VisitEmulationProxy proxy = new VisitEmulationProxy();
		final String user = ExampleOfAVisitWithTwoTourists.USER_ID_JOE;
		assertNotNull(proxy.getNextPOIPosition(user));
		assertNotNull(proxy.getCurrentPosition(user));
		assertNotNull(proxy.stepInCurrentPath(user));
		assertNotNull(proxy.stepsInVisit(user));
	}

	@Test
	void testGoToLastPOI() {
		VisitEmulationProxy proxy = new VisitEmulationProxy();
		final String user = ExampleOfAVisitWithTwoTourists.USER_ID_JOE;
		Position currentPOI = proxy.getCurrentPosition(user);
		Position currentPos = currentPOI;

		while (proxy.stepsInVisit(user) != currentPOI) {
			while (proxy.stepInCurrentPath(user) != currentPos) {
				currentPos = proxy.stepInCurrentPath(user);
			}
			currentPOI = proxy.stepsInVisit(user);
		}

		assertEquals(currentPOI, ExampleOfAVisitWithTwoTourists.POSITION47);

	}

	@AfterAll
	static void tearDown() {
		// since the rabbitmq container is not started in method @BeforeClass,
		// it is not stopped and removed here
		if (process != null) {
			process.destroyForcibly();
		}
	}
}

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

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_visit_emulation_server;

import static vlibtour.vlibtour_common.Log.EMULATION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.Position;

class VLibTourVisitEmulationServerIT {
	private static Process process;
	private static WebTarget service;

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
		process = new ProcessBuilder("java", "-cp", System.getProperty("java.class.path"),
				VisitEmulationServer.class.getCanonicalName(), "Dalton", "ParisBigTour").inheritIO().start();
		Thread.sleep(1000);
		Client client = ClientBuilder.newClient();
		URI uri = UriBuilder.fromUri(ExampleOfAVisitWithTwoTourists.BASE_URI_WEB_SERVER).build();
		service = client.target(uri);
	}

	@Test
	@Disabled // FIXME
	void testGetNextPOIPosition() throws IOException {
		Response jsonResponse = service
				.path("visitemulation/getNextPOIPosition/" + ExampleOfAVisitWithTwoTourists.USER_ID_JOE).request()
				.accept(MediaType.APPLICATION_JSON).get();
		Assertions.assertNotNull(jsonResponse);
		Assertions.assertNotNull(jsonResponse.readEntity(Position.class));
	}

	@Test
	@Disabled // FIXME
	void testGetCurrentPosition() throws IOException {
		Response jsonResponse = service
				.path("visitemulation/getCurrentPosition/" + ExampleOfAVisitWithTwoTourists.USER_ID_JOE).request()
				.accept(MediaType.APPLICATION_JSON).get();
		Assertions.assertNotNull(jsonResponse);
		Assertions.assertNotNull(jsonResponse.readEntity(Position.class));
	}

	@Test
	@Disabled // FIXME
	void testStepInCurrentPath() throws IOException {
		Response jsonResponse = service
				.path("visitemulation/stepInCurrentPath/" + ExampleOfAVisitWithTwoTourists.USER_ID_JOE).request()
				.accept(MediaType.APPLICATION_JSON).get();
		Assertions.assertNotNull(jsonResponse);
		Assertions.assertNotNull(jsonResponse.readEntity(Position.class));
	}

	@Test
	@Disabled // FIXME
	void testStepsInVisit() throws IOException {
		Response jsonResponse = service
				.path("visitemulation/stepInCurrentPath/" + ExampleOfAVisitWithTwoTourists.USER_ID_JOE).request()
				.accept(MediaType.APPLICATION_JSON).get();
		Assertions.assertNotNull(jsonResponse);
		Assertions.assertNotNull(jsonResponse.readEntity(Position.class));
	}

	@AfterAll
	static void tearDown() throws InterruptedException, IOException {
		if (process != null) {
			process.destroyForcibly();
		}
	}
}

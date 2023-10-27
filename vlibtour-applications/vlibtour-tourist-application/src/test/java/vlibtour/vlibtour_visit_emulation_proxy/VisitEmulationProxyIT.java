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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_tourist_application.visit_emulation_proxy.VisitEmulationProxy;
import vlibtour.vlibtour_visit_emulation_api.VisitEmulationTourInitRequest;

final class VisitEmulationProxyIT {
	private static final String user = ExampleOfAVisitWithTwoTourists.USER_ID_JOE;
	private static final String tour = ExampleOfAVisitWithTwoTourists.DALTON_TOUR_ID;
	private static final List<Position> pois = ExampleOfAVisitWithTwoTourists.POI_POSITIONS_OF_DALTON_VISIT;

	private static VisitEmulationProxy proxy;

	@BeforeAll
	static void setUp() throws IOException, InterruptedException, URISyntaxException {
		proxy = new VisitEmulationProxy();
		VisitEmulationTourInitRequest body = new VisitEmulationTourInitRequest(tour, user, pois);
		proxy.initATourForAGroup(body);
	}

	@Test
	void test() {
		Position lastPosition;
		while (true) {
			Position nextPOIPosition = proxy.getNextPOIPosition(user);
			while (true) {
				Position currentPositionInPath = proxy.stepInCurrentPath(user);
				if (currentPositionInPath.getName().equals(nextPOIPosition.getName())) {
					break;
				}
			}
			Position nextPOI = proxy.stepsInVisit(user);
			if (nextPOI.getName().equals(nextPOIPosition.getName())) {
				lastPosition = nextPOIPosition;
				break;
			}
		}

		assertEquals(lastPosition.getName(), ExampleOfAVisitWithTwoTourists.POSITION47.getName());

	}

	@AfterAll
	static void tearDown() {
		proxy.close();
	}
}

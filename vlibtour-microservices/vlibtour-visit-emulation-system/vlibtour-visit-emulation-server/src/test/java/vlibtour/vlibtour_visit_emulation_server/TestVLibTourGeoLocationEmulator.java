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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vlibtour.vlibtour_common.Position;

class TestVLibTourGeoLocationEmulator {
	private String user1 = "user1";
	private String user2 = "user2";
	private String user3 = "user3";

	GraphOfPositionsForEmulation graph;

	@BeforeEach
	void setUp() {
		graph = new GraphOfPositionsForEmulation();
		graph.setAdjacencySets(VisitEmulationServer.initTourWithPOIs(graph));
	}

	/**
	 * This test demonstrates the usage of the class VLibTourGeoLocationEmulator.
	 * There are three users in this scenario. The graph used in the simulation is
	 * initialised in method
	 * {@link GraphOfPositionsForEmulation#initTourWithPOIs()}; it corresponds to
	 * the graph that is drawn in the file
	 * {@code src/main/resources/itineraries/network_for_itinerary_with_three_pois.txt}.
	 * <p>
	 * The method
	 * {@link GraphOfPositionsForEmulation#setStartingPosition(String, Position)}
	 * is for assigning the starting position of a user. All the users starts their
	 * visit from positions 2. The method
	 * {@link GraphOfPositionsForEmulation#setAPathTo(String, Position)} is for
	 * computing and choosing randomly a path from the current position to a
	 * destination position. In the scenario, user 1, 2, and 3 are going to
	 * positions 4, 19, and 47, respectively. Then, the method
	 * {@link GraphOfPositionsForEmulation#stepInCurrentPath(String)} simulates
	 * the visit position from position in the current path. When at the end of the
	 * path, stepping does not change the current position.
	 */
	@Test
	void test() {
		Position position;
		graph.setStartingPosition(user1, new Position(String.valueOf(2), null));
		graph.setStartingPosition(user2, new Position(String.valueOf(2), null));
		graph.setStartingPosition(user3, new Position(String.valueOf(2), null));
		graph.setAPathTo(user1, new Position("Musée Grévin", null));
		graph.setAPathTo(user2, new Position("Pyramide du Louvre", null));
		graph.setAPathTo(user3, new Position("Les catacombes", null));
		graph.stepInCurrentPath(user1);
		position = graph.getCurrentPosition(user1);
		Assertions.assertEquals(position, new Position(String.valueOf(3), null));
		graph.stepInCurrentPath(user1);
		graph.stepInCurrentPath(user1);
		graph.stepInCurrentPath(user1);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user2);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
		graph.stepInCurrentPath(user3);
	}
}

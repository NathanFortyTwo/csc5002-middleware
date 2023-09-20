package vlibtour.vlibtour_visit_emulation_server;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.Position;

class TestDepthFirstSearch {
	private Map<Position, Set<Position>> adjacencySets = new HashMap<>();

	private static Map<Position, Set<Position>> addEdge(final Map<Position, Set<Position>> adjacencyList,
			final int departure, final int destination) {
		return GraphOfPositionsForEmulation.addEdge(adjacencyList, new Position(String.valueOf(departure), null),
				new Position(String.valueOf(destination), null));
	}

	private static Map<Position, Set<Position>> addEdge(final Map<Position, Set<Position>> adjacencyList,
			final String departure, final String destination) {
		return GraphOfPositionsForEmulation.addEdge(adjacencyList, new Position(departure, null),
				new Position(destination, null));
	}

	@BeforeEach
	void setUp() {
		addEdge(adjacencySets, 1, 6);
		addEdge(adjacencySets, 6, 9);
		addEdge(adjacencySets, 9, 12);
		addEdge(adjacencySets, 12, 14);
		addEdge(adjacencySets, 14, 16);
		addEdge(adjacencySets, 16, 18);
		addEdge(adjacencySets, 18, 20);
		addEdge(adjacencySets, String.valueOf(20), ExampleOfAVisitWithTwoTourists.POSITION22.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION22.getName(), String.valueOf(24));
		addEdge(adjacencySets, 24, 28);
		addEdge(adjacencySets, 28, 31);
		addEdge(adjacencySets, 31, 34);
		addEdge(adjacencySets, 34, 37);
		addEdge(adjacencySets, String.valueOf(37), ExampleOfAVisitWithTwoTourists.POSITION40.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION40.getName(), String.valueOf(43));
		addEdge(adjacencySets, 43, 46);
		addEdge(adjacencySets, String.valueOf(46), ExampleOfAVisitWithTwoTourists.POSITION47.getName());

		addEdge(adjacencySets, 2, 3);
		addEdge(adjacencySets, String.valueOf(3), ExampleOfAVisitWithTwoTourists.POSITION4.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION4.getName(), String.valueOf(5));
		addEdge(adjacencySets, 5, 6);
		addEdge(adjacencySets, 5, 8);
		addEdge(adjacencySets, 8, 10);
		addEdge(adjacencySets, String.valueOf(10), ExampleOfAVisitWithTwoTourists.POSITION11.getName());

		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION4.getName(), String.valueOf(7));
		addEdge(adjacencySets, String.valueOf(7), ExampleOfAVisitWithTwoTourists.POSITION11.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION11.getName(), String.valueOf(13));
		addEdge(adjacencySets, String.valueOf(13), ExampleOfAVisitWithTwoTourists.POSITION15.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION15.getName(), String.valueOf(17));
		addEdge(adjacencySets, String.valueOf(17), ExampleOfAVisitWithTwoTourists.POSITION19.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION19.getName(), String.valueOf(21));
		addEdge(adjacencySets, 21, 23);
		addEdge(adjacencySets, 23, 26);
		addEdge(adjacencySets, 26, 29);
		addEdge(adjacencySets, 29, 32);
		addEdge(adjacencySets, 32, 35);
		addEdge(adjacencySets, 35, 38);
		addEdge(adjacencySets, 38, 41);
		addEdge(adjacencySets, 41, 44);
		addEdge(adjacencySets, 44, 43);

		addEdge(adjacencySets, 26, 25);
		addEdge(adjacencySets, 25, 27);
		addEdge(adjacencySets, 27, 30);
		addEdge(adjacencySets, 30, 33);
		addEdge(adjacencySets, 33, 36);
		addEdge(adjacencySets, 36, 39);
		addEdge(adjacencySets, 39, 42);
		addEdge(adjacencySets, 42, 45);
		addEdge(adjacencySets, 42, 36); // cycle
		addEdge(adjacencySets, String.valueOf(45), ExampleOfAVisitWithTwoTourists.POSITION47.getName());
		addEdge(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION47.getName(), String.valueOf(46));
	}

	@Test
	void testNoPathFromUnknownDeparture() {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> GraphOfPositionsForEmulation.computePathsFromDepartureToDestination(adjacencySets,
						new Position(String.valueOf(0), null), new Position(String.valueOf(46), null)));
	}

	@Test
	void testNoPathToUnknownDestination() {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> GraphOfPositionsForEmulation.computePathsFromDepartureToDestination(adjacencySets,
						new Position(String.valueOf(1), null), new Position(String.valueOf(100), null)));
	}

	@Test
	void testOnePathWithoutAnyCycle() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, new Position(String.valueOf(21), null),
						new Position(String.valueOf(43), null));
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(1, pathsFromDepartureToDestination.size());
		Assertions.assertEquals(10, pathsFromDepartureToDestination.get(0).size());
	}

	@Test
	void testOnePathWithoutAnyCycle2() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, new Position(String.valueOf(2), null),
						ExampleOfAVisitWithTwoTourists.POSITION4);
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(1, pathsFromDepartureToDestination.size());
		Assertions.assertEquals(3, pathsFromDepartureToDestination.get(0).size());
	}

	@Test
	void testOnePathWithACycle() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, new Position(String.valueOf(33), null),
						new Position(String.valueOf(46), null));
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(1, pathsFromDepartureToDestination.size());
		Assertions.assertEquals(7, pathsFromDepartureToDestination.get(0).size());
	}

	@Test
	void testTwoPathsWithACycle() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, new Position(String.valueOf(26), null),
						new Position(String.valueOf(46), null));
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(2, pathsFromDepartureToDestination.size());
		for (int i = 0; i < pathsFromDepartureToDestination.size(); i++) {
			if (pathsFromDepartureToDestination.get(i).size() != 9
					&& pathsFromDepartureToDestination.get(i).size() != 11) {
				Assertions.fail();
			}
		}
	}

	@Test
	void testManyPathsWithACycle() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, new Position(String.valueOf(2), null),
						new Position(String.valueOf(46), null));
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(5, pathsFromDepartureToDestination.size());
	}

	@Test
	void testManyPathsWithACycle2() {
		List<List<Position>> pathsFromDepartureToDestination = GraphOfPositionsForEmulation
				.computePathsFromDepartureToDestination(adjacencySets, ExampleOfAVisitWithTwoTourists.POSITION4,
						ExampleOfAVisitWithTwoTourists.POSITION19);
		Assertions.assertNotNull(pathsFromDepartureToDestination);
		Assertions.assertEquals(2, pathsFromDepartureToDestination.size());
	}

	@AfterEach
	void tearDown() {
		adjacencySets = null;
	}

}

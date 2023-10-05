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

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.GPSPosition;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_visit_emulation_api.VisitEmulationService;

/**
 * This class provides utility methods for the visit of tourists. You are free
 * to complement or modify this code.
 * <p>
 * The data structures are designed to allow the management of the visit of
 * several users.
 * <p>
 * A limitation of the current implementation is that there is only one graph of
 * positions. The limitation is imposed by the usage of the class
 * {@link vlibtour.vlibtour_visit_emulation_server.GraphOfPositionsForEmulation}.
 * <p>
 * A regular usage of this server is as follows:
 * <ul>
 * <li>{@code getCurrentPosition}: to know where a user is;</li>
 * <li>{@code getNextPOIPosition}: to know the position of the next POI, i.e.
 * the end of current path of a user;</li>
 * <li>{@code stepInCurrentPath}: to move to the next position in the current
 * path of a user. When a user is at the end of their path,
 * {@code stepInCurrentPath} returns the same position;</li>
 * <li>{@code stepsInVisit}: this calls is used when a user is at their POI in
 * order to know the position of the next POI in the visit. When a user is at
 * the end of their visit, {@code stepsInVisit} returns the same position.</li>
 * </ul>
 * 
 * @author Denis Conan
 */

// TODO: Annotations 

public final class VisitEmulationServer implements VisitEmulationService {
	/**
	 * the visit of the users.
	 */
	private static Map<String, List<Position>> visits = new HashMap<>();
	/**
	 * the indices in the visits.
	 */
	private static Map<String, Integer> currentIndicesInVisits = new HashMap<>();

	/**
	 * public constructor for REST server.
	 */
	public VisitEmulationServer() {
		// nothing to initialise because only static members
	}

	/**
	 * the main method.
	 * <p>
	 * It initialises the visits of the tourists, and afterwards it starts Grizzly
	 * HTTP server exposing JAX-RS resources.
	 * 
	 * @param args there is no command line arguments.
	 * @throws InterruptedException abrupt interruption to terminate.
	 * @throws IOException          erreur de communication au niveau du serveur.
	 */
	public static void main(final String[] args) throws InterruptedException, IOException {
		String usage = "USAGE: " + VisitEmulationServer.class.getCanonicalName()
				+ " groupId (in the demo script, 'Dalton') tourId (in the demo script, 'ParisBigTour')";
		if (args.length != 2) {
			throw new IllegalArgumentException(usage);
		}
		String groupId = args[0];
		if (groupId == null || groupId.isBlank()) {
			throw new IllegalArgumentException("the groupId cannot be null or blank");
		}
		String tourId = args[1];
		if (tourId == null || tourId.isBlank()) {
			throw new IllegalArgumentException("the tourId cannot be null or blank");
		}
		visits = new HashMap<>();
		currentIndicesInVisits = new HashMap<>();
		// Generate a graph of positions and inform/set all the clients of/to this graph
		GraphOfPositionsForEmulation.setAdjacencySets(VisitEmulationServer.initTourWithPOIs());
		EMULATION.trace("{}", () -> "Graph of positions as adjacency lists = \n>>>>\n"
				+ VisitEmulationServer.initTourWithPOIs() + "\n<<<<");
		// set the group of users and the starting positions
		Set<String> group = null;
		if (groupId.equals(ExampleOfAVisitWithTwoTourists.DALTON_GROUP_ID)) {
			group = ExampleOfAVisitWithTwoTourists.DALTON_GROUP;
			group.stream().forEach(u -> GraphOfPositionsForEmulation.setStartingPosition(u,
					ExampleOfAVisitWithTwoTourists.DEPARTURE_POSITION));
		} else {
			throw new IllegalArgumentException(
					"For now, we only know the demo group (" + ExampleOfAVisitWithTwoTourists.DALTON_GROUP_ID + ")");
		}
		// set the visit and start the visit for the users
		List<Position> visit = null;
		if (tourId.equals(ExampleOfAVisitWithTwoTourists.DALTON_TOUR_ID)) {
			visit = ExampleOfAVisitWithTwoTourists.POI_POSITIONS_OF_DALTON_VISIT;
			for (String user : group) {
				visits.put(user, visit);
			}
			if (EMULATION.isDebugEnabled()) {
				group.stream().forEach(u -> EMULATION.debug("{}", () -> u + ": visit = "
						+ visits.get(u).stream().map(Position::getName).collect(Collectors.joining(","))));
			}
			// FIXME visits must contain u
			group.stream().forEach(u -> {
				currentIndicesInVisits.put(u, 0);
				GraphOfPositionsForEmulation.setAPathTo(u, visits.get(u).get(0));
			});
		} else {
			throw new IllegalArgumentException(
					"For now, we only know the demo tour (" + ExampleOfAVisitWithTwoTourists.DALTON_TOUR_ID + ")");
		}
		// create a resource config that scans for JAX-RS resources and providers
		// in the server package
		final ResourceConfig rc = new ResourceConfig().packages(VisitEmulationServer.class.getPackage().getName());
		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI_WEB_SERVER
		final HttpServer server = GrizzlyHttpServerFactory
				.createHttpServer(URI.create(ExampleOfAVisitWithTwoTourists.BASE_URI_WEB_SERVER), rc);
		EMULATION.debug("{}", () -> String.format("Jersey app started with WADL available at " + "%sapplication.wadl",
				ExampleOfAVisitWithTwoTourists.BASE_URI_WEB_SERVER));
		Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
		server.start();
		Thread.currentThread().join();
	}

	/**
	 * sets a network of positions of a tour. Some of the positions are POIs and all
	 * the POIs are in the network. The three POIs are {@code 04}, {@code 19}, and
	 * {@code 47}. The departure is {@code 02} and the arrival is {@code 48}.
	 * <p>
	 * The network built in this example method is described in file
	 * {@code src/main/resources/itineraries/network_for_itinerary_with_three_pois.txt}.
	 * <p>
	 * The method is {@code static} in order to be tested in a JUnit class.
	 * 
	 * @return the graph.
	 */
	static Map<Position, Set<Position>> initTourWithPOIs() {
		// CHECKSTYLE:OFF
		// creation of the positions
		// positions with a non-integer name are the ones of POIs: cf.
		// ExampleOfAVisitWithTwoTourists.POSITIONS_OF_SOME_POIS
		Position p01 = new Position(String.valueOf(1), new GPSPosition(48.869301, 2.3450524), "Musée Pooja Namo");
		Position p02 = new Position(String.valueOf(2), new GPSPosition(48.8528621, 2.3209537),
				"Galeries Lafayette Haussmann");
		Position p03 = new Position(String.valueOf(3), new GPSPosition(48.8634926, 2.3218978), "Place Saint Augustin");
		Position p04 = ExampleOfAVisitWithTwoTourists.POSITION4;
		Position p05 = new Position(String.valueOf(5), new GPSPosition(48.869301, 2.3450524), "Bonne Nouvelle");
		Position p06 = new Position(String.valueOf(6), new GPSPosition(48.869301, 2.3450524), "Strasbourg Saint-Denis");
		Position p07 = new Position(String.valueOf(7), new GPSPosition(48.8674807, 2.33698), "La Bourse");
		Position p08 = new Position(String.valueOf(8), new GPSPosition(48.8687025, 2.3464083), "43, rue de Cléry");
		Position p09 = new Position(String.valueOf(9), new GPSPosition(48.867572, 2.353312), "123, bd Sébastopol");
		Position p10 = new Position(String.valueOf(10), new GPSPosition(48.8673138, 2.3427597), "37, rue du Mail");
		Position p11 = ExampleOfAVisitWithTwoTourists.POSITION11;
		Position p12 = new Position(String.valueOf(12), new GPSPosition(48.864414, 2.351565), "26, rue de Turbigo");
		Position p13 = new Position(String.valueOf(13), new GPSPosition(48.865320, 2.338845), "33, rue de Valois");
		Position p14 = new Position(String.valueOf(14), new GPSPosition(48.863824, 2.348924), "14, rue de Turbigo");
		Position p15 = ExampleOfAVisitWithTwoTourists.POSITION15;
		Position p16 = new Position(String.valueOf(16), new GPSPosition(48.862928, 2.345094), "Jardin Nelson Mandela");
		Position p17 = new Position(String.valueOf(17), new GPSPosition(48.8637511, 2.3361412), "La Comédie Française");
		Position p18 = new Position(String.valueOf(18), new GPSPosition(48.8609335, 2.3385873), "154, rue de Rivoli");
		Position p19 = ExampleOfAVisitWithTwoTourists.POSITION19;
		Position p20 = new Position(String.valueOf(20), new GPSPosition(48.858472, 2.348140), "41, rue de Rivoli");
		Position p21 = new Position(String.valueOf(21), new GPSPosition(48.860148, 2.333168),
				"6 quai François Mitterand");
		Position p22 = ExampleOfAVisitWithTwoTourists.POSITION22;
		Position p23 = new Position(String.valueOf(23), new GPSPosition(48.859618, 2.333050), "Pont du Caroussel");
		Position p24 = new Position(String.valueOf(24), new GPSPosition(48.853563, 2.347127),
				"Parvis de la Cathédrale Notre Dame");
		Position p25 = new Position(String.valueOf(25), new GPSPosition(48.859562, 2.329248), "1, rue du Bac");
		Position p26 = new Position(String.valueOf(26), new GPSPosition(48.858757, 2.332578), "Pont du Caroussel");
		Position p27 = new Position(String.valueOf(27), new GPSPosition(48.8543549, 2.3253203), "rue du Bac");
		Position p28 = new Position(String.valueOf(28), new GPSPosition(48.852815, 2.346655), "43, rue de la Bucherie");
		Position p29 = new Position(String.valueOf(29), new GPSPosition(48.856075, 2.340424), "1, rue de Nevers");
		Position p30 = new Position(String.valueOf(30), new GPSPosition(48.847706, 2.3269443), "rue de Rennes");
		Position p31 = new Position(String.valueOf(31), new GPSPosition(48.851410, 2.345840), "Square André-Lefèvre");
		Position p32 = new Position(String.valueOf(32), new GPSPosition(48.853762, 2.344292), "Place Saint-Michel");
		Position p33 = new Position(String.valueOf(33), new GPSPosition(48.843992, 2.323693),
				"Église Notre Dame des Champs");
		Position p34 = new Position(String.valueOf(34), new GPSPosition(48.849617, 2.344853), "52, rue des Écoles");
		Position p35 = new Position(String.valueOf(35), new GPSPosition(48.850380, 2.342704), "26 bd Saint-Michel");
		Position p36 = new Position(String.valueOf(36), new GPSPosition(48.840605, 2.324314), "8 rue de la Gaité");
		Position p37 = new Position(String.valueOf(37), new GPSPosition(48.843863, 2.338773), "103, bd Saint-Michel");
		Position p38 = new Position(String.valueOf(38), new GPSPosition(48.845173, 2.3333816), "Théâtre Odéon");
		Position p39 = new Position(String.valueOf(39), new GPSPosition(48.837597, 2.322994), "91 avenue duMaine");
		Position p40 = ExampleOfAVisitWithTwoTourists.POSITION40;
		Position p41 = new Position(String.valueOf(41), new GPSPosition(48.845074, 2.332309), "Odéon");
		Position p42 = new Position(String.valueOf(42), new GPSPosition(48.836658, 2.325762), "2, rue Fernat");
		Position p43 = new Position(String.valueOf(43), new GPSPosition(48.840069, 2.337088), "Hôpital Val-de-Grace");
		Position p44 = new Position(String.valueOf(44), new GPSPosition(48.8425568, 2.3322554), "Observatoire Assas");
		Position p45 = new Position(String.valueOf(45), new GPSPosition(48.835549, 2.328830), "21, rue Froidevaux");
		Position p46 = new Position(String.valueOf(46), new GPSPosition(48.835436, 2.333569), "Denfert Rochereau");
		Position p47 = ExampleOfAVisitWithTwoTourists.POSITION47;
		// CHECKSTYLE:OFF
		Map<Position, Set<Position>> adjacencyLists = new HashMap<>();
		// part of the network that is useless
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p01, p06);
		// from the departure of the tour to the first POI
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p02, p03);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p03, p04);
		// from the first POI to the second POI
		// first path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p04, p05);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p05, p06);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p06, p09);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p09, p12);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p12, p14);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p14, p16);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p16, p18);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p18, p19);
		// second path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p04, p07);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p07, p11);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p11, p13);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p13, p15);
 		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p15, p17);
                GraphOfPositionsForEmulation.addEdge(adjacencyLists, p17, p19);
		// third path that reconnects to the second path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p05, p08);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p08, p10);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p10, p11);
		// from the second POI to the third POI
		// first path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p19, p20);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p20, p22);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p22, p24);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p24, p28);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p28, p31);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p31, p34);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p34, p37);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p37, p40);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p40, p43);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p43, p46);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p46, p47);
		// second path that reconnects to the first path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p19, p21);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p21, p23);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p23, p26);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p26, p29);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p29, p32);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p32, p35);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p35, p38);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p38, p41);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p41, p44);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p44, p43);
		// third path that begins like the second path
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p26, p25);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p25, p27);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p27, p30);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p30, p33);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p33, p36);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p36, p39);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p39, p42);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p42, p45);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p45, p47);
		GraphOfPositionsForEmulation.addEdge(adjacencyLists, p47, p46);
		return adjacencyLists;
	}

	/**
	 * gets the position of the next POI to visit when arrived at the end of the
	 * current path, that is the next POI in the visit. This position is the last
	 * one of the current path.
	 * 
	 * @param user the identifier of the user.
	 * @return the position of the current POI.
	 */
	// TODO: Annotations
	
	public synchronized Position getNextPOIPosition(final String user) {
		// delegates to GraphOfPositionsForEmulation
		return GraphOfPositionsForEmulation.getNextPOIPosition(user);
	}

	/**
	 * gets the current position of a user.
	 * 
	 * @param user the identifier of the user.
	 * @return the current position of the user.
	 */
	// TODO: Annotations
	
	public synchronized Position getCurrentPosition(final String user) {
		// delegates to GraphOfPositionsForEmulation
		return GraphOfPositionsForEmulation.getCurrentPosition(user);
	}

	/**
	 * steps to the next position in the current path---i.e. towards the next POI.
	 * It returns the new position of the user, or the same if the end of the path
	 * is already reached.
	 * 
	 * @param user the identifier of the user.
	 * @return the new position of the user, or the same if the end of the path is
	 *         already reached.
	 */
	// TODO: Annotations
	
	public synchronized Position stepInCurrentPath(final String user) {
		// delegates to GraphOfPositionsForEmulation
		return GraphOfPositionsForEmulation.stepInCurrentPath(user);
	}

	// the other methods of the API

	/**
	 * when at a POI, steps in the visit---i.e. computes the path to the next POI.
	 * If the current POI is the last one, i.e. the user is already at the end of
	 * the visit, the position that is returned is the position of the current POI.
	 * 
	 * @param user the identifier of the user.
	 * @return the next position.
	 */
	// TODO: Annotations
	
	public synchronized Position stepsInVisit( final String user) {
		if (GraphOfPositionsForEmulation.getAdjacencySets() == null
				|| GraphOfPositionsForEmulation.getAdjacencySets().isEmpty()) {
			throw new IllegalStateException("There is no graph of positions");
		}
		if (visits.get(user) == null) {
			throw new IllegalArgumentException("user " + user + " has no visit set");
		}
		if (visits.get(user).isEmpty()) {
			throw new IllegalArgumentException("the visit of user " + user + " is empty");
		}
		if (currentIndicesInVisits.get(user) == null) {
			throw new IllegalArgumentException("user " + user + " has no current index in visit set");
		}
		if (currentIndicesInVisits.get(user) == visits.get(user).size() - 1) {
			EMULATION.info("{}", () -> user + ": already at the end of their visit (no new current path)");
		} else {
			currentIndicesInVisits.put(user, currentIndicesInVisits.get(user) + 1);
			GraphOfPositionsForEmulation.setAPathTo(user, visits.get(user).get(currentIndicesInVisits.get(user)));
		}
		return visits.get(user).get(currentIndicesInVisits.get(user));
	}
}

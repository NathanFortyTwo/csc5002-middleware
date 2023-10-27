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

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.GPSPosition;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_visit_emulation_api.VisitEmulationService;
import vlibtour.vlibtour_visit_emulation_api.VisitEmulationTourInitRequest;

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

@Path("/visitemulation")
public final class VisitEmulationServer implements VisitEmulationService {
	private static Map<String, VisitEmulationTourInfo> visitEmulationTourInfo = new HashMap<>();
	private static Map<String, String> userIdToTourId = new HashMap<>();

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
		// create a resource config that scans for JAX-RS resources and providers
		// in the server package
		final ResourceConfig rc = new ResourceConfig().packages(VisitEmulationServer.class.getPackage().getName());
		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI_WEB_SERVER
		final HttpServer server = GrizzlyHttpServerFactory
				.createHttpServer(URI.create(BASE_URI_WEB_SERVER), rc);
		EMULATION.debug("{}", () -> String.format("Jersey app started with WADL available at " + "%sapplication.wadl",
				BASE_URI_WEB_SERVER));
		Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
		server.start();
		Thread.currentThread().join();
	}

	@POST
	@Path("/initATourForAGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Position initATourForAGroup(VisitEmulationTourInitRequest request) {
		String tourId = request.getTourId();
		String userId = request.getUserId();
		List<Position> POIs = request.getPOIs();

		if (visitEmulationTourInfo.containsKey(tourId)) {
			// Tour already initialised, check for user in tour
			if (userIdToTourId.containsKey(userId)) {
				// User already in tour return first position
				return visitEmulationTourInfo.get(tourId).getGraphOfPositionsForEmulation()
						.getCurrentPosition(userId);
			}
			// Else we will add the user to the tour
		} else {
			// Tour not initialised, initialise it
			GraphOfPositionsForEmulation graph = new GraphOfPositionsForEmulation();
			Map<String, List<Position>> visits = new HashMap<>();
			Map<String, Integer> currentIndicesInVisits = new HashMap<>();

			graph.setAdjacencySets(VisitEmulationServer.initTourWithPOIs(graph));
			System.out.println("Graph initialised");
			visitEmulationTourInfo.put(tourId, new VisitEmulationTourInfo(graph, currentIndicesInVisits, visits));
		}

		// Add user to tour
		userIdToTourId.put(userId, tourId);
		visitEmulationTourInfo.get(tourId).getGraphOfPositionsForEmulation().setStartingPosition(userId,
				DEFAULT_DEPARTURE_POSITION);
		visitEmulationTourInfo.get(tourId).getVisits().put(userId, POIs);
		visitEmulationTourInfo.get(tourId).getCurrentIndicesInVisits().put(userId, 0);
		visitEmulationTourInfo.get(tourId).getGraphOfPositionsForEmulation().setAPathTo(userId, POIs.get(0));

		return visitEmulationTourInfo.get(tourId).getGraphOfPositionsForEmulation().getCurrentPosition(userId);
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
	static Map<Position, Set<Position>> initTourWithPOIs(
			final GraphOfPositionsForEmulation graphOfPositionsForEmulation) {
		Map<Position, Set<Position>> adjacencyLists = new HashMap<>();

		// Create positions for Points of Interest (POIs)
		Position[] poiPositions = {
				new Position("Musée Pooja Namo", new GPSPosition(48.869301, 2.3450524)),
				new Position("Elysée", new GPSPosition(48.8738596, 2.3320555)),
				new Position("Place Saint Augustin", new GPSPosition(48.8754451, 2.3196203)),
				new Position("Musée Grévin", new GPSPosition(48.871799, 2.342355)),
				new Position("Bonne Nouvelle", new GPSPosition(48.870998, 2.3472050)),
				new Position("Strasbourg Saint-Denis", new GPSPosition(48.8695756, 2.352890)),
				new Position("La Bourse", new GPSPosition(48.8674807, 2.33698)),
				new Position("43, rue de Cléry", new GPSPosition(48.8687025, 2.3464083)),
				new Position("123, bd Sébastopol", new GPSPosition(48.867572, 2.353312)),
				new Position("37, rue du Mail", new GPSPosition(48.8673138, 2.3427597)),
				new Position("Jardin du Palais Royal", new GPSPosition(48.866154, 2.338562)),
				new Position("26, rue de Turbigo", new GPSPosition(48.864414, 2.351565)),
				new Position("33, rue de Valois", new GPSPosition(48.865320, 2.338845)),
				new Position("14, rue de Turbigo", new GPSPosition(48.863824, 2.348924)),
				new Position("Galerie de Valois", new GPSPosition(48.864007, 2.337890)),
				new Position("Jardin Nelson Mandela", new GPSPosition(48.862928, 2.345094)),
				new Position("La Comédie Française", new GPSPosition(48.8637511, 2.3361412)),
				new Position("154, rue de Rivoli", new GPSPosition(48.8609335, 2.3385873)),
				new Position("Pyramide du Louvre", new GPSPosition(48.860959, 2.335757)),
				new Position("41, rue de Rivoli", new GPSPosition(48.858472, 2.348140)),
				new Position("6 quai François Mitterand", new GPSPosition(48.860148, 2.333168)),
				new Position("Île de la Cité", new GPSPosition(48.855201, 2.347953)),
				new Position("Pont du Caroussel", new GPSPosition(48.859618, 2.333050)),
				new Position("Parvis de la Cathédrale Notre Dame", new GPSPosition(48.853563, 2.347127)),
				new Position("1, rue du Bac", new GPSPosition(48.859562, 2.329248)),
				new Position("Pont du Caroussel", new GPSPosition(48.858757, 2.332578)),
				new Position("rue du Bac", new GPSPosition(48.8543549, 2.3253203)),
				new Position("43, rue de la Bucherie", new GPSPosition(48.852815, 2.346655)),
				new Position("1, rue de Nevers", new GPSPosition(48.856075, 2.340424)),
				new Position("rue de Rennes", new GPSPosition(48.847706, 2.3269443)),
				new Position("Square André-Lefèvre", new GPSPosition(48.851410, 2.345840)),
				new Position("Place Saint-Michel", new GPSPosition(48.853762, 2.344292)),
				new Position("Église Notre Dame des Champs", new GPSPosition(48.843992, 2.323693)),
				new Position("52, rue des Écoles", new GPSPosition(48.849617, 2.344853)),
				new Position("26 bd Saint-Michel", new GPSPosition(48.850380, 2.342704)),
				new Position("8 rue de la Gaité", new GPSPosition(48.840605, 2.324314)),
				new Position("103, bd Saint-Michel", new GPSPosition(48.843863, 2.338773)),
				new Position("Théâtre Odéon", new GPSPosition(48.845173, 2.3333816)),
				new Position("91 avenue duMaine", new GPSPosition(48.837597, 2.322994)),
				new Position("Port-Royal", new GPSPosition(48.839795, 2.337056)),
				new Position("Odéon", new GPSPosition(48.845074, 2.332309)),
				new Position("2, rue Fernat", new GPSPosition(48.836658, 2.325762)),
				new Position("Hôpital Val-de-Grace", new GPSPosition(48.840069, 2.337088)),
				new Position("Observatoire Assas", new GPSPosition(48.8425568, 2.3322554)),
				new Position("21, rue Froidevaux", new GPSPosition(48.835549, 2.328830)),
				new Position("Denfert Rochereau", new GPSPosition(48.835436, 2.333569)),
				new Position("Les catacombes", new GPSPosition(48.833566, 2.332416))
		};

		int[][] paths = {
				{ 1, 6 },
				{ 2, 3, 4, 5, 6, 9, 12, 14, 16, 18, 19 },
				{ 4, 7, 11, 13, 15, 17, 19 },
				{ 5, 8, 10, 11 },
				{ 19, 20, 22, 24, 28, 31, 34, 37, 40, 43, 46, 47 },
				{ 19, 21, 23, 26, 29, 32, 35, 38, 41, 44, 43 },
				{ 26, 25, 27, 30, 33, 36, 39, 42, 45, 47, 46 }
		};

		// Add edges for each path
		for (int[] path : paths) {
			for (int i = 0; i < path.length - 1; i++) {
				Position from = poiPositions[path[i] - 1];
				Position to = poiPositions[path[i + 1] - 1];

				graphOfPositionsForEmulation.addEdge(adjacencyLists, from, to);
			}
		}

		return adjacencyLists;
	}

	private GraphOfPositionsForEmulation getUserGraphOfPositionsForEmulation(final String userId) {
		String tourId = userIdToTourId.get(userId);
		if (tourId == null) {
			throw new IllegalArgumentException("user " + userId + " has no tourId");
		}
		return visitEmulationTourInfo.get(tourId).getGraphOfPositionsForEmulation();
	}

	/**
	 * gets the position of the next POI to visit when arrived at the end of the
	 * current path, that is the next POI in the visit. This position is the last
	 * one of the current path.
	 * 
	 * @param user the identifier of the user.
	 * @return the position of the current POI.
	 */
	@GET
	@Path("/getNextPOIPosition/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Position getNextPOIPosition(@PathParam("user") final String user) {
		// delegates to GraphOfPositionsForEmulation
		return getUserGraphOfPositionsForEmulation(user).getNextPOIPosition(user);
	}

	/**
	 * gets the current position of a user.
	 * 
	 * @param user the identifier of the user.
	 * @return the current position of the user.
	 */
	@GET
	@Path("/getCurrentPosition/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Position getCurrentPosition(@PathParam("user") final String user) {
		// delegates to GraphOfPositionsForEmulation

		return getUserGraphOfPositionsForEmulation(user).getCurrentPosition(user);
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
	@POST
	@Path("/stepInCurrentPath/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Position stepInCurrentPath(@PathParam("user") final String user) {
		// delegates to GraphOfPositionsForEmulation
		return getUserGraphOfPositionsForEmulation(user).stepInCurrentPath(user);
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
	@POST
	@Path("/stepsInVisit/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized Position stepsInVisit(@PathParam("user") final String user) {
		VisitEmulationTourInfo tourInfo = visitEmulationTourInfo.get(userIdToTourId.get(user));

		GraphOfPositionsForEmulation graph = tourInfo.getGraphOfPositionsForEmulation();
		Map<String, Integer> currentIndicesInVisits = tourInfo.getCurrentIndicesInVisits();
		Map<String, List<Position>> visits = tourInfo.getVisits();

		if (graph.getAdjacencySets() == null || graph.getAdjacencySets().isEmpty()) {
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
			getUserGraphOfPositionsForEmulation(user).setAPathTo(user,
					visits.get(user).get(currentIndicesInVisits.get(user)));
		}
		return visits.get(user).get(currentIndicesInVisits.get(user));
	}
}

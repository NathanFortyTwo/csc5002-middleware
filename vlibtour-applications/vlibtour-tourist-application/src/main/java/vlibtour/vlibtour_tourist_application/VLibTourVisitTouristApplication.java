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
package vlibtour.vlibtour_tourist_application;

import static vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists.USER_ID_AVREL;
import static vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists.USER_ID_JOE;
import static vlibtour.vlibtour_common.Log.VLIBTOUR;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;
import vlibtour.vlibtour_scenario.map_viewer.BasicMap;
import vlibtour.vlibtour_scenario.map_viewer.MapHelper;
import vlibtour.vlibtour_tour_management_entity.POI;
import vlibtour.vlibtour_tour_management_entity.Tour;
import vlibtour.vlibtour_tour_management_entity.VlibTourTourManagementException;
import vlibtour.vlibtour_tourist_application.group_communication_proxy.VLibTourGroupCommunicationSystemProxy;
import vlibtour.vlibtour_tourist_application.lobby_room_proxy.VLibTourLobbyRoomProxy;
import vlibtour.vlibtour_tourist_application.tour_management_proxy.VLibTourTourManagementProxy;
import vlibtour.vlibtour_tourist_application.visit_emulation_proxy.VisitEmulationProxy;

/**
 * This class is the client application of the tourists. The access to the lobby
 * room server, to the group communication system, and to the visit emulation
 * server visit should be implemented using the design pattern Delegation (see
 * https://en.wikipedia.org/wiki/Delegation_pattern).
 * 
 * A client creates two queues to receive messages from the broker; the binding
 * keys are of the form "{@code *.*.identity}" and "{@code *.*.location}" while
 * the routing keys are of the form "{@code sender.receiver.identity|location}".
 * 
 * This class uses the classes {@code MapHelper} and {@code BasicMap} for
 * displaying the tourists on the map of Paris. Use the attributes for the
 * color, the map, the map marker dot, etc.
 * 
 * @author Denis Conan
 */
public class VLibTourVisitTouristApplication {
	/**
	 * the long duration: painting the map takes around 1s per user (thus, 2 x
	 * 1000).
	 */
	private static final long LONG_DURATION = 2000;
	/**
	 * the map to manipulate. Not all the clients need to have a map, but only the
	 * initiator.
	 */
	private static BasicMap map;
	/**
	 * the dots of the tourists on the map.
	 */
	@SuppressWarnings("unused")
	private static Map<String, MapMarkerDot> mapDots;
	/**
	 * the user identifier of the tourist application.
	 */
	@SuppressWarnings("unused")
	private String userId;
	/**
	 * delegation to the proxy of type
	 * {@link vlibtour.vlibtour_tourist_application.tour_management_proxy.VLibTourTourManagementProxy}.
	 */
	@SuppressWarnings("unused")
	private static VLibTourTourManagementProxy tourManagementProxy;
	/**
	 * delegation to the proxy of type
	 * {@link vlibtour.vlibtour_tourist_application.lobby_room_proxy.VLibTourLobbyRoomProxy}.
	 */
	@SuppressWarnings("unused")
	private static VLibTourLobbyRoomProxy lobbyRoomProxy = new VLibTourLobbyRoomProxy();
	/**
	 * delegation to the proxy of type
	 * {@link vlibtour.vlibtour_tourist_application.group_communication_proxy.VLibTourGroupCommunicationSystemProxy}.
	 */
	@SuppressWarnings("unused")
	private VLibTourGroupCommunicationSystemProxy groupCommProxy;
	/**
	 * delegation to the proxy of type
	 * {@link vlibtour.vlibtour_emulation_visit_proxy.VLibTourVisitEmulationProxy}.
	 */
	@SuppressWarnings("unused")
	private static VisitEmulationProxy visitEmulationProxy;

	/**
	 * creates a client application, which will join to a group communication
	 * system. The algorithm is the following one:
	 * <ul>
	 * <li>Create the AMQP artifacts (e.g. virtual hosts, users with their
	 * corresponding login and password, etc.) of the group communication system by
	 * calling the lobby room microservice via the {@link #visitEmulationProxy}
	 * proxy.</li>
	 * <li>Instantiate the {@link #groupCommProxy} proxy, which opens a connection,
	 * a channel and create an exchange, a queue, etc.</li>
	 * <li>Set the consumer of RabbitMQ messages of the group communication system
	 * (via a call to @link #groupCommProxy}):
	 * <ul>
	 * <li>the consumer is a default consumer;</li>
	 * <li>the {@code handleDelivery} method of the consumer implements some of the
	 * functionality of the tourist application, i.e. get members' new position,
	 * check whether all the members have reached the next POI, etc.
	 * <p>
	 * (Please, do not implement all these parts in the {@code handleDelivery}
	 * method of the anonymous class, but create instance methods!)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param tourId      the tour identifier of this application.
	 * @param gcsId       the identifier of the group communication system that the
	 *                    application is going to connect to (create/join). AMQP
	 *                    speaking, this string is used to name the virtual
	 *                    host—i.e. by a slight abuse of language, the group
	 *                    communication system.
	 * @param userId      the user identifier of this client application.
	 * @param isInitiator the boolean stating whether this user is the initiator of
	 *                    the visit.
	 * @throws InAMQPPartException             the exception thrown in case of a
	 *                                         problem in the AMQP part.
	 * @throws VlibTourTourManagementException problem in the name or description of
	 *                                         POIs.
	 * @throws IOException                     problem when setting the
	 *                                         communication configuration with the
	 *                                         broker.
	 * @throws TimeoutException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws JsonRpcException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws InterruptedException            thread interrupted in call sleep.
	 */
	public VLibTourVisitTouristApplication(final String tourId, final String gcsId, final String userId,
			final boolean isInitiator) throws InAMQPPartException, VlibTourTourManagementException, IOException,
			JsonRpcException, TimeoutException, InterruptedException {
		if (tourId == null || tourId.equals("")) {
			throw new IllegalArgumentException("tourId cannot be null");
		}
		if (userId == null || userId.equals("")) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		this.userId = userId;
		// TODO LOBBYROOM and GROUPCOMM
		// call the lobby room proxy in order to create the AMQP artifacts (e.g. virtual
		// hosts, users with their corresponding login and password, etc.) of the group
		// communication system
		// this call should return the necessary information for the creation of the
		// group communication proxy
		// GCSInfo gcsInfo = xxx

		// TODO GROUPCOMM
		// instantiate the group communication proxy, which opens a connection, a
		// channel and create an exchange, a queue, etc.
		// groupCommProxy = xxx
		groupCommProxy = new VLibTourGroupCommunicationSystemProxy(gcsId, userId);

		// TODO GROUPCOMM
		// set the consumer of RabbitMQ messages of the group communication system (via
		// a call to the proxy)
		// - the consumer is a default consumer
		// - the handleDelivery method of the consumer implements some of the
		// functionality of the tourist application, i.e. get members' new position,
		// check whether all the members have reached the next POI, etc.
		// (Please, do not implement all these parts in the handleDelivery
		// method of the anonymous class, but create instance methods!)
		// - set the consumer, etc.
		DefaultConsumer consumer = new DefaultConsumer(groupCommProxy.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope,
					com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				VLIBTOUR.info("{}", () -> " [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				// TODO GROUPCOMM
				// call the method of the tourist application that implements the functionality
				// of the tourist application
				// - get members' new position
				// - check whether all the members have reached the next POI
				// - etc.
				// Check if rounting key is a position
				if (envelope.getRoutingKey().contains("position")) {
					// Create a UserPosition object from the body
					Position userPosition = Position.GSON.fromJson(message, Position.class);
					String userId = envelope.getRoutingKey().split("\\.")[0];
					try {
						onReceivePosition(userPosition, userId);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		groupCommProxy.setConsumer(consumer);

	}

	public static void onReceivePosition(Position userPosition, String userId) throws InterruptedException {
		System.out.println(userId + " " + userPosition.getName());
		MapMarkerDot userDot = mapDots.get(userId);
		MapHelper.moveTouristOnMap(userDot, userPosition);
		map.repaint();
		Thread.sleep(LONG_DURATION);
	}

	/**
	 * executes the tourist application. <br>
	 * We prefer insert comments in the method instead of detailing the method here.
	 * 
	 * @param args the command line arguments.
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		String usage = "USAGE: " + VLibTourVisitTouristApplication.class.getCanonicalName()
				+ " userId (either Joe or Avrel) mode (initiate or join) tourId groupId";
		final int nbArgs = 4;
		if (args.length != nbArgs) {
			throw new IllegalArgumentException(usage);
		}
		String userId = args[0];
		if (!userId.equals(USER_ID_AVREL) && !userId.equals(USER_ID_JOE)) {
			throw new IllegalArgumentException("user id must be either " + USER_ID_AVREL + " or " + USER_ID_JOE);
		}
		boolean isInitiator = args[1].equals("initiate");
		String tourId = args[2];
		if (tourId == null || tourId.equals("")) {
			throw new IllegalArgumentException("tourId cannot be null");
		}
		String groupId = args[nbArgs - 1];
		if (groupId == null || groupId.equals("")) {
			throw new IllegalArgumentException("visitAlias cannot be null");
		}
		if (!groupId.equals(ExampleOfAVisitWithTwoTourists.DALTON_GROUP_ID)) {
			throw new IllegalArgumentException(
					"For now, we only know the groupId " + ExampleOfAVisitWithTwoTourists.DALTON_GROUP_ID);
		}
		mapDots = new java.util.HashMap<>();
		Set<String> group = ExampleOfAVisitWithTwoTourists.DALTON_GROUP;
		VLIBTOUR.info("{}", () -> userId + "'s application is starting");
		// instantiate the tour management proxy in order to get the list of POIs of the
		// tour
		// TODO TOUR

		// instantiate the tourManagement proxy
		// VLibTourTourManagementProxy tourManagementProxy = new
		// VLibTourTourManagementProxy();

		// TODO TOUR (necessary for the VISITEMULATION)
		// populate the tour information
		Tour tour = new Tour(tourId, "description of " + tourId);
		POI poi1 = new POI(
				ExampleOfAVisitWithTwoTourists.POSITION4.getName(),
				ExampleOfAVisitWithTwoTourists.POSITION4.getDescription(),
				ExampleOfAVisitWithTwoTourists.POSITION4.getGpsPosition().getLatitude(),
				ExampleOfAVisitWithTwoTourists.POSITION4.getGpsPosition().getLongitude());
		tour.addPOI(poi1);

		POI poi2 = new POI(
				ExampleOfAVisitWithTwoTourists.POSITION19.getName(),
				ExampleOfAVisitWithTwoTourists.POSITION19.getDescription(),
				ExampleOfAVisitWithTwoTourists.POSITION19.getGpsPosition().getLatitude(),
				ExampleOfAVisitWithTwoTourists.POSITION19.getGpsPosition().getLongitude());
		tour.addPOI(poi2);

		POI poi3 = new POI(
				ExampleOfAVisitWithTwoTourists.POSITION47.getName(),
				ExampleOfAVisitWithTwoTourists.POSITION47.getDescription(),
				ExampleOfAVisitWithTwoTourists.POSITION47.getGpsPosition().getLatitude(),
				ExampleOfAVisitWithTwoTourists.POSITION47.getGpsPosition().getLongitude());
		tour.addPOI(poi3);

		// TODO GROUPCOMM
		// the identifier of the group communication system is the string concatenation
		// of the identifier of the tour,
		// VLibTourLobbyService.GROUP_TOUR_USER_DELIMITER, and the identifier of the
		// group
		// AMQP speaking, this string is used to name the virtual host—i.e. by a slight
		// abuse of language, the group communication system
		String gcsId = tourId + VLibTourLobbyService.GROUP_TOUR_USER_DELIMITER + groupId;
		// instantiate the VLibTourVisitTouristApplication
		final VLibTourVisitTouristApplication client = new VLibTourVisitTouristApplication(tourId, gcsId, userId,
				isInitiator);
		final long shortDuration = 1000;

		// TODO VISITEMULATION instantiate the visit emulation proxy
		VisitEmulationProxy visitEmulationProxy = new VisitEmulationProxy();

		// TODO VISITEMULATION through the emulation proxy, get the first position of
		// the tourist
		// who launched the application (whose name is in userId)
		Position startingPosition = ExampleOfAVisitWithTwoTourists.DEPARTURE_POSITION;
		// if this is the initiator
		// - set the map viewer of the scenario
		// - while setting the map, check that the POIs of the tour match the ones
		// defined in ExampleOfAVisitWithTwoTourists.POSITIONS_OF_SOME_POIS

		if (isInitiator) {
			// CHECKSTYLE:OFF
			map = MapHelper.createMapWithCenterAndZoomLevel(48.851412, 2.343166, 14);
			Font font = new Font("bold20", Font.BOLD, 20);
			// CHECKSTYLE:ON
			// Show the POIs on the map
			for (POI poi : tour.getPOIs()) {
				Optional<Position> position = ExampleOfAVisitWithTwoTourists.POSITIONS_OF_SOME_POIS.stream()
						.filter(p -> poi.getName().equals(p.getName())).findFirst();
				if (position.isEmpty()) {
					throw new IllegalStateException("the position of POI " + poi.getName()
							+ " has not been found in ExampleOfAVisitWithTwoTourists.POSITIONS_OF_SOME_POIS");
				}
				MapHelper.addMarkerDotOnMap(map, position.get().getGpsPosition().getLatitude(),
						position.get().getGpsPosition().getLongitude(), Color.BLACK, font, poi.getName());
			}

			Thread.sleep(shortDuration);
			// TODO VISITEMULATION
			// Show userId first position on the map

			// TODO show other users first position on the map, they can start at the same
			// position as the initiator
			// Repaint the modified map
			for (String username : group) {
				MapMarkerDot userDot = MapHelper.addTouristOnMap(map, ExampleOfAVisitWithTwoTourists.COLOR_TOURIST,
						font,
						username, startingPosition);

				mapDots.put(username, userDot);
			}
			map.repaint();
			// wait for painting the map
			Thread.sleep(LONG_DURATION);
		}
		// TODO GROUPCOMM
		// start the consumption of messages (e.g. positions of group members)
		// from the group communication system
		client.groupCommProxy.startConsuming();

		// TODO GROUPCOMM and VISITEMULATION
		Thread.sleep(LONG_DURATION * 5);
		MapMarkerDot userDot = mapDots.get(userId);
		while (userDot != null) {
			Position nextPOIPosition = visitEmulationProxy.getNextPOIPosition(userId);
			while (true) {
				Position currentPositionInPath = visitEmulationProxy.stepInCurrentPath(userId);
				// When steping in path, publish the position
				client.groupCommProxy.publish(Position.GSON.toJson(currentPositionInPath),
						VLibTourGroupCommunicationSystemProxy.BROADCAST_POSITION);
				System.out.println(currentPositionInPath.getName());

				if (currentPositionInPath.getName().equals(nextPOIPosition.getName())) {
					break; // Reached the next POI
				}

			}

			Position nextPOI = visitEmulationProxy.stepsInVisit(userId);
			if (nextPOI.getName().equals(nextPOIPosition.getName())) {
				break; // End of the visit
			}
		}
		// loop until the end of the visit is detected
		// while looping,
		// VISITEMULATION Update UserId Position
		// Update other group members position
		// For the initiator
		// Move all the users on the map with
		// MapHelper.moveTouristOnMap(<username>,<userposition>);
		// Repaint the map with map.repaint();
		// wait for painting the map with Thread.sleep(LONG_DURATION);

		// TODO GROUPCOMM and LOBBYROOM
		// At the end of the loop
		// lobby room proxy: nothing to close
		// group communication proxy: close the channel and the connection
		client.groupCommProxy.close();

		// TODO VISITEMULATION
		// visit emulation proxy: close
		visitEmulationProxy.close();
		// close the map
		if (isInitiator) {
			map.dispatchEvent(new WindowEvent(map, WindowEvent.WINDOW_CLOSING));
		}
		Thread.sleep(2 * LONG_DURATION);
		VLIBTOUR.info("{}", () -> "\n\nThe demonstration has finished. Hit return to end the services.\n\n");
	}
}

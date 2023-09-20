
Vlibtour Tourist Application
====

This module is the client application for the tourists.

In the package `vlibtour.vlibtour_scenario.map_viewer`, it proposes helper methods for painting the maps of Paris with users as MapMarkerDot objects.

In that same package, one can refer to the `MapDemo` class for learning how to draw the map, add users, and move them on the map.
For instance, to see how it runs, execute the map demonstration:

	$ mvn clean install
	$ mvn exec:java@map

NB: command `mvn exec:java@map` is proposed only for demonstration purposes.
It is not the command that is used for running the scenario of the demonstration of the project.

The `VLibTourVisitTouristApplication` is the main class of the tourist application.
For running the tourist application, execute the following script:

	$ start_tourist_application_w_emulated_location.sh

This shell script is used in the demonstration shell script, i.e. `run_scenario_w_mapviewer.sh`.

For implementing the tourist application, the proposal is to bring into play proxies that are used by delegation in class `VLibTourVisitTouristApplication`.
There are four classes, each one in a dedicated package, that corresponds to the proxies to the four microservices:

1. Class `VLibTourTourManagementProxy` that contains the EJB stub to access the tour management EJB bean.
2. Class `VLibTourLobbyRoomProxy` that contains the AMQP aspects for synchronous calls to the lobby room server.
3. Class `VLibTourGroupCommunicationSystemProxy` that contains the AMQP aspects for broadcasting asynchronous messages that are shared between the tourists of a group.
4. Class `VisitEmulationProxy` that contains the REST stub to obtain tourist's positions from the visit emulation server.

Advice: one should test the proxies in integration tests before using them in class `VLibTourVisitTouristApplication`.

NB: JUnit integration test classes are named after the pattern `*IT`,
which are executed with command `mvn failsafe:integration-test failsafe:verify`,
i.e. using the Maven plugin `failsafe`.
Recall that the JUnit unit tests are executed by the Maven plugin `surefire`.

The `run_integration_tests.sh` shell script executes the Maven commands for running these integration tests.

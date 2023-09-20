
Server of the Vlibtour Visit Emulation microservice
====

The visit emulation server is able to follow a group of tourists for a visit, in particular it updates the next POI for the visit.
The functionalities are the following ones:

- get the current position of a tourist,
- get the position of the next POI,
- step to the next position in current path towards the next POI,
- step to the next POI in current visit.

The visit emulation server requires two arguments:
(1) the name of the group of users,
(2) the name of the tour of the visit, with the departure position and the sequence of POIs.

For the emulation, class `GraphOfPositionsForEmulation` implements a graph of positions, some of which correspond to POI,
and some utility functions such as computing the simple paths of the graph between two nodes.

A prototypical usage of these functionalities are as follows:

1. Start the server for a group of users and a tour for the visit.
2. Start a client, which should be a member of the group that the server is aware of.
3. The client gets from the server the current position, i.e. the departure position of their visit.
4. The client can call the server to know the next POI.
4. The client can call the server to step in the path the next POI.
5. When at a POI, the client can call the server to know whether the current POI is the end of the visit.
6. When at a POI, the client can call the server to step in the visit to the next POI.

Class `VisitEmulationServer` must conform to the API defined in interface `VisitEmulationService`.

This module contains both JUnit unit test classes (whose names are patterned after `Test*` or `*Test`) and
JUnit integration test classes (whose names are patterned after `*IT`).

Recall that JUnit unit tests are executed with commands `mvn test` or `mvn install`, i.e. by the `surefire` Maven plugin and
that JUnit integration tests are executed with command `mvn failsafe:integration-test failsafe:verify`,
i.e. using the `failsafe` Maven plugin.
Practically, unit tests and integration tests are executed by launching commands:

	$ mvn install # unit tests when compiling and installing the module
	$ ./run_integration_tests.sh # integration tests when executing the failsafe plugin

Therefore, after implementing the class of the visit emulation server, i.e. class `VisitEmulationServer` with Jakarta REST WebService annotations,
one must implement the classes of the unit tests and the classes of the integration tests.

The visit emulation is started by executing the following command:

	$ ./start_visit_emulation_server.sh

This shell script is used in integration tests (cf. shell script `run_integration_tests.sh` in module `vlibtour-tourist-application`)
and in the demonstration (cf. shell script `run_scenario_w_mapviewer.sh`).

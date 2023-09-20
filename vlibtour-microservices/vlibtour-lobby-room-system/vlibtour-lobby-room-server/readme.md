
Server of the Vlibtour Lobby Room microservice
====

This module implements the lobby room server. It must conform to the API defined in module `vlibtour-lobby-room-api`
(see the Maven dependency in file `pom.xml`).

The integration tests are executed in the `run_integration_tests.sh` shell script by using Maven plugin `failsafe`.
Recall that JUnit integration test classes are named after the pattern `*IT`.

Therefore, after implementing the class of the lobby room server, i.e. class `VLibTourLobbyServer`,
one must implement the class of the integration tests, i.e. class `VLibTourLobbyServerIT`.

Class `VLibTourLobbyServer` must conform to the API defined in interface `VLibTourLobbyService`.

Advise: Before using the lobby room server either in integration tests of client applications or in the scenario of the demonstrator,
one should test the lobby room server by writing integration tests in this module.

NB: JUnit integration test classes are named after the pattern `*IT` are integration tests,
which are executed with command `mvn failsafe:integration-test failsafe:verify`,
i.e. using the Maven plugin `failsafe`.
Recall that the JUnit unit tests are executed by the Maven plugin `surefire`.

The `run_integration_tests.sh` shell script executes the Maven commands for running these integration tests.

The lobby room server is started by executing the following command:

	$ ./start_lobby_room_server.sh

This shell script is used in integration tests (cf. shell scripts `run_integration_tests.sh` in this module and in module `vlibtour-tourist-application`)
and in the demonstration (cf. shell script `run_scenario_w_mapviewer.sh`).



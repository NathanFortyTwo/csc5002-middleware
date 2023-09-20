
Vlibtour Lobby Room microservice
====

This module declares the two modules of the microservice, namely the module for defining the API and the module that implements the server, which conforms to the API.

Therefore, one must firstly define the API, then implement the server, and finally, test the server by writing integration tests.

The integration tests of the server are executed in the `run_integration_tests.sh` shell script in the module of the server,
i.e. in `vlibtour-lobby-room-server/run_integration_tests.sh`.
See the `readme.md` file in the module of the server for more information on the integration tests.

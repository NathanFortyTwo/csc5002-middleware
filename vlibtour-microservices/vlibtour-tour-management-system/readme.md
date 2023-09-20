
Vlibtour Tour Management microservice
====

This module declares the three modules of the microservice, namely the module for defining the API,
the module for defining the EJB entities, and the module that implements the EJB bean/server, which conforms to the API and manipulates entities.

There is a fourth module for writing integration tests. Please refer the `readme.md` file of that module for some explanations about
a dedicated module for integration tests.

Therefore, one must firstly define the API, then define the entities, next implement the server, and finally, test the server by writing integration tests.

The integration tests of the server are executed in the `run_integration_tests.sh` shell script in this directory.

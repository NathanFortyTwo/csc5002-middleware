
Vlibtour Tour Management integration tests
====

This module contains the JUnit integration test classes of the tour management microservice.

The integration tests implemented in this module are not JUnit integration tests because there exist incompatibility between JUnit and EJB Glassfish client library:
problem with EJB lookup operations.
One may have used an embedded Glassifsh server, which is especially developped for JUnit testing of EJB beans.

The `run_integration_tests.sh` shell script that is present in the parent directory executes the Maven commands for running these integration tests.

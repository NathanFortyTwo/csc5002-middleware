
EJB Bean/Server of the Vlibtour Tour Management microservice
====

This module implements the tour management bean/server.
It must conform to the API defined in module `vlibtour-tour-management-api` and to the entities defined in module `vlibtour-tour-management-entity`
(see the Maven dependency in file `pom.xml`).

There are no integration tests in this directory.
But, the integration tests are written in the dedicated module called `vlibtour-tour-management-integration-tests`.
These integration tests are executed by executing the `run_integration_tests.sh` shell script that is present in the parent directory.

Therefore, after implementing the tour management EJB bean/server, i.e. class `VlibTourTourManagementBean`,
one must implement the class of the integration tests,
i.e. class `VlibTourTourManagementBeanIntegrationTests` in module `vlibtour-tour-management-integration-tests`.

Class `VlibTourTourManagementBean` must conform to the API defined in interface `VlibTourTourManagementService`.

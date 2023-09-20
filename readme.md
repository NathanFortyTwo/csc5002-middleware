CSC5002 case study "VlibTour"
=======

## 1. Directory structure

The project contains the following files and sub-directories:

	$ tree -L 2
	.
	├── pom.xml
	├── readme.md
	├── run_integration_tests.sh
	├── run_scenario_w_mapviewer.sh
	├── vlibtour-applications
	│   ├── vlibtour-tourist-application
	│   └── vlibtour-tour-management-admin-client
	├── vlibtour-libraries
	│   ├── geocalc
	│   ├── pom.xml
	│   └── vlibtour-common
	└── vlibtour-microservices
	    ├── run_integration_tests.sh
	    ├── vlibtour-bikestation
	    ├── vlibtour-lobby-room-system
	    ├── vlibtour-tour-management-system
	    └── vlibtour-visit-emulation-system
	
In short:

- The `pom.xml` is the root description of Maven modules with the list of sub-modules and the common configuration of the building process.

- The `readme.md` is this file. Other `readme.md` files are provided in the sub-directories.

- The `run_integration_tests.sh` shell script is used for executing integration tests, after compiling and installing, and before running the demonstration.
This shell script performs a call to the integration tests scripts of each microservice, and then of the client applications.

- The `run_scenario_w_mapviewer.sh` shell script is for running the demonstration.

- The `vlibtour-applications` sub-directory contains the two client application modules.
Please refer to the document describing the software architecture of the demonstrator:
[https://www-inf.telecom-sudparis.eu/COURS/CSC5002/Chap/sujetmicroprojetvlibtour-chap.pdf](https://www-inf.telecom-sudparis.eu/COURS/CSC5002/Chap/sujetmicroprojetvlibtour-chap.pdf)

- The `vlibtour-libraries` sub-directory contains the two libraries that are used by the client applications and the microservices.
Library `geocalc` is an open source software that is copied from GitHub and included in the building process of the software.
Library `vlibtour-common` contains a few classes that are used by all the other modules,
namely classes `Position` and `GPSPosition`, class `Log`, and class `ExampleOfAVisitWithTwoTourists`.
Class `ExampleOfAVisitWithTwoTourists` defines the constants of the demonstration, e.g. two tourists with their departure position, etc.

- The `vlibtour-microservices` contains the Maven modules of the microservices. 
Please refer to the document describing the software architecture of the demonstrator:
[https://www-inf.telecom-sudparis.eu/COURS/CSC5002/Chap/sujetmicroprojetvlibtour-chap.pdf](https://www-inf.telecom-sudparis.eu/COURS/CSC5002/Chap/sujetmicroprojetvlibtour-chap.pdf)

## 2. From installing to running the demonstrator

### 2.1. Installing

To compile and install all the Maven module, execute the following command:

	$ mvn clean install

The `install` Maven command includes running the JUnit unit tests, that is the test classes named after patterns `*Test` or `Test*`.
The JUnit unit tests are run with the Maven plugin `surefire`.
Recall that these test classes are under directories `src/test/java`, and not `src/main/java`.

### 2.2. Running the integration tests

After installing the Maven modules and before running the demonstration, execute the following command to run the integration tests:

	$ ./run_integration_tests.sh

This shell script call for shell scripts in sub-directories, i.e. the shell script for the integration tests of the microservices and the shell script for the microservices.

### 2.3. Running the demonstration

To run the demonstration, execute the following command:

	$ run_scenario_w_mapviewer.sh

In short, the demonstration shell script:

1. If necessary, cleans, stops and removes Glassfish Docker container.
2. If necessary, builds the Docker container for Glassfish,
3. Starts the Docker container for Glassfish, starts the Glassfish and Derby servers, and deploy the Tour Management EJB Bean—i.e. the Tour Management microservice.
4. Executes the client application that populates the database with some Tours and some POIs for the scenario the demonstration.
5. Starts the Docker container for RabbitMQ (the RabbitMQ server/broker is started).
6. Starts the Lobby Room microservice.
7. Starts the Visit Emulation microservice.
8. Starts the (two) tourist applications.
When completely implemented, one of the tourist application displays a map of Paris in order to follow the visit of the (two) tourists from POI to POI.
9. Waits for the end of the demonstration, which is declared by the end-user hitting return in the console of the shell script.
10. If necessary, stops the (two) tourist applications by using the `kill` command.
11. Stops the Lobby Room microservice.
12. Stops the Visit Emulation microservice.
13. Stops the RabbitMQ Docker container.
14. Undeploys the Tour Management EJB Bean—i.e. the Tour Management microservice—, stops the Glassfish and Derby servers, and stops and removes the Glassfish Docker container.

## 3. Implementing the case study

Implementing the case study includes implementing:

- The APIs of microservices.

- The microservices.

- Some unit tests of microservices (using JUnit5).

- Some integration tests of microservices (using either JUnit5 or shell scripts).

- The client applications.

- Some integration tests of the client applications.

- The scenario of the demonstration.

At the beginning of the micro-project, some JAVA classes contain only a skeleton, and some shell scripts contain commented commands.


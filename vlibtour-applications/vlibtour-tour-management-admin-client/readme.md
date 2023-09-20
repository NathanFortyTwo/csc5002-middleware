
Vlibtour Tour Management Admin client Application
====

This module is the client application for the admin client that populates the database of tours and POIs by calling the tour management microservice.

Advise: Implement this client application after (1) implementing the tour management microservice and (2) testing the microservice with integration tests.
In other words, this client application should not be used for "testing" the tour management microservice.

Compile the Maven module by executing:

	$ mvn clean install

Launch the client application by using the provides shell script:

	$start_tour_management_admin_client.sh

This shell script is used in the demonstration shell script, i.e. `run_scenario_w_mapviewer.sh`.

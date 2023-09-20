
VlibTour Bike Station
=======

TODO: Chantal, explain what is this module.

## 1. Installing

To compile and install, execute the following command:

	$ mvn clean install

## 2. Testing

### 2.1. Integration test with the `failsafe` Maven plugin

The server is tested with an integration test, and more particularly in class `ScenarioParisStationsIT`.

NB: classes named after the pattern `*IT` are integration tests, which are executed with command `mvn failsafe:integration-test failsafe:verify`,
i.e. using the Maven plugin `failsafe`. Recall that the JUnit unit tests are executed by the Maven plugin `surefire`.

The `run_integration_tests.sh` shell script executes the Maven commands for running these integration tests.

### 2.2. Integration test with the `exec` Maven plugin

You can start a server and then some clients using two consoles.
In the first console, execute the following command:

	$ mvn exec:java@server
	...
	Hit enter to stop it...

Then, in a second console, you can execute the following command:

	$ mvn exec:java@client

To stop the server, hit return in the first console.

### 2.3. Integration test with a Web browser

The server can be tested using a Web browser.
In the console,  execute the following command:

	$ mvn exec:java@server
	...
	Hit enter to stop it...
	Then,test some of the services from a Web browser with those URLs
	http://localhost:9999/MyServer/application.wadl

## 3. Explanations on the source code

Classes `Position` and `Station` have been generated with `http://www.jsonschema2pojo.org/`
by examining an example of a JSON station

	https://api.jcdecaux.com/vls/v1/stations/7102?contract=paris&apiKey=91f170cdabb4c3227116c3e871a63e8d3ad148ee
	{
	    "number":7102,
    	"name":"07102 - SAINTE CLOTHILDE",
	    "address":"FACE 19 RUE CASIMIR PERIER - 75007 PARIS",
	    "position":{"lat":48.857829110709226,"lng":2.319149052579355},
	    "banking":true,
    	"bonus":false,
	    "status":"OPEN",
    	"contract_name":"Paris",
	    "bike_stands":42,
    	"available_bike_stands":13,
	    "available_bikes":25,
    	"last_update":1505671910000
	}

Be careful! Choose "long integer" and "Jackson 2"

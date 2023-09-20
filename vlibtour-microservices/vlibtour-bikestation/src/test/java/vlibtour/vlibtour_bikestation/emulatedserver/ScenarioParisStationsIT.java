// CHECKSTYLE:OFF
/**
This file is part of the course CSC5002.

Copyright (C) 2017-23 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Chantal Taconet
Contributor(s): Denis Conan
 */

package vlibtour.vlibtour_bikestation.emulatedserver;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json.Station;

class ScenarioParisStationsIT {
	static  String REST_URI ;

	@SuppressWarnings("unused")
	private static String fileName = "src/main/resources/paris.json";
	@SuppressWarnings("unused")
	private static Stations stations; 
	@SuppressWarnings("unused")
	private static List<Station> stationList; 

	@BeforeAll
	static void setUp() throws Exception {
		// Get Paris Stations from file 
		@SuppressWarnings("unused")
		ObjectMapper mapper = new ObjectMapper();
		

		//JSON from file to Object
                // stationList = Arrays.asList(mapper.readValue(new File(fileName), Station[].class));
		// stations=new Stations(stationList);
	}

	@AfterAll
	public static void tearDown() throws Exception {
	}

	@Test
	void testReadStations() {
            // FIXME System.out.println(stations.toString());
            // FIXME Assertions.assertEquals(1228, stationList.size());
            // FIXME Assertions.assertNotNull(stations.lookupNumber(10042));		
	}
}

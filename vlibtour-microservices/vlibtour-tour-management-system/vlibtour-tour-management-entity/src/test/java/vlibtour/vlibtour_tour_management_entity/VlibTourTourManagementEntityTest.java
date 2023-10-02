/**
This file is part of the course CSC5002.

Copyright (C) 2017-2023 Télécom SudParis

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

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_tour_management_entity;

import org.junit.jupiter.api.Test;

class VlibTourTourManagementEntityTest {

	@Test()
	public void createPOITest() throws Exception {
		POI poi = new POI("POI1", "description", 0.0, 0.0);

		assert poi.getName().equals("POI1");
		assert poi.getDescription().equals("description");
		assert poi.getLatitude() == 0.0;
		assert poi.getLongitude() == 0.0;
	}

	@Test()
	public void createTourTest() throws Exception {
		Tour tour = new Tour("Tour1", "description");

		assert tour.getName().equals("Tour1");
		assert tour.getDescription().equals("description");
		assert tour.getPOIs().isEmpty();
	}

	@Test()
	public void addPOIToTourTest() throws Exception {
		Tour tour = new Tour("Tour1", "description");
		POI poi = new POI("POI1", "description", 0.0, 0.0);

		tour.addPOI(poi);

		assert tour.getPOIs().size() == 1;
		assert tour.getPOIs().contains(poi);
	}

	@Test()
	public void addPOIToTourPreserveOrderTest() throws Exception {
		Tour tour = new Tour("Tour1", "description");
		POI poi1 = new POI("POI1", "description", 0.0, 0.0);
		POI poi2 = new POI("POI2", "description", 0.0, 0.0);

		tour.addPOI(poi1);
		tour.addPOI(poi2);

		assert tour.getPOIs().size() == 2;
		assert tour.getPOIs().get(0).equals(poi1);
		assert tour.getPOIs().get(1).equals(poi2);
	}
}

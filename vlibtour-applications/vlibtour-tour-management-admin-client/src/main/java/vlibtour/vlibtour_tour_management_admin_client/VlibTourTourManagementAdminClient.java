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
package vlibtour.vlibtour_tour_management_admin_client;

import java.util.List;
import java.util.stream.Collectors;

import javax.naming.*;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService;
import vlibtour.vlibtour_tour_management_entity.POI;
import vlibtour.vlibtour_tour_management_entity.Tour;

/**
 * This class defines the administration client of the case study vlibtour.
 * <ul>
 * <li>USAGE:
 * <ul>
 * <li>vlibtour.vlibtour_admin_client.VlibTourAdminClient populate toursAndPOIs
 * </li>
 * <li>vlibtour.vlibtour_admin_client.VlibTourAdminClient empty toursAndPOIs
 * </li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author Denis Conan
 */
public class VlibTourTourManagementAdminClient {
	private static VlibTourTourManagementService service;

	/**
	 * constructs an instance of the administration client.
	 * 
	 * @throws Exception the exception thrown by the lookup.
	 */
	public VlibTourTourManagementAdminClient() throws Exception {
		Context myContext = new InitialContext();
		service = (VlibTourTourManagementService) myContext
				.lookup("vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService");
	}

	/**
	 * the main of the administration client.
	 * 
	 * @param args the command line arguments. See documentation of this
	 * 	 class.
	 * @throws Exception the exception that can be thrown (none is treated).
	 */
	public static void main(final String[] args) throws Exception {
		new VlibTourTourManagementAdminClient();

		System.out.println("Creating tour for the Dalton");

		Tour tour = new Tour(ExampleOfAVisitWithTwoTourists.DALTON_TOUR_ID,
				"description of " + ExampleOfAVisitWithTwoTourists.DALTON_TOUR_ID);
		tour = service.createTour(tour);

		List<POI> poiList = ExampleOfAVisitWithTwoTourists.POI_POSITIONS_OF_DALTON_VISIT.stream()
				.map(position -> new POI(
						position.getName(), position.getDescription(), position.getGpsPosition().getLatitude(),
						position.getGpsPosition().getLongitude()))
				.collect(Collectors.toList());

		for (POI poi : poiList) {
			poi = service.createPoi(poi);
			service.addPOItoTour(tour.getId(), poi.getId());
		}

		// Dalton tour as been created
	}
}

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
package vlibtour.vlibtour_tour_management_bean_integration_test;

import javax.naming.*;

import vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService;
import vlibtour.vlibtour_tour_management_entity.Tour;
import vlibtour.vlibtour_tour_management_entity.POI;

public class VlibTourTourManagementBeanIntegrationTests {
	/**
	 * constructs an instance of the test class.
	 * 
	 * @throws Exception the exception thrown by the lookup.
	 */
	private VlibTourTourManagementBeanIntegrationTests() {
	}

	/**
	 * the main of the integration tests. The integration tests are written before
	 * writing
	 * the admin client.
	 * 
	 * @param args the command line arguments
	 * @throws Exception the exception that can be thrown (none is treated).
	 */
	public static void main(final String[] args) throws Exception {
		Context myContext = new InitialContext();
		VlibTourTourManagementService service = (VlibTourTourManagementService) myContext
				.lookup("vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService");

		// Create tours
		Tour tour = service.createTour("Tour incroyable", "Description");

		// Create POIs
		POI poi1 = service.createPoi("Musée Grévin", "description of Musée Grévin...", 48.871799, 2.342355);
		POI poi2 = service.createPoi("Galerie de Valois", "description of Galerie de Valois...", 48.864007, 2.337890);
		POI poi3 = service.createPoi("Pyramide du Louvre", "description of Pyramide du Louvre...", 48.860959, 2.335757);
		POI poi4 = service.createPoi("Île de la Cité", "description of Île de la Cité...", 48.855201, 2.347953);

		// Verification taht tour and pois are created
		assert service.getTour("Tour incroyable") != null;

		assert service.getPOI("Musée Grévin") != null;
		assert service.getPOI("Galerie de Valois") != null;
		assert service.getPOI("Pyramide du Louvre") != null;
		assert service.getPOI("Île de la Cité") != null;

		// Add POIs to tour
		System.out.println("Adding POIs to tour");

		service.addPOItoTour(tour.getId(), poi1.getId());
		service.addPOItoTour(tour.getId(), poi2.getId());
		service.addPOItoTour(tour.getId(), poi3.getId());
		service.addPOItoTour(tour.getId(), poi4.getId());

		// Verification that POIs are added to tour
		Tour createdTour = service.getTour("Tour incroyable");
		System.out.println("Created tour: " + createdTour);

		assert createdTour.getPOIs().size() == 4;
		assert createdTour.getPOIs().get(0).equals(poi1);
		assert createdTour.getPOIs().get(1).equals(poi2);
		assert createdTour.getPOIs().get(2).equals(poi3);
		assert createdTour.getPOIs().get(3).equals(poi4);
	}
}

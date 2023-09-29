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

import java.util.List;
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
				.lookup("VlibTourTourManagementService");
		Tour tour1 = service.createTour("Tour1", "Description");
		Tour tour2 = service.createTour("Tour2", "Description");
		Tour tour3 = service.createTour("Tour3", "Description");
		Tour tour4 = service.createTour("Tour4", "Description");
		
		POI poi1 = service.createPoi("Musée Grévin", "description of Musée Grévin...", 48.871799, 2.342355);
		POI poi2 = service.createPoi("Jardin du Palais Royal", "description of Jardin du Palais Royal...", 48.866154,
				2.338562);
		POI poi3 = service.createPoi("Galerie de Valois", "description of Galerie de Valois...", 48.864007, 2.337890);
		POI poi4 = service.createPoi("Pyramide du Louvre", "description of Pyramide du Louvre...", 48.860959, 2.335757);
		POI poi5 = service.createPoi("Île de la Cité", "description of Île de la Cité...", 48.855201, 2.347953);
		POI poi6 = service.createPoi("Port-Royal", "description of Port-Royal...", 48.839795, 2.337056);
		POI poi7 = service.createPoi("Les catacombes", "description of Les catacombes...", 48.833566, 2.332416);

		service.addPOItoTour(tour1, poi1);
		service.addPOItoTour(tour1, poi2);
		service.addPOItoTour(tour1, poi3);
		service.addPOItoTour(tour1, poi4);

		List<POI> listPOIs = tour1.getPOIs();
		assert listPOIs.size() == 4;
		assert listPOIs.get(0).equals(poi1);
		assert listPOIs.get(1).equals(poi2);
		assert listPOIs.get(2).equals(poi3);
		assert listPOIs.get(3).equals(poi4);

	}
}

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
package vlibtour.vlibtour_tourist_application.tour_management_proxy;

import javax.naming.NamingException;

import vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService;
import vlibtour.vlibtour_tour_management_entity.Tour;

/**
 * The EJB Proxy (for clients) of the tour management server.
 * 
 * @author Denis Conan
 */
public final class VLibTourTourManagementProxy {
	/**
	 * the client access to the EJB stub for calling the tour management server.
	 */
	@SuppressWarnings("unused")
	private VlibTourTourManagementService vlibtt;

	/**
	 * public constructor.
	 * 
	 * @throws NamingException in case of problem in founding the tour management
	 *                         EJB skeleton using JNDI.
	 */
	public VLibTourTourManagementProxy() throws NamingException {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	/**
	 * find the tour by its identifier. Observe that the identifier for the search
	 * is a string while the identifier in the database is an integer.
	 * 
	 * @param tourId the identifier of the tour.
	 * @return the tour, as a sequence of POIs.
	 */
	public Tour findTour(final String tourId) {
		throw new UnsupportedOperationException("Not implemented, yet");
	}
    
    // TODO perhaps other methods
}

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
package vlibtour.vlibtour_visit_emulation_api;

import vlibtour.vlibtour_common.GPSPosition;
import vlibtour.vlibtour_common.Position;

/**
 * This interface defines the API of the visit emulation server.
 * 
 * @author Denis Conan
 */
public interface VisitEmulationService {
	public static Position DEFAULT_DEPARTURE_POSITION = new Position("Elysée",
			new GPSPosition(48.870081157416955, 2.3165105054638717));
	public static final String BASE_URI_WEB_SERVER = "http://localhost:8888/VisitEmulation/";

	/**
	 * gets the current position.
	 * 
	 * @param user the user.
	 * @return the position.
	 */
	Position getCurrentPosition(String user);

	/**
	 * gets the position of the next POI from the current position of a given user.
	 * 
	 * @param user the user.
	 * @return the position.
	 */
	Position getNextPOIPosition(String user);

	/**
	 * steps in the current path of a given user.
	 * 
	 * @param user the user.
	 * @return the next position.
	 */
	Position stepInCurrentPath(String user);

	/**
	 * when at a POI, steps in the visit---i.e. computes the path to the next POI.
	 * If the current POI is the last one, i.e. the user is already at the end of
	 * the visit, the position that is returned is the position of the current POI.
	 * 
	 * @param user the user.
	 * @return the next position.
	 */
	Position stepsInVisit(String user);

	/**
	 * Initialize a tour for a given user and a list of POIs.
	 * 
	 * @param request the request.
	 */
	Position initATourForAGroup(VisitEmulationTourInitRequest request);

}

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
Contributor(s): Chantal Taconet
 */
package vlibtour.vlibtour_common;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class defines an example of a visit with two tourists.
 * <p>
 * Be careful! The positions that are created in this class must be consistent
 * with the ones created in {@code VisitEmulationServer#initTourWithPOIs}.
 * 
 * @author Denis Conan
 */
public final class ExampleOfAVisitWithTwoTourists {
	/**
	 * the name of Dalton's tour.
	 * <p>
	 * This is the name that is provided as an argument to shell script
	 * {@code start_tourist_application_w_emulated_location.sh} and the {@code main}
	 * method of class {@code VLibTourVisitTouristApplication}.
	 * <p>
	 * This name is used when configuring the visit emulation server in class
	 * {@code VisitEmulationServer}.
	 */
	public static final String DALTON_TOUR_ID = "ParisBigTour";
	/**
	 * the user identifier of the first tourist.
	 */
	public static final String USER_ID_JOE = "Joe";
	/**
	 * the user identifier of the second tourist.
	 */
	public static final String USER_ID_AVREL = "Avrel";
	/**
	 * the user identifiers for the demonstration with the Dalton's group.
	 */
	public static final Set<String> DALTON_GROUP = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(USER_ID_JOE, USER_ID_AVREL)));
	/**
	 * the name of Dalton's group.
	 * <p>
	 * This is the name that is provided as an argument to shell script
	 * {@code start_tourist_application_w_emulated_location.sh} and the {@code main}
	 * method of class {@code VLibTourVisitTouristApplication}.
	 * <p>
	 * This name is used when configuring the visit emulation server in class
	 * {@code VisitEmulationServer}.
	 */
	public static final String DALTON_GROUP_ID = "Dalton";
	/**
	 * the colour onto the map of the user identifier of the first tourist.
	 */
	public static final Color COLOR_TOURIST = Color.RED;
	/**
	 * departure position of all the users.
	 */
	public static final Position DEPARTURE_POSITION = new Position(String.valueOf(2),
			new GPSPosition(48.869301, 2.3450524));
	/**
	 * Position 4, i.e. a POI.
	 */
	public static final Position POSITION4 = new Position("Musée Grévin", new GPSPosition(48.871799, 2.342355),
			"description of Musée Grévin...");
	/**
	 * Position 11, i.e. of a POI.
	 */
	public static final Position POSITION11 = new Position("Jardin du Palais Royal",
			new GPSPosition(48.866154, 2.338562), "description of Jardin du Palais Royal...");
	/**
	 * Position 15, i.e. of a POI.
	 */
	public static final Position POSITION15 = new Position("Galerie de Valois", new GPSPosition(48.864007, 2.337890),
			"description of Galerie de Valois...");
	/**
	 * Position 19, i.e. of a POI.
	 */
	public static final Position POSITION19 = new Position("Pyramide du Louvre", new GPSPosition(48.860959, 2.335757),
			"description of Pyramide du Louvre...");
	/**
	 * Position 22, i.e. of a POI.
	 */
	public static final Position POSITION22 = new Position("Île de la Cité", new GPSPosition(48.855201, 2.347953),
			"description of Île de la Cité...");
	/**
	 * Position 40, i.e. of a POI.
	 */
	public static final Position POSITION40 = new Position("Port-Royal", new GPSPosition(48.839795, 2.337056),
			"description of Port-Royal...");
	/**
	 * Position 47, i.e. of a POI.
	 */
	public static final Position POSITION47 = new Position("Les catacombes", new GPSPosition(48.833566, 2.332416),
			"description of Les catacombes...");
	/**
	 * the list of positions of some POIs that are used to populate the vlibtour
	 * tour management server.
	 * <p>
	 * Be careful! This collection is in line with the list of POIs created in
	 * {@code VlibTourTourManagementAdminClient#populateToursAndPOIs}.
	 */
	public static final List<Position> POSITIONS_OF_SOME_POIS = Collections.unmodifiableList(
			Arrays.asList(POSITION4, POSITION11, POSITION15, POSITION19, POSITION22, POSITION40, POSITION47));
	/**
	 * the list of positions of the POIs for the example visit.
	 */
	public static final List<Position> POI_POSITIONS_OF_DALTON_VISIT = Collections
			.unmodifiableList(Arrays.asList(POSITION4, POSITION19, POSITION47));
	/**
	 * the base URI of the Grizzly HTTP server will listen on.
	 */
	public static final String BASE_URI_WEB_SERVER = "http://localhost:8888/VisitEmulation/";

	/**
	 * private constructor of the utility class.
	 */
	private ExampleOfAVisitWithTwoTourists() {
	}
}

// CHECKSTYLE:OFF
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

Initial developer(s): Chantal Taconet
Contributor(s): Denis Conan
 */
package vlibtour.vlibtour_scenario.map_viewer;

import static vlibtour.vlibtour_common.Log.VLIBTOUR;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.GPSPosition;
import vlibtour.vlibtour_common.Position;

/**
 * This class contains a demonstration of the utility methods for displaying
 * POIs and tourists on a Open Street Map.
 * 
 * @author Chantal Taconet
 * @author Denis Conan
 */
public final class MapDemo {

	/**
	 * private constructor to avoid instantiation.
	 */
	private MapDemo() {
	}

	/**
	 * the main of the example.
	 * 
	 * @param args there is no command line arguments.
	 * @throws Exception pb in using OSM cache...
	 */
	public static void main(final String[] args) throws Exception {
		// create a map (JMapViewer) centered between "Musée Grévin" and "Catacombes"
		// with a 14 zoom level
		BasicMap map = MapHelper.createMapWithCenterAndZoomLevel(48.851412, 2.343166, 14);
		Font font = new Font("name", Font.BOLD, 20);
		// Add markers for the POIs in the tour
		Position pos = ExampleOfAVisitWithTwoTourists.POSITION4;
		MapHelper.addMarkerDotOnMap(map, pos.getGpsPosition().getLatitude(), pos.getGpsPosition().getLongitude(),
				Color.BLACK, font, pos.getName());
		pos = ExampleOfAVisitWithTwoTourists.POSITION19;
		MapHelper.addMarkerDotOnMap(map, pos.getGpsPosition().getLatitude(), pos.getGpsPosition().getLongitude(),
				Color.BLACK, font, pos.getName());
		pos = ExampleOfAVisitWithTwoTourists.POSITION47;
		MapHelper.addMarkerDotOnMap(map, pos.getGpsPosition().getLatitude(), pos.getGpsPosition().getLongitude(),
				Color.BLACK, font, pos.getName());
		// Set the visit (only the last POI on path 47=catacombes)
		List<Position> visit = new ArrayList<>();
		visit.add(ExampleOfAVisitWithTwoTourists.POSITION47);
		// First paint with the initial position of the users
		MapMarkerDot joeDot = MapHelper.addTouristOnMap(map, Color.RED, font, "Joe",
				new Position(String.valueOf(2), new GPSPosition(48.869301, 2.3450524)));
		MapMarkerDot avrelDot = MapHelper.addTouristOnMap(map, Color.GREEN, font, "Avrel",
				new Position(String.valueOf(2), new GPSPosition(48.869301, 2.3450524)));
		MapMarkerDot williamDot = MapHelper.addTouristOnMap(map, Color.YELLOW, font, "William",
				new Position(String.valueOf(2), new GPSPosition(48.869301, 2.3450524)));
		// wait for painting the map, not necessary with the cache
		Thread.sleep(10000);
		// Update users position
		MapHelper.moveTouristOnMap(joeDot,
				new Position(String.valueOf(1), new GPSPosition(48.869301, 2.3450524), "Musée Pooja Namo"));
		MapHelper.moveTouristOnMap(avrelDot, new Position(String.valueOf(2), new GPSPosition(48.8528621, 2.3209537),
				"Galeries Lafayette Haussmann"));
		MapHelper.moveTouristOnMap(williamDot,
				new Position(String.valueOf(3), new GPSPosition(48.8634926, 2.3218978), "Place Saint Augustin"));
		// Display the map
		map.map().repaint();
		// wait for the end of repainting
		Thread.sleep(3000);
		// Update users position
		MapHelper.moveTouristOnMap(joeDot, ExampleOfAVisitWithTwoTourists.POSITION4);
		MapHelper.moveTouristOnMap(avrelDot,
				new Position(String.valueOf(5), new GPSPosition(48.869301, 2.3450524), "Bonne Nouvelle"));
		MapHelper.moveTouristOnMap(williamDot,
				new Position(String.valueOf(7), new GPSPosition(48.8674807, 2.33698), "La Bourse"));
		// Display the map
		map.map().repaint();
		// wait for the end of repainting
		Thread.sleep(3000);
		VLIBTOUR.info("{}", () -> "\nend!\n");
		System.exit(0);
	}
}

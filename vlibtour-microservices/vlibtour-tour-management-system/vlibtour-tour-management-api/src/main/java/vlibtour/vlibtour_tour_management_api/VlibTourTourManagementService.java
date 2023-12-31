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
package vlibtour.vlibtour_tour_management_api;

import java.util.List;

import jakarta.ejb.Remote;
import vlibtour.vlibtour_tour_management_entity.POI;
import vlibtour.vlibtour_tour_management_entity.Tour;
import vlibtour.vlibtour_tour_management_entity.VlibTourTourManagementException;

/**
 * This interface defines the operation for managing POIs and Tours.
 * 
 * @author Denis Conan
 */
@Remote
public interface VlibTourTourManagementService {

    public Tour createTour(Tour tour) throws VlibTourTourManagementException;

    public POI createPoi(POI poi) throws VlibTourTourManagementException;

    public void addPOItoTour(Long tourId, Long poiId) throws VlibTourTourManagementException;

    public Tour getTour(String name);

    public POI getPOI(String POIName);

    public List<Tour> getListTours();

}

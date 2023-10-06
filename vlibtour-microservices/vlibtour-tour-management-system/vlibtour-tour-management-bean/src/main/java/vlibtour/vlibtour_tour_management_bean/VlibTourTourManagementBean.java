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
package vlibtour.vlibtour_tour_management_bean;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import vlibtour.vlibtour_tour_management_api.VlibTourTourManagementService;
import vlibtour.vlibtour_tour_management_entity.POI;
import vlibtour.vlibtour_tour_management_entity.Tour;
import vlibtour.vlibtour_tour_management_entity.VlibTourTourManagementException;

/**
 * This class defines the EJB Bean of the VLibTour tour management.
 * 
 * @author Denis Conan
 */
@Stateless()
public class VlibTourTourManagementBean implements VlibTourTourManagementService {
    // connect to the jdbc database
    @PersistenceContext()
    private EntityManager em;

    public Tour createTour(Tour tour) throws VlibTourTourManagementException {
        try {
            em.persist(tour);
            return tour;
        } catch (PersistenceException e) {
            System.out.println(e);
            throw new VlibTourTourManagementException("Tour already exists");
        }
    }

    public POI createPoi(POI poi) throws VlibTourTourManagementException {
        try {
            em.persist(poi);
            return poi;
        } catch (PersistenceException e) {
            System.out.println(e);
            throw new VlibTourTourManagementException("POI already exists");
        }
    }

    public void addPOItoTour(Long tourId, Long poiId) throws VlibTourTourManagementException {
        Tour tour = em.find(Tour.class, tourId);
        POI poi = em.find(POI.class, poiId);

        if (tour == null) {
            throw new VlibTourTourManagementException("Tour does not exist");
        }
        if (poi == null) {
            throw new VlibTourTourManagementException("POI does not exist");
        }

        try {
            if (tour.getPOIs().contains(poi)) {
                throw new VlibTourTourManagementException("POI already exists in the tour");
            }
            tour.addPOI(poi);
        } catch (PersistenceException e) {
            throw new VlibTourTourManagementException("POI already exists in the tour");
        }
    }

    public Tour getTour(String name) {
        try {
            return em.createNamedQuery(Tour.FIND_BY_NAME, Tour.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public POI getPOI(String name) {
        try {
            return em.createNamedQuery(POI.FIND_BY_NAME, POI.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Tour> getListTours() {
        return em.createNamedQuery(Tour.FIND_ALL, Tour.class).getResultList();
    }

}
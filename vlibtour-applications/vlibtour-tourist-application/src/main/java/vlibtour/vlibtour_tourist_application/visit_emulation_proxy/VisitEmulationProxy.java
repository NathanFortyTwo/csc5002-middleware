/**
This file is part of the course CSC5002.

Copyright (C) 2019-2023 Télécom SudParis

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

Initial developer(s): Chantal Taconet and Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_tourist_application.visit_emulation_proxy;

import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import vlibtour.vlibtour_common.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_common.Position;
import vlibtour.vlibtour_visit_emulation_api.VisitEmulationService;

/**
 * The REST Proxy (for clients) of the VLibTour Visit Emulation Server.
 */
public final class VisitEmulationProxy implements VisitEmulationService {
	/**
	 * constructs the REST proxy.
	 */
	private WebTarget service;
	private Client client;

	public VisitEmulationProxy() {
		// init webtarget

		this.client = ClientBuilder.newClient();
		URI uri = UriBuilder.fromUri(ExampleOfAVisitWithTwoTourists.BASE_URI_WEB_SERVER).build();
		this.service = client.target(uri);
		System.out.println("VisitEmulationProxy: " + service.getUri());
	}

	/**
	 * gets the position of the next POI to visit when arrived at the end of the
	 * current path, that is the next POI in the visit. This position is the last
	 * one of the current path.
	 * 
	 * @param user the identifier of the user.
	 * @return the position of the current POI.
	 */
	public synchronized Position getNextPOIPosition(final String user) {
		Position position = service
				.path("visitemulation/getNextPOIPosition/" + user).request()
				.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
		return position;
	}

	/**
	 * gets the current position of a user.
	 * 
	 * @param user the identifier of the user.
	 * @return the current position of the user.
	 */
	public synchronized Position getCurrentPosition(final String user) {
		Position position = service
				.path("visitemulation/getCurrentPosition/" + user).request()
				.accept(MediaType.APPLICATION_JSON).get().readEntity(Position.class);
		return position;
	}

	/**
	 * steps to the next position in the current path. It returns the new position
	 * of the user, or the same if the end of the path is already reached.
	 * 
	 * @param user the identifier of the user.
	 * @return the new position of the user, or the same if the end of the path is
	 *         already reached.
	 */
	public synchronized Position stepInCurrentPath(final String user) {
		Position position = service
				.path("visitemulation/stepInCurrentPath/" + user).request()
				.accept(MediaType.APPLICATION_JSON).post(null).readEntity(Position.class);
		return position;
	}

	/**
	 * when at a POI, steps in the visit---i.e. ask the visit emulation server for
	 * the position to the next POI. If the current POI is the last one, i.e. the
	 * user is already at the end of the visit, the position that is returned is the
	 * position of the current POI.
	 * 
	 * @param user the identifier of the user.
	 * @return the next position.
	 */
	public synchronized Position stepsInVisit(final String user) {
		Position position = service
				.path("visitemulation/stepsInVisit/" + user).request()
				.accept(MediaType.APPLICATION_JSON).post(null).readEntity(Position.class);
		return position;
	}

	/**
	 * closes the client.
	 */
	public void close() {
		client.close();
	}
}

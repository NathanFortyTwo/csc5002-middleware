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
package vlibtour.vlibtour_tourist_application.lobby_room_proxy;

/**
 * This record is the container of the information for a group communication
 * system.
 *
 * TODO: decide whether you want to use it, and if so, which data it should contain.
 * 
 * @param tourId   the name of the tour.
 * @param gcsId    the identifier of the group communication system to use for
 *                 communicating in the group.
 * @param userId   the user identifier that creates and joins the group
 *                 communication system.
 * @param urlToGCS the URL for the connection to the group communication system.
 * 
 * @author Denis Conan
 */
public record GCSInfo(String tourId, String gcsId, String userId, String urlToGCS) {
	/**
	 * check arguments before constructing an object.
	 */
	public GCSInfo {
		if (tourId == null || tourId.isBlank()) {
			throw new IllegalArgumentException("tourId cannot be null or blank");
		}
		if (gcsId == null || gcsId.isBlank()) {
			throw new IllegalArgumentException("groupId cannot be null or blank");
		}
		if (userId == null || userId.isBlank()) {
			throw new IllegalArgumentException("userId cannot be null or blank");
		}
		if (urlToGCS == null || urlToGCS.isEmpty()) {
			throw new IllegalArgumentException("the urd cannot be null or empty");
		}
	}
}

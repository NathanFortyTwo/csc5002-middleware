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
package vlibtour.vlibtour_lobby_room_server;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;

/**
 * This class implements the VLibTour lobby room service. This is the entry
 * point of the system for the clients when they want to start a tour.
 * <p>
 * When launched in its own process via a {@code java} command in shell script,
 * there is no call to {@link #close()}: the process is killed in the shell
 * script that starts this process. But, the class is a
 * {@link java.lang.Runnable} so that the lobby room server can be integrated in
 * another process.
 * 
 * @author Denis Conan
 */
public class VLibTourLobbyServer implements Runnable, VLibTourLobbyService {

	/**
	 * creates the lobby room server and the corresponding JSON server object.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 */
	public VLibTourLobbyServer() throws InAMQPPartException {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	@Override
	public String createGCSAndJoinIt(final String gcsId, final String userId) {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	@Override
	public String joinAGroup(final String groupId, final String userId) {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	/**
	 * creates the JSON RPC server and enters into the main loop of the JSON RPC
	 * server. The exit to the main loop is done when calling
	 * {@code stopLobbyRoom()} on the administration server. At the end of this
	 * method, the connectivity is closed.
	 */
	@Override
	public void run() {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	/**
	 * calls for the termination of the main loop if not already done and then
	 * closes the connection and the channel of this server.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 */
	public void close() throws InAMQPPartException {
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	/**
	 * starts the lobby server.
	 * <p>
	 * When launched in its own process via a {@code java} command in shell script,
	 * there is no call to {@link #close()}: the process is killed in the shell
	 * script that starts this process.
	 * 
	 * @param args command line arguments
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		System.out.println("\n\n>>>>>>> TODO implement this method\n\n");
	}
}

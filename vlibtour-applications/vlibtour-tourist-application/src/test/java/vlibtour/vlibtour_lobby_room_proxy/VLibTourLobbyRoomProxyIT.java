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

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_lobby_room_proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_tourist_application.lobby_room_proxy.VLibTourLobbyRoomProxy;

class VLibTourLobbyRoomProxyIT {

	@SuppressWarnings("unused")
	private static Client c;
	private static String uri;
	private static VLibTourLobbyRoomProxy lobbyRoomProxy;
	private static String gcsId = "78";
	private static String userId = "15";

	@BeforeAll
	static void setUp()
			throws IOException, InterruptedException, URISyntaxException, JsonRpcException, TimeoutException {
		// this is an integration test that assumes that
		// (1) the rabbitmq broker is already running with exchange "lobby-room"
		// (2) the lobby-room server is already started, connected to the same exchange,
		// and ready to receive messages
		c = new Client(new ClientParameters().url("http://127.0.0.1:15672/api/").username("guest").password("guest"));
		Thread.sleep(1000);

		lobbyRoomProxy = new VLibTourLobbyRoomProxy();
		uri = lobbyRoomProxy.service.createGCSAndJoinIt(gcsId, userId);
		Thread.sleep(1000);
	}

	@Test
	void test() throws IOException, TimeoutException, InterruptedException, ExecutionException, InAMQPPartException,
			JsonRpcException {
		Assertions.assertNotNull(uri);

		String[] data = uri.split("/");
		String[] subdata = data[2].split(":");
		System.out.println("uri is " + uri);

		Assertions.assertEquals(data.length, 4);
		Assertions.assertEquals(subdata.length, 3);

		Assertions.assertEquals(data[0], "amqp:");
		Assertions.assertEquals(subdata[0], userId);
		Assertions.assertEquals(data[3], gcsId);

		try {
			Integer.parseInt(data[2].split(":")[2]);
		} catch (NumberFormatException e) {
			Assertions.fail();
		}

		Process process = new ProcessBuilder("docker", "exec", "rabbitmq",
				"rabbitmqctl", "list_vhosts")
				.start();

		InputStream inputStream = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		boolean found = false;
		while ((line = reader.readLine()) != null) {
			if (line.contains(gcsId)) {
				found = true;
			}
		}
		Assertions.assertTrue(found);
		process.waitFor();
	}

	@AfterAll
	static void tearDown() throws IOException, TimeoutException {
		// since the rabbitmq container is not started in method @BeforeClass,
		// it is not stopped and removed here
		lobbyRoomProxy.close();
	}
}

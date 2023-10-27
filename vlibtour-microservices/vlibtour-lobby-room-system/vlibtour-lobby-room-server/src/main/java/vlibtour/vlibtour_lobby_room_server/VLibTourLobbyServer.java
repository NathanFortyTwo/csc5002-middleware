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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

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
	private static String HOST = "localhost";
	private Connection connection;
	private Channel channel;
	/**
	* the RabbitMQ JSON RPC server.
	*/
	private JsonRpcServer rpcServer;

	/**
	 * creates the lobby room server and the corresponding JSON server object.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 * @throws TimeoutException
	 * @throws IOException
	 */
	/**
	 * Constructor VLibTourLobbyServer() opens the connection to the RabbitMQ broker, 
	 * creates the exchange for receiving RPC calls, 
	 * creates the queue, and binds the queue to the exchange. 
	 * Then, the constructor creates the stub for receiving the RPC calls, 
	 * i.e. an instance of JsonRpcServer that represents a VLibTourLobbyService 
	 * and that delegates the calls to an instance of class VLibTourLobbyServer. 
	 * Since the stub is created in the constructor of our class that implements the interface, 
	 * we provide the instance this. If we wanted to better manage concurrency 
	 * (because giving the reference this in the constructor is dangerous), 
	 * we would add another method for the creation of the stub object (instance of JsonRpcServer).
	 */
	public VLibTourLobbyServer() throws InAMQPPartException, IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		while (connection == null) {
			try {
				connection = factory.newConnection();
			} catch (IOException e) {
				System.out.println(" [x] Cannot connect to the AMQP broker");
				System.out.println(" [x] Retrying in 5 seconds");
				Thread.sleep(5000);
			}
		}
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, BINDING_KEY);

		rpcServer = new JsonRpcServer(channel, queueName, VLibTourLobbyService.class, this);
	}

	private void createGCS(final String gcsId) throws IOException, InterruptedException {
		// Create exchange
		new ProcessBuilder("docker", "exec", "rabbitmq", "rabbitmqctl", "add_vhost", gcsId).inheritIO().start()
				.waitFor();
	}

	@Override
	public String createGCSAndJoinIt(final String gcsId, final String userId) {
		try {
			createGCS(gcsId);
			return joinAGroup(gcsId, userId);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String generatePassword() {
		CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
		CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
		CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
		CharacterRule[] rules = new CharacterRule[] { lowerCaseRule, upperCaseRule, digitRule };
		String password = new PasswordGenerator().generatePassword(8, rules);
		return password;
	}

	@Override
	public String joinAGroup(final String groupId, final String userId) {
		String password = generatePassword();
		try {
			new ProcessBuilder("docker", "exec", "rabbitmq", "rabbitmqctl", "add_user", userId, password)
					.inheritIO().start().waitFor();
			new ProcessBuilder("docker", "exec", "rabbitmq", "rabbitmqctl", "set_permissions", "-p", groupId, userId,
					".*", ".*", ".*").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		return "amqp://" + userId + ":" + password + "@" + HOST + ":" + connection.getPort() + "/"
				+ groupId;
	}

	/**
	 * creates the JSON RPC server and enters into the main loop of the JSON RPC
	 * server. The exit to the main loop is done when calling
	 * {@code stopLobbyRoom()} on the administration server. At the end of this
	 * method, the connectivity is closed.
	 */
	@Override
	public void run() {
		try {
			rpcServer.mainloop();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				close();
			} catch (InAMQPPartException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * calls for the termination of the main loop if not already done and then
	 * closes the connection and the channel of this server.
	 * 
	 * @throws InAMQPPartException the exception thrown in case of a problem in the
	 *                             AMQP part.
	 */
	public void close() throws InAMQPPartException {
		try {
			if (rpcServer != null) {
				rpcServer.terminateMainloop();
			}
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (IOException | TimeoutException e) {
			throw new InAMQPPartException("Cannot close the connection to the AMQP broker");
		}

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
		System.out.println(" [x] Starting the lobby server");
		VLibTourLobbyServer lobbyServer = new VLibTourLobbyServer();
		System.out.println(" [x] Awaiting RPC requests");
		lobbyServer.run();
	}
}

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
package vlibtour.vlibtour_group_communication_proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.ClientParameters;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_tourist_application.group_communication_proxy.VLibTourGroupCommunicationSystemProxy;

class VLibTourGroupCommunicationProxyIT {

	@SuppressWarnings("unused")
	private static Client c;

	@BeforeAll
	static void setUp() throws MalformedURLException, URISyntaxException, InterruptedException, IOException {
		// this is an integration test that assumes that
		// (1) the rabbitmq broker is already running with exchange "lobby-room"
		// (2) the lobby-room server is already started, connected to the same exchange,
		// and ready to receive messages
		c = new Client(new ClientParameters().url("http://127.0.0.1:15672/api/").username("guest").password("guest"));
		Thread.sleep(1000);
	}

	int nbMessagesReceived = 0;

	@Test
	void test() throws IOException, TimeoutException, InterruptedException, ExecutionException, InAMQPPartException {
		String topic = "0"; // groupId
		String consumerPartialRoutingKey = "0"; // userId
		String producerPartialRoutingKey = "1"; // userId
		VLibTourGroupCommunicationSystemProxy receiverProxy = new VLibTourGroupCommunicationSystemProxy(topic,
				consumerPartialRoutingKey);
		Assertions.assertNotNull(c.getExchanges().stream().filter(q -> q.getName().equals(topic)));

		DefaultConsumer consumer = new DefaultConsumer(receiverProxy.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope,
					com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				message = " [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'";
				System.out.println(message);
				nbMessagesReceived++;
			};
		};
		VLibTourGroupCommunicationSystemProxy producerProxy = new VLibTourGroupCommunicationSystemProxy(topic,
				producerPartialRoutingKey);

		receiverProxy.setConsumer(consumer);

		producerProxy.publish("Hello world !", "all.position");
		producerProxy.publish("Miam miam ", "all.position");
		
		Assertions.assertEquals(0, nbMessagesReceived);
		receiverProxy.startConsuming();
		Thread.sleep(500);
		Assertions.assertEquals(2, nbMessagesReceived);
		receiverProxy.close();

	}

	@AfterAll
	static void tearDown() {
		// since the rabbitmq container is not started in method @BeforeClass,
		// it is not stopped and removed here
	}
}

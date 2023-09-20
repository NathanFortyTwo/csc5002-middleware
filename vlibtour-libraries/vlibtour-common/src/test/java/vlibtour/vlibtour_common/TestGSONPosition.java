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
Contributor(s):
 */
package vlibtour.vlibtour_common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestGSONPosition {
	Position museeGrevin;
	Position museeGrevinBis;
	Position pyramideLouvre;

	@BeforeEach
	void setUp() {
		museeGrevin = new Position("Musée Grévin", new GPSPosition(48.871799, 2.342355));
		museeGrevinBis = new Position("Musée Grévin", new GPSPosition(48.871799, 2.342355));
		pyramideLouvre = new Position("Pyramide du Louvre", new GPSPosition(48.860959, 2.335757));
	}

	@AfterEach
	void tearDown() {
		museeGrevin = null;
		museeGrevinBis = null;
		pyramideLouvre = null;
	}

	@Test
	void testDistance() {
		Assertions.assertEquals(museeGrevin, museeGrevinBis);
		Assertions.assertNotEquals(museeGrevin, pyramideLouvre);
		Assertions.assertEquals(Position.GSON.toJson(museeGrevin), Position.GSON.toJson(museeGrevinBis));
//		System.out.println(Position.GSON.toJson(museeGrevin));
		Assertions.assertNotEquals(Position.GSON.toJson(museeGrevin), Position.GSON.toJson(pyramideLouvre));
		Assertions.assertEquals(museeGrevin, Position.GSON.fromJson(Position.GSON.toJson(museeGrevin), Position.class));
		Assertions.assertEquals(museeGrevin,
				Position.GSON.fromJson(Position.GSON.toJson(museeGrevinBis), Position.class));
		Assertions.assertNotEquals(museeGrevin,
				Position.GSON.fromJson(Position.GSON.toJson(pyramideLouvre), Position.class));
	}
}

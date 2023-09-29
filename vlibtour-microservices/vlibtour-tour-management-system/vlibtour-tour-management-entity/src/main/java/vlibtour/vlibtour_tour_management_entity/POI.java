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
package vlibtour.vlibtour_tour_management_entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;

/**
 * The entity bean defining a point of interest (POI). A {@link Tour} is a
 * sequence of points of interest.
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Denis Conan
 */
@Entity
@NamedQueries({
		@NamedQuery(name = POI.FIND_ALL, query = "SELECT p FROM POI p"),
		@NamedQuery(name = POI.FIND_BY_ID, query = "SELECT p FROM POI p WHERE p.id = :id"),
		@NamedQuery(name = POI.FIND_BY_NAME, query = "SELECT p FROM POI p WHERE p.name = :name")
})
public class POI implements Serializable {
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String description;

	@NotNull
	private double latitude;

	@NotNull
	private double longitude;

	// Constructors, getters, and setters

	public POI() {
		// Default constructor
	}

	public POI(String name, String description, double latitude, double longitude) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		if (description == null || description.equals("")) {
			throw new IllegalArgumentException("description cannot be null or empty");
		}
		if (latitude < -90.0 || latitude > 90.0) {
			throw new IllegalArgumentException("latitude must be in [-90, 90]");
		}
		if (longitude < -180.0 || longitude > 180.0) {
			throw new IllegalArgumentException("longitude must be in [-180, 180]");
		}
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getter and Setter methods for all fields are omitted
	//#region
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null || description.equals("")) {
			throw new IllegalArgumentException("description cannot be null or empty");
		}
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	//#endregion

	@Override
	public String toString() {
		return "POI{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}

	// Named queries
	public static final String FIND_ALL = "POI.findAll";
	public static final String FIND_BY_ID = "POI.findById";
	public static final String FIND_BY_NAME = "POI.findByName";
}

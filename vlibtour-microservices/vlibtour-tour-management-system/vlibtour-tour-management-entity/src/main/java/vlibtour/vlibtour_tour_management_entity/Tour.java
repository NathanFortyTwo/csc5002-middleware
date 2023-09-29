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
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;

/**
 * The entity bean defining a tour in the VLibTour case study. A tour is a
 * sequence of points of interest ({@link POI}).
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Denis Conan
 */
@Entity
@NamedQueries({
		@NamedQuery(name = Tour.FIND_ALL, query = "SELECT t FROM Tour t"),
		@NamedQuery(name = Tour.FIND_BY_ID, query = "SELECT t FROM Tour t WHERE t.id = :id"),
		@NamedQuery(name = Tour.FIND_BY_NAME, query = "SELECT t FROM Tour t WHERE t.name = :name")
})
public class Tour implements Serializable {
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

	@ManyToMany
	@JoinTable(name = "tour_poi", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name = "poi_id"))
	@OrderColumn(name = "poi_order")
	private List<POI> POIs;

	// Constructors, getters, and setters

	public Tour() {
		POIs = new ArrayList<>();
	}

	public Tour(String name, String description) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("description cannot be null or empty");
		}
		this.name = name;
		this.description = description;
		POIs = new ArrayList<>();
	}

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
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null || description.isEmpty()) {
			throw new IllegalArgumentException("description cannot be null or empty");
		}
		this.description = description;
	}

	public List<POI> getPOIs() {
		return POIs;
	}

	public void setPOIs(List<POI> POIs) {
		this.POIs = POIs;
	}
	//#endregion

	@Override
	public String toString() {
		return "Tour{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", POIs=" + POIs +
				'}';
	}

	// Named queries
	public static final String FIND_ALL = "Tour.findAll";
	public static final String FIND_BY_ID = "Tour.findById";
	public static final String FIND_BY_NAME = "Tour.findByName";
}

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
package vlibtour.vlibtour_visit_emulation_server;

import java.util.List;
import java.util.Map;

import vlibtour.vlibtour_common.Position;

/**
 * This interface defines the API of the visit emulation server.
 * 
 * @author Denis Conan
 */
public class VisitEmulationTourInfo {
    private GraphOfPositionsForEmulation graphOfPositionsForEmulation;
    private Map<String, Integer> currentIndicesInVisits;
    private Map<String, List<Position>> visits;

    public VisitEmulationTourInfo(
            GraphOfPositionsForEmulation graphOfPositionsForEmulation,
            Map<String, Integer> currentIndicesInVisits,
            Map<String, List<Position>> visits) {
        if (graphOfPositionsForEmulation == null) {
            throw new IllegalArgumentException("graphOfPositionsForEmulation cannot be null");
        }
        if (currentIndicesInVisits == null) {
            throw new IllegalArgumentException("currentIndicesInVisits cannot be null");
        }
        if (visits == null) {
            throw new IllegalArgumentException("visits cannot be null");
        }

        this.graphOfPositionsForEmulation = graphOfPositionsForEmulation;
        this.currentIndicesInVisits = currentIndicesInVisits;
        this.visits = visits;
    }

    public GraphOfPositionsForEmulation getGraphOfPositionsForEmulation() {
        return graphOfPositionsForEmulation;
    }

    public void setGraphOfPositionsForEmulation(GraphOfPositionsForEmulation graphOfPositionsForEmulation) {
        if (graphOfPositionsForEmulation == null) {
            throw new IllegalArgumentException("graphOfPositionsForEmulation cannot be null");
        }
        this.graphOfPositionsForEmulation = graphOfPositionsForEmulation;
    }

    public Map<String, Integer> getCurrentIndicesInVisits() {
        return currentIndicesInVisits;
    }

    public void setCurrentIndicesInVisits(Map<String, Integer> currentIndicesInVisits) {
        if (currentIndicesInVisits == null) {
            throw new IllegalArgumentException("currentIndicesInVisits cannot be null");
        }
        this.currentIndicesInVisits = currentIndicesInVisits;
    }

    public Map<String, List<Position>> getVisits() {
        return visits;
    }

    public void setVisits(Map<String, List<Position>> visits) {
        if (visits == null) {
            throw new IllegalArgumentException("visits cannot be null");
        }
        this.visits = visits;
    }
}

package vlibtour.vlibtour_visit_emulation_api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import vlibtour.vlibtour_common.Position;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "tourId", "userId", "POIs" })
public class VisitEmulationTourInitRequest implements Serializable {
    @JsonProperty("tourId")
    private String tourId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("POIs")
    private List<Position> POIs;

    public VisitEmulationTourInitRequest(String tourId, String userId, List<Position> POIs) {
        this.tourId = tourId;
        this.userId = userId;
        this.POIs = POIs;
    }

    // Getters and setters for the fields

    public VisitEmulationTourInitRequest() {
    }

    @JsonProperty("tourId")
    public String getTourId() {
        return tourId;
    }

    @JsonProperty("tourId")
    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("POIs")
    public List<Position> getPOIs() {
        return POIs;
    }

    @JsonProperty("POIs")
    public void setPOIs(List<Position> POIs) {
        this.POIs = POIs;
    }
}

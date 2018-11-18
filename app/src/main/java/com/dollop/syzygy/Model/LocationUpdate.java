package com.dollop.syzygy.Model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationUpdate implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;

    public String getDest_lattitude() {
        return dest_lattitude;
    }

    public void setDest_lattitude(String dest_lattitude) {
        this.dest_lattitude = dest_lattitude;
    }

    @SerializedName("dest_lattitude")
    @Expose
    private String dest_lattitude;


    public String getDest_longitude() {
        return dest_longitude;
    }

    public void setDest_longitude(String dest_longitude) {
        this.dest_longitude = dest_longitude;
    }

    @SerializedName("dest_longitude")
    @Expose
    private String dest_longitude;

    @SerializedName("caregiver_id")
    @Expose
    private String caregiverId;
    @SerializedName("hire_caregiver_id")
    @Expose
    private String hireCaregiverId;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("current_position")
    @Expose
    private String currentPosition;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getHireCaregiverId() {
        return hireCaregiverId;
    }

    public void setHireCaregiverId(String hireCaregiverId) {
        this.hireCaregiverId = hireCaregiverId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
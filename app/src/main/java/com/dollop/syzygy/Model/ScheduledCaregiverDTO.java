package com.dollop.syzygy.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduledCaregiverDTO {

    @SerializedName("schedule_notification_id")
    @Expose
    private String scheduleNotificationId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("caregiver_specialization_id")
    @Expose
    private Object caregiverSpecializationId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_created_date")
    @Expose
    private String serviceCreatedDate;

    public String getScheduleNotificationId() {
        return scheduleNotificationId;
    }

    public void setScheduleNotificationId(String scheduleNotificationId) {
        this.scheduleNotificationId = scheduleNotificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getCaregiverSpecializationId() {
        return caregiverSpecializationId;
    }

    public void setCaregiverSpecializationId(Object caregiverSpecializationId) {
        this.caregiverSpecializationId = caregiverSpecializationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCreatedDate() {
        return serviceCreatedDate;
    }

    public void setServiceCreatedDate(String serviceCreatedDate) {
        this.serviceCreatedDate = serviceCreatedDate;
    }

}
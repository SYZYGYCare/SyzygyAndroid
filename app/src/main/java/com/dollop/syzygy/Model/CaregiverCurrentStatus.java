package com.dollop.syzygy.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaregiverCurrentStatus {

    @SerializedName("hire_caregiver_id")
    @Expose
    private String hireCaregiverId;

    public String getBookingstatus() {
        return bookingstatus;
    }

    public void setBookingstatus(String bookingstatus) {
        this.bookingstatus = bookingstatus;
    }

    @SerializedName("bookingstatus")
    @Expose
    private String bookingstatus;


    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    @SerializedName("lattitude")
    @Expose
    private String lattitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("longitude")
    @Expose
    private String longitude;


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
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("transaction_id")
    @Expose
    private Object transactionId;
    @SerializedName("is_accepted")
    @Expose
    private String isAccepted;
    @SerializedName("user_schedule_id")
    @Expose
    private Object userScheduleId;
    @SerializedName("hire_hour")
    @Expose
    private Object hireHour;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("hour_remaining")
    @Expose
    private Object hourRemaining;
    @SerializedName("is_registration_completed")
    @Expose
    private String isRegistrationCompleted;
    @SerializedName("is_hire_senior")
    @Expose
    private Object isHireSenior;
    @SerializedName("is_current")
    @Expose
    private String isCurrent;
    @SerializedName("is_now")
    @Expose
    private String isNow;
    @SerializedName("is_completed")
    @Expose
    private String isCompleted;
    @SerializedName("hire_reason")
    @Expose
    private Object hireReason;
    @SerializedName("hire_invite_date")
    @Expose
    private Object hireInviteDate;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("hire_date")
    @Expose
    private String hireDate;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
/*    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitud")
    @Expose
    private String longitud;*/
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profile_pic")
    @Expose
    private Object profilePic;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("social_security_no")
    @Expose
    private Object socialSecurityNo;
    @SerializedName("registration_date")
    @Expose
    private String registrationDate;

    public String getHireCaregiverId() {
        return hireCaregiverId;
    }

    public void setHireCaregiverId(String hireCaregiverId) {
        this.hireCaregiverId = hireCaregiverId;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Object getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Object transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Object getUserScheduleId() {
        return userScheduleId;
    }

    public void setUserScheduleId(Object userScheduleId) {
        this.userScheduleId = userScheduleId;
    }

    public Object getHireHour() {
        return hireHour;
    }

    public void setHireHour(Object hireHour) {
        this.hireHour = hireHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getHourRemaining() {
        return hourRemaining;
    }

    public void setHourRemaining(Object hourRemaining) {
        this.hourRemaining = hourRemaining;
    }

    public String getIsRegistrationCompleted() {
        return isRegistrationCompleted;
    }

    public void setIsRegistrationCompleted(String isRegistrationCompleted) {
        this.isRegistrationCompleted = isRegistrationCompleted;
    }

    public Object getIsHireSenior() {
        return isHireSenior;
    }

    public void setIsHireSenior(Object isHireSenior) {
        this.isHireSenior = isHireSenior;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getIsNow() {
        return isNow;
    }

    public void setIsNow(String isNow) {
        this.isNow = isNow;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Object getHireReason() {
        return hireReason;
    }

    public void setHireReason(Object hireReason) {
        this.hireReason = hireReason;
    }

    public Object getHireInviteDate() {
        return hireInviteDate;
    }

    public void setHireInviteDate(Object hireInviteDate) {
        this.hireInviteDate = hireInviteDate;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

/*    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getSocialSecurityNo() {
        return socialSecurityNo;
    }

    public void setSocialSecurityNo(Object socialSecurityNo) {
        this.socialSecurityNo = socialSecurityNo;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

}
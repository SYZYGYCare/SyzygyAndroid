package com.dollop.syzygy.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCurrentStatus {

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
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_account_activate")
    @Expose
    private String isAccountActivate;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("agency_id")
    @Expose
    private String agencyId;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("caregiver_specialization_id")
    @Expose
    private Object caregiverSpecializationId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("is_individual")
    @Expose
    private Object isIndividual;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("social_security_no")
    @Expose
    private String socialSecurityNo;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("log_time_offset")
    @Expose
    private Object logTimeOffset;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cost_per_hour")
    @Expose
    private String costPerHour;
    @SerializedName("min_charges")
    @Expose
    private String minCharges;
    @SerializedName("about_me")
    @Expose
    private Object aboutMe;
    @SerializedName("fb_link")
    @Expose
    private Object fbLink;
    @SerializedName("linkdin_link")
    @Expose
    private Object linkdinLink;
    @SerializedName("service_detail")
    @Expose
    private String serviceDetail;
    @SerializedName("video_link")
    @Expose
    private Object videoLink;
    @SerializedName("current_city")
    @Expose
    private String currentCity;
    @SerializedName("ac_holder_name")
    @Expose
    private String acHolderName;
    @SerializedName("bank_account_no")
    @Expose
    private String bankAccountNo;
    @SerializedName("bank_verified")
    @Expose
    private String bankVerified;
    @SerializedName("police_verified")
    @Expose
    private String policeVerified;
    @SerializedName("no_verified")
    @Expose
    private String noVerified;
    @SerializedName("email_verified")
    @Expose
    private String emailVerified;
    @SerializedName("profile_pic_verified")
    @Expose
    private String profilePicVerified;
    @SerializedName("social_link_verified")
    @Expose
    private String socialLinkVerified;
    @SerializedName("bank_ifsc_code")
    @Expose
    private String bankIfscCode;
    @SerializedName("admin_commision")
    @Expose
    private String adminCommision;
    @SerializedName("registration_date")
    @Expose
    private String registrationDate;
    @SerializedName("ambulance_type")
    @Expose
    private String ambulanceType;
    @SerializedName("vehical_registration_no")
    @Expose
    private String vehicalRegistrationNo;
    @SerializedName("vehical_model_no")
    @Expose
    private String vehicalModelNo;
    @SerializedName("lincens")
    @Expose
    private String lincens;
    @SerializedName("registration_card")
    @Expose
    private String registrationCard;
    @SerializedName("addressProof")
    @Expose
    private String addressProof;
    @SerializedName("identity_card")
    @Expose
    private String identityCard;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("image2")
    @Expose
    private String image2;
    @SerializedName("registration_no")
    @Expose
    private String registrationNo;
    @SerializedName("client_name")
    @Expose
    private String clientName;

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

    public String getLatitude() {
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
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsAccountActivate() {
        return isAccountActivate;
    }

    public void setIsAccountActivate(String isAccountActivate) {
        this.isAccountActivate = isAccountActivate;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getIsIndividual() {
        return isIndividual;
    }

    public void setIsIndividual(Object isIndividual) {
        this.isIndividual = isIndividual;
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

    public String getSocialSecurityNo() {
        return socialSecurityNo;
    }

    public void setSocialSecurityNo(String socialSecurityNo) {
        this.socialSecurityNo = socialSecurityNo;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Object getLogTimeOffset() {
        return logTimeOffset;
    }

    public void setLogTimeOffset(Object logTimeOffset) {
        this.logTimeOffset = logTimeOffset;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(String costPerHour) {
        this.costPerHour = costPerHour;
    }

    public String getMinCharges() {
        return minCharges;
    }

    public void setMinCharges(String minCharges) {
        this.minCharges = minCharges;
    }

    public Object getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(Object aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Object getFbLink() {
        return fbLink;
    }

    public void setFbLink(Object fbLink) {
        this.fbLink = fbLink;
    }

    public Object getLinkdinLink() {
        return linkdinLink;
    }

    public void setLinkdinLink(Object linkdinLink) {
        this.linkdinLink = linkdinLink;
    }

    public String getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(String serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

    public Object getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(Object videoLink) {
        this.videoLink = videoLink;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getAcHolderName() {
        return acHolderName;
    }

    public void setAcHolderName(String acHolderName) {
        this.acHolderName = acHolderName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankVerified() {
        return bankVerified;
    }

    public void setBankVerified(String bankVerified) {
        this.bankVerified = bankVerified;
    }

    public String getPoliceVerified() {
        return policeVerified;
    }

    public void setPoliceVerified(String policeVerified) {
        this.policeVerified = policeVerified;
    }

    public String getNoVerified() {
        return noVerified;
    }

    public void setNoVerified(String noVerified) {
        this.noVerified = noVerified;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getProfilePicVerified() {
        return profilePicVerified;
    }

    public void setProfilePicVerified(String profilePicVerified) {
        this.profilePicVerified = profilePicVerified;
    }

    public String getSocialLinkVerified() {
        return socialLinkVerified;
    }

    public void setSocialLinkVerified(String socialLinkVerified) {
        this.socialLinkVerified = socialLinkVerified;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getAdminCommision() {
        return adminCommision;
    }

    public void setAdminCommision(String adminCommision) {
        this.adminCommision = adminCommision;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAmbulanceType() {
        return ambulanceType;
    }

    public void setAmbulanceType(String ambulanceType) {
        this.ambulanceType = ambulanceType;
    }

    public String getVehicalRegistrationNo() {
        return vehicalRegistrationNo;
    }

    public void setVehicalRegistrationNo(String vehicalRegistrationNo) {
        this.vehicalRegistrationNo = vehicalRegistrationNo;
    }

    public String getVehicalModelNo() {
        return vehicalModelNo;
    }

    public void setVehicalModelNo(String vehicalModelNo) {
        this.vehicalModelNo = vehicalModelNo;
    }

    public String getLincens() {
        return lincens;
    }

    public void setLincens(String lincens) {
        this.lincens = lincens;
    }

    public String getRegistrationCard() {
        return registrationCard;
    }

    public void setRegistrationCard(String registrationCard) {
        this.registrationCard = registrationCard;
    }

    public String getAddressProof() {
        return addressProof;
    }

    public void setAddressProof(String addressProof) {
        this.addressProof = addressProof;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
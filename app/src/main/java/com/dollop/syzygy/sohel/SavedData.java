package com.dollop.syzygy.sohel;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dollop.syzygy.AppController;

public class SavedData {
    private static final String START_KM = "startKm";


    private static final String CLIENT = "client_tocken";
    private static final String Tocken = "tocken";
    private static final String TypeAgain = "typeagain";
    private static final String TYPE = "type";
    private static final String PHONE = "phone";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL_ID = "user_email_id";
    private static final String USER_ID = "user_id";
    private static final String MOBILE = "mobile";
    private static final String DRIVER_NAME = "driverName";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitide";
    private static final String NOTIFICATION_REQUEST = "notificationRequest";
    private static final String OTP_VERIFY_PHONE = "otp_verify_phone";
    private static final String NOTIFICATION_JSON = "notificationJson";
    private static final String NOTIFICATION_TIMME = "notification_time";
    private static final String CLIENT_ID = "clientId";
    private static final String HIRE_CAREGIVER_ID = "hireCareGiverId";
    private static final String CAREGIVER_ID = "CareGiver_id";
    private static final String HIRE_LATER_NOTIFICATION = "Hire_later_notification";
    private static final String CANCEL_REQUEST_FROM_CLIENT = "cancel_request_from_client";
    private static final String CLIENT_GENDER = "client_gender";
    private static final String START_TIMER = "startTimer";
    private static final String TIMER_LAST_TIME = "timer_last_time";
    private static final String SAVE_SRC_LAT_AMBULANCE = "save_ambulance_src_lat";
    private static final String SAVE_SRC_LONG_AMBULANCE = "save_ambulance_src_long";
    private static final String SAVE_DEST_LAT_AMBULANCE = "save_ambulance_dest_lat";
    private static final String SAVE_DEST_LONG_AMBULANCE = "save_ambulance_dest_long";
    private static final String SAVE_IN_WORKING = "inworking";
    private static final String CARE_GIVER_SUMMERY = "care_giver_simmry";
    private static final String START_RIDE = "riding";
    private static final String PAYMENT_STATUS = "payment_status";
    private static final String RATING_STATUS = "rating_status";
    private static final String ACCOUNTNOHOLDER = "ac_holder";
    private static final String IFCCODE = "ifccode";
    private static final String ACCOUNTNo = "accountno";
    private static final String SUMMERY = "summery";
    private static final String AmbulanceSummery = "ambusummery";
    private static final String UserPayType = "userpaytype";

    private static final String REMAINDER_FOR = "remainderFor";
    private static final String REQUIRED_CARE = "requiredCare";
    private static final String REQUIRED_CARE_TYPE = "requiredCareType";
    private static final String REMAINDER_TIME = "remainderTime";
    private static final String REMAINDER_DATE = "remainderDate";
    private static final String REMAINDER_HOUR = "remainderHour";
    private static final String ANY_REMAINDER = "anyRemainder";
    private static final String AcceptLayoutKeep= "acceptLayout";
    private static final String AcceptedFullName= "acceptfullname";
    private static final String AcceptedPhoneNo= "acceptedPhoneNo";
    private static final String AcceptedLatitude= "acceptedlatitude";
    private static final String AcceptedLongitude= "acceptedlongitude";
    private static final String AcceptedImagePath= "acceptedImage";
    private static final String AcceptedRating= "acceptedRating";
    private static final String Hire_CareGiver_id= "Hire_CareGiver_id";
    private static final String AcceptSrcLatitude= "accestsrclat";
    private static final String AcceptSrcLongitude= "accestsrclong";
    private static final String AcceptCareType= "caregiverType";
    private static final String AccepteduserGender= "AccepteduserGender";
    private static final String HiredUserType= "HiredUserType";



    static SharedPreferences prefs;

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
        }
        return prefs;
    }

    public static void clear() {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.clear();
        editor.apply();
    }

    public static String getAcceptCareType() {
        return getInstance().getString(AcceptCareType, "");
    }

    public static void saveAcceptCareType(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptCareType, remainderDate);
        editor.apply();
    }

    public static String getHiredUserType() {
        return getInstance().getString(HiredUserType, "");
    }

    public static void saveHiredUserType(String HiredUserType1) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(HiredUserType, HiredUserType1);
        editor.apply();
    }
    public static String getHire_CareGiver_id() {
        return getInstance().getString(Hire_CareGiver_id, "");
    }

    public static void savHire_CareGiver_id(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(Hire_CareGiver_id, remainderDate);
        editor.apply();
    }
    public static String getAcceptedRating() {
        return getInstance().getString(AcceptedRating, "");
    }

    public static void saveAcceptedRating(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedRating, remainderDate);
        editor.apply();
    }  public static String getAcceptedImagePath() {
        return getInstance().getString(AcceptedImagePath, "");
    }

    public static void saveAcceptedImagePath(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedImagePath, remainderDate);
        editor.apply();
    }
    public static String getAcceptedFullName() {
        return getInstance().getString(AcceptedFullName, "");
    }

    public static void saveAcceptedFullName(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedFullName, remainderDate);
        editor.apply();
    }
    public static String getAcceptedPhoneNo() {
        return getInstance().getString(AcceptedPhoneNo, "");
    }

    public static void saveAcceptedPhoneNo(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedPhoneNo, remainderDate);
        editor.apply();
    }    public static String getAccepteduserGender() {
        return getInstance().getString(AccepteduserGender, "");
    }

    public static void saveAccepteduserGender(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AccepteduserGender, remainderDate);
        editor.apply();
    }
 public static String getAcceptedLatitude() {
        return getInstance().getString(AcceptedLatitude, "");
    }

    public static void saveAcceptedLatitude(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedLatitude, remainderDate);
        editor.apply();
    }
    public static String getAcceptedLongitude() {
        return getInstance().getString(AcceptedLongitude, "");
    }

    public static void saveAcceptedLongitude(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptedLongitude, remainderDate);
        editor.apply();
    }
 public static String getAcceptedSrcLatitude() {
        return getInstance().getString(AcceptSrcLatitude, "");
    }

    public static void saveAcceptedsrcLatitude(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptSrcLatitude, remainderDate);
        editor.apply();
    }
 public static String getAcceptedSrcLongitude() {
        return getInstance().getString(AcceptSrcLongitude, "");
    }

    public static void saveAcceptedsrcLongitude(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AcceptSrcLongitude, remainderDate);
        editor.apply();
    }

    public static String getRemainderDate() {
        return getInstance().getString(REMAINDER_DATE, "");
    }

    public static void saveRemainderDate(String remainderDate) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REMAINDER_DATE, remainderDate);
        editor.apply();
    }





    public static boolean getAcceptLayout() {
        return getInstance().getBoolean(AcceptLayoutKeep, false);
    }

    public static void saveAcceptLayoutKeep(boolean startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(AcceptLayoutKeep, startTimer);
        editor.apply();
    }

    public static String getAnyRemainder() {
        return getInstance().getString(ANY_REMAINDER, "");
    }

    public static void saveAnyRemainder(String anyRemainder) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(ANY_REMAINDER, anyRemainder);
        editor.apply();
    }

    public static String getRemainderHour() {
        return getInstance().getString(REMAINDER_HOUR, "");
    }

    public static void saveRemainderHour(String remainderHour) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REMAINDER_HOUR, remainderHour);
        editor.apply();
    }

    public static String getRemainderTime() {
        return getInstance().getString(REMAINDER_TIME, "");
    }

    public static void saveRemainderTime(String remainderTime) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REMAINDER_TIME, remainderTime);
        editor.apply();
    }

    public static String getRequiredCateType() {
        return getInstance().getString(REQUIRED_CARE_TYPE, "");
    }

    public static void saveRequiredCateType(String requiredCareType) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REQUIRED_CARE_TYPE, requiredCareType);
        editor.apply();
    }

    public static String getRequiredCate() {
        return getInstance().getString(REQUIRED_CARE, "");
    }

    public static void saveRequiredCate(String requiredCare) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REQUIRED_CARE, requiredCare);
        editor.apply();
    }

    public static String getRemainderFor() {
        return getInstance().getString(REMAINDER_FOR, "");
    }

    public static void saveRemainderFor(String remainderFor) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(REMAINDER_FOR, remainderFor);
        editor.apply();
    }

    public static String getAccountnoholder() {
        return getInstance().getString(ACCOUNTNOHOLDER, "");
    }

    public static void saveAccountHolderName(String user_type) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(ACCOUNTNOHOLDER, user_type);
        editor.apply();
    }

    public static String getIfccode() {
        return getInstance().getString(IFCCODE, "");
    }

    public static void saveIFCCODE(String user_type) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(IFCCODE, user_type);
        editor.apply();
    }

    public static String getACCOUNTNo() {
        return getInstance().getString(ACCOUNTNo, "");
    }

    public static void saveAccountNo(String user_type) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(ACCOUNTNo, user_type);
        editor.apply();
    }

    public static String gettocken_id() {
        return getInstance().getString(Tocken, "");
    }

    public static void saveTocken(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(Tocken, startKm);
        editor.apply();
    }

    public static String getSaveType() {
        return getInstance().getString(TypeAgain, "");
    }

    public static void saveType(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(TypeAgain, startKm);
        editor.apply();
    }

    public static String gettockenUserType() {
        return getInstance().getString(TYPE, null);
    }

    public static void saveTockenUserType(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(TYPE, startKm);
        editor.apply();
    }


    public static String getUserPhone() {
        return getInstance().getString(PHONE, null);
    }

    public static void saveUserPhone(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(PHONE, startKm);
        editor.apply();
    }

    public static String getUserName() {
        return getInstance().getString(USER_NAME, null);
    }

    public static void saveUserName(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(USER_NAME, startKm);
        editor.apply();
    }

    public static String getUserEmail() {
        return getInstance().getString(USER_EMAIL_ID, null);
    }

    public static void saveUserEmail(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(USER_EMAIL_ID, startKm);
        editor.apply();
    }


    public static String getUserId() {
        return getInstance().getString(USER_ID, null);
    }

    public static void saveUserId(String startKm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(USER_ID, startKm);
        editor.apply();
    }


    public static String getMOBILE() {
        return getInstance().getString(PHONE, null);
    }

    public static void saveMOBILE(String mobile) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(PHONE, mobile);
        editor.apply();
    }

    public static String getDRIVER_NAME() {
        return getInstance().getString(DRIVER_NAME, null);
    }

    public static void saveDRIVER_NAME(String driverName) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(DRIVER_NAME, driverName);
        editor.apply();
    }

    public static String getLatitude() {
        return getInstance().getString(LATITUDE, "");
    }

    public static void saveLatitude(String driverName) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LATITUDE, driverName);
        editor.apply();
    }

    public static String getLongitude() {
        return getInstance().getString(LONGITUDE, "");
    }

    public static void saveLONGITUDE(String longitide) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LONGITUDE, longitide);
        editor.apply();
    }

    public static String getClientPhone() {
        return getInstance().getString(OTP_VERIFY_PHONE, null);
    }

    public static void saveClientPhone(String notificationRequest) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(OTP_VERIFY_PHONE, notificationRequest);
        editor.apply();
    }



    public static String getNotificationRequest() {
        return getInstance().getString(NOTIFICATION_REQUEST, null);
    }

    public static void saveNotificationRequest(String notificationRequest) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(NOTIFICATION_REQUEST, notificationRequest);
        editor.apply();
    }


    public static String getNotificationTime() {
        return getInstance().getString(NOTIFICATION_TIMME, null);
    }

    public static void saveNotificationTIme(String notificationJson) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(NOTIFICATION_TIMME, notificationJson);
        editor.apply();
    }


    public static String getNotificationJson() {
        return getInstance().getString(NOTIFICATION_JSON, null);
    }

    public static void saveNotificationJson(String notificationJson) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(NOTIFICATION_JSON, notificationJson);
        editor.apply();
    }

    public static String getClientId() {
        return getInstance().getString(CLIENT_ID, null);
    }

    public static void saveClientId(String clientId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CLIENT_ID, clientId);
        editor.apply();
    }

    public static String getHireCareGiverId() {
        return getInstance().getString(HIRE_CAREGIVER_ID, null);
    }

    public static void saveHireCareGiverId(String hireCareGiverId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(HIRE_CAREGIVER_ID, hireCareGiverId);
        editor.apply();
    }

    public static String getClientGender() {
        return getInstance().getString(CLIENT_GENDER, null);
    }

    public static void saveClientGender(String hireCareGiverId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CLIENT_GENDER, hireCareGiverId);
        editor.apply();
    }

    public static String getCancelRequestFromClient() {
        return getInstance().getString(CANCEL_REQUEST_FROM_CLIENT, null);
    }

    public static void saveCancelRequestFromClient(String hireCareGiverId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CANCEL_REQUEST_FROM_CLIENT, hireCareGiverId);
        editor.apply();
    }



    public static String getHireLaterMessage() {
        return getInstance().getString(HIRE_LATER_NOTIFICATION, null);
    }

    public static void saveHireLaterMessage(String hireCareGiverId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(HIRE_LATER_NOTIFICATION, hireCareGiverId);
        editor.apply();
    }


    public static String getCareGiverId() {
        return getInstance().getString(CAREGIVER_ID, null);
    }

    public static void saveCareGiverId(String hireCareGiverId) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CAREGIVER_ID, hireCareGiverId);
        editor.apply();
    }

    public static String getTimerTime() {
        return getInstance().getString(TIMER_LAST_TIME, null);
    }

    public static void saveTimerTime(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(TIMER_LAST_TIME, startTimer);
        editor.apply();
    }


    public static String getStartTimer() {
        return getInstance().getString(START_TIMER, null);
    }

    public static void saveStartTimer(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(START_TIMER, startTimer);
        editor.apply();
    }


    public static String getSRCAmbulanceLatitude() {
        return getInstance().getString(SAVE_SRC_LAT_AMBULANCE, "");
    }

    public static void saveSRCAmbulanceLatitude(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_SRC_LAT_AMBULANCE, startTimer);
        editor.apply();
    }

    public static String getDestAmbulanceLatitude() {
        return getInstance().getString(SAVE_DEST_LAT_AMBULANCE, null);
    }

    public static void saveDestAmbulanceLatitude(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_DEST_LAT_AMBULANCE, startTimer);
        editor.apply();
    }


    public static String getSRCAmbulanceLongitude() {
        return getInstance().getString(SAVE_SRC_LONG_AMBULANCE, null);
    }

    public static void saveSRCAmbulanceLongitude(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_SRC_LONG_AMBULANCE, startTimer);
        editor.apply();
    }

    public static String getDESTAmbulanceLongitude() {
        return getInstance().getString(SAVE_DEST_LONG_AMBULANCE, null);
    }

    public static void saveDESTAmbulanceLongitude(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_DEST_LONG_AMBULANCE, startTimer);
        editor.apply();
    }


    public static String getInWorking() {
        return getInstance().getString(SAVE_IN_WORKING, "");
    }

    public static void saveInWOrking(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_IN_WORKING, startTimer);
        editor.apply();
    }

    public static boolean getPaymentStatus() {
        return getInstance().getBoolean(PAYMENT_STATUS, false);
    }

    public static void savePaymentStatus(boolean startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(PAYMENT_STATUS, startTimer);
        editor.apply();
    }


    public static boolean getRatingStatus()
    {
        return getInstance().getBoolean(RATING_STATUS, false);
    }

    public static void saveRatingStatus(boolean startTimer)
    {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(RATING_STATUS, startTimer);
        editor.apply();
    }

    public static boolean getRideStart() {
        return getInstance().getBoolean(START_RIDE, false);
    }

    public static void saveRideStore(boolean startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(START_RIDE, startTimer);
        editor.apply();
    }

    public static boolean getSummeryPage() {
        return getInstance().getBoolean(SUMMERY, false);
    }

    public static void saveSummerypage(boolean startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(SUMMERY, startTimer);
        editor.apply();
    }

    public static String getPayType()
    {
        return getInstance().getString(UserPayType, "");
    }

    public static void savePayType(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(UserPayType, startTimer);
        editor.apply();
    }


    public static boolean getAmbulanceSummery() {
        return getInstance().getBoolean(AmbulanceSummery, false);
    }

    public static void saveAmbulance(boolean startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(AmbulanceSummery, startTimer);
        editor.apply();
    }

    public static String getMessageForSummery() {
        return getInstance().getString(SAVE_IN_WORKING, "");
    }

    public static void saveMessageForsummery(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SAVE_IN_WORKING, startTimer);
        editor.apply();
    }


    public static String getCaregiverMessageForSummery() {
        return getInstance().getString(CARE_GIVER_SUMMERY, "");
    }

    public static void saveCaregiverMessageForsummery(String startTimer) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(CARE_GIVER_SUMMERY, startTimer);
        editor.apply();
    }

}
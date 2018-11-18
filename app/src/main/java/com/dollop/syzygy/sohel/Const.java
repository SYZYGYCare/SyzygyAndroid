package com.dollop.syzygy.sohel;

/**
 * Created by Meenakshi on 4/4/2017.
 */

public class Const {

    public interface URL {
      //  http://syzygycare.com/

        public static String Image_Url_CareGiver = "https://syzygycare.com/careApp/uploads/document/caregiver/";

        public static String Image_Url_Ambulance = "https://syzygycare.com/careApp/uploads/document/ambulance/";

        public static String host_Url = "https://syzygycare.com/careApp/";
        public static String Image_Url = "https://syzygycare.com/careApp/uploads/profile/";
        public static String Image_Url_health = "https://syzygycare.com/careApp/uploads/healthTips/";
        public static String GetOtp_Url = host_Url + "getOtp";
        public static String Client_MatchOtp = host_Url + "matchOtp";
        public static String MatechOtpAndRegister_Url = host_Url + "matchOtpAndRegistration";
        public static String LogOut = host_Url + "logout";
        public static String Login_Url = host_Url + "login";
        public static String HireCareGiverHistroy_Url = host_Url + "getCargiverHistory";
        public static String Remaining_Amount = host_Url + "getCargiverRemainingPayment";
        public static String HireCareGiverHistroy_ClientUrl = host_Url + "getCustomerHistory";
        public static String User_claim = host_Url + "user_claim";
        public static String AddEmerGencyClientContact_Url = host_Url + "addEmergencyNo";
        public static String DeleteEmerGencyClientContact_Url = host_Url + "deleteEmergencyNo";
        public static String Client_ForgotPassword_Url = host_Url + "forgot_password";
        public static String MatchOtp = host_Url + "matchOtp";
        public static String Client_ChangePasword_Url = host_Url + "change_password";
        public static String Update_Profile_Url = host_Url + "updateProfile";
        public static String Add_Senior_Url = host_Url + "addSenior";
        public static String Get_Senior_Url = host_Url + "getSenior";
        public static String Client_Match_Otp = host_Url + "matchOtp";
        public static String Registration_Url = host_Url + "registration";
        public static String GET_HEALTH_CATEGORY = host_Url + "getHealthCategory";
        public static String GET_HEALTH_SUB_CATEGORY = host_Url + "getHealthSubCategory";
        public static String GET_HEALTH_CO_SUB_CATEGORY = host_Url + "getHealthCoSubCategory";
        public static String GET_HEALTH_POINTS = host_Url + "getHealthPoints";
        public static String GetToDo = host_Url + "getAllHealthPoints";
        public static String AddHealthPoint = host_Url + "addPoints";
        public static String GET_CLAIM = host_Url + "addClaim";
        public static String Client_Ambulance_Url = host_Url + "addAmbulance";
        /////////////////////////
        public static String GetService_Url = host_Url + "getServices";/////////
        ///////////////////////////

        ///////////////////////////
        public static String GETSpecializationList_Url = host_Url + "specializationList";//////
        //////////////////////////

        public static String Get_Profile_Url = host_Url + "Get_Profile_Url";
        public static String Get_Emergency_Number = host_Url + "getEmergencyNo";
        public static String GET_PROFILE = host_Url + "getProfileDetail";
        public static String GET_CAREGIVER_LOC = host_Url + "getNearestCaregiver";
        public static String UPDATE_PROFILE = host_Url + "updateProfile";
        public static String UPDATE_CAREGIVER_LOCATION = host_Url + "updateCaregiverLatLong";
        public static String HiringConfirmation_Notification_Client = host_Url + "hiringConfirmationCleint";
        public static String GETSCHEDUAL_TIMEDATE = host_Url + "addScheduleNotification";

        public static String UPDATE_CAREGIVER = host_Url + "addDetailsCaregiver";
        ///////////////////////
        public static String GET_AMBULANCE_TYPE = host_Url + "getAmbulanceType";///////////
        ///////////////////////
        public static String ADDEMERGENCY = host_Url + "addEmergencyNo";
        public static String CAREGIVER_CHANGE_PASSWORD = host_Url + "change_password_at_login";
        public static String CAREGIVER_FORGET_PASSWORD = host_Url + "forgot_password";
        public static String UPDATE_FCM_ID = host_Url + "updateFcmId";
        public static String CAREGIVER_ACCEPT_REQUEST = host_Url + "acceptClientRequest";
        public static String CAREGIVER_UPDATE_LOCATION = host_Url + "updateCaregiveCurrentLocation";
        public static String CAREGIVER_GET_LOCATION = host_Url + "getCaregiveCurrentLocation";
        public static String GET_BOOKING_STATUS_CAREGIVER = host_Url + "getbookingstatusforcaregiver";
        public static String GET_BOOKING_STATUS_USER = host_Url + "getbookingstatusforuser";
        public static String CAREGIVER_START_STOP_HIRING_TIME = host_Url + "startStopHiringTime";
        public static String CAREGIVER_CANCEL_REQUEST = host_Url + "cancel_request";

        public static String UPDATE_CANCEL_REQUEST = host_Url + "updateBookingStatus";
        public static String DENY_REQUEST = host_Url + "denyRequest";

        public static String CAREGIVER_PAUSE_OR_RESUME = host_Url + "pauseResumeHiringTime";
        public static String RATTING = host_Url + "rating";
        public static String Caregiver_rating = host_Url + "caregiver_rating";
        public static String TRUST_BADGES = host_Url + "getTrustBadges";
        public static String AMUBLANCE_START_STOP = host_Url + "startStopAmbulanceTime";//
        public static String PAYMENT_TO_SERVER = host_Url + "addPaymentHistory";

        public static String GET_PREVIOUS_CAREGIVER = host_Url + "getCaregiverDetail";
        public static String ADD_CHECKLIST = host_Url + "addCheckList";
        public static String GET_CHECKLIST = host_Url + "getCheckList";
        public static String ADD_REMAINDER = host_Url + "addRemainder";
        public static String GET_REMAINDER = host_Url + "get_reminders";
        public static String SEND_CHAT_MESSAGE = host_Url + "sendChat";
        public static String GET_CHAT_MESSAGE = host_Url + "getChat";
        public static String GET_REFER_CODE = host_Url + "get_refer_code";
        public static String USER_REFER_CODE = host_Url + "invite_friend";
        public static String DELETE_REMAINDER = host_Url + "delete_reminders";
        public static String GET_WALLET_BALANCE = host_Url + "get_wallet";
        public static String GET_USER_WALLET_BALANCE = host_Url + "get_user_wallet";
        public static String GET_USER_TOKEN= host_Url + "getUserToken";
        public static String GET_REMAINDER_DEFAULT_DATA = host_Url + "get_reminder_cat";
        public static String GET_REMAINDER_TYPE= host_Url + "get_reminder_care";
        public static String Update_Wallet = host_Url + "update_wallet";
        public static String Update_User_Wallet = host_Url + "update_user_wallet";
        public static String GetCityList = host_Url + "getCityList";
    }
}
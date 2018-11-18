package com.dollop.syzygy.utility;

public class Constants {
	public static final String PARAMETER_SEP = "&";
	public static final String PARAMETER_EQUALS = "=";
	public static final String ACCEPT_USER_AMBULANCE = "2";
	public static final String HIRE_CARE_GIVER = "1";
	public static final String HIRE_AMBULANCE = "2";

	public static final String PRODCUTION_SLAT = "ErManUDVRq";
	public static final String PRODUCTION_MERCHANT_KEY = "Ah80ItVb";
	//public static final String PRODUCTION_MERCHANT_KEY = "mdyCKV"; Given By Mahendra
	public static final String PRODUCTION_MERCHANT_ID = "6076784";

	//public static final String CARE_GIVER = "11";
	public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
	public static boolean SPECILIZATION_ROLE_DOCTOR = false;

	public static String dateFormatForDisplay(int year, int monthOfYear, int dayOfMonth) {
		int myear = year;
		int month = monthOfYear;
		int day = dayOfMonth;
		String mMonth = "", mday = "", mYear = "";
		String[] mon = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		mMonth = mon[month];
		if (day > 9) {
			mday = day + "";
		} else {
			mday = "0" + day;
		}
		return mday + " " + mMonth + " " + myear;
	}

	public static String dateFormatForDisplayForThisAppOnly(int year, int monthOfYear, int dayOfMonth) {
		int myear = year;
		int month = monthOfYear;
		int day = dayOfMonth;
		String mMonth = "", mday = "", mYear = "";
		if (month > 9) {
			mMonth = month+ "";
		}else {
			mMonth = "0"+month;
		}
		if (day > 9) {
			mday = day + "";
		} else {
			mday = "0" + day;
		}
		return myear + "-" + mMonth + "-" + mday;
	}

	public static String timeFormatForDisplay(int hourOfDay, int minute) {
		boolean isHourIsLessThen9=false;

		String ampm = "", min = "", hMin = "";
		int hour;
		if (hourOfDay >= 12) {
			hour = hourOfDay - 12;
			if (hour == 0) {
				hour = 12;
			}else if(hour<=9){
				isHourIsLessThen9=true;
			}
			ampm = "PM";
		} else {
			hour = hourOfDay;
			if (hour == 0) {
				hour = 12;
			}else if(hour<=9){
				isHourIsLessThen9=true;
			}
			ampm = "AM";
		}
		if (minute < 10) {
			min = "0";
		} else {
			min = "";
		}
		if (hour < 10) {
			hMin = "0";
		} else {
			hMin = "";
		}
		if(isHourIsLessThen9){
			isHourIsLessThen9=false;
			return 0+""+hour + ":" + min + minute + " " + ampm;
		}else {
			isHourIsLessThen9=false;
			return hour + ":" + min + minute + " " + ampm;
		}
	}
}
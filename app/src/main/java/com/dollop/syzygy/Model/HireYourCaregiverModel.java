package com.dollop.syzygy.Model;

/**
 * Created by sarvesh on 9/4/2017.
 */

public class HireYourCaregiverModel {
    String hireimage="";
    String type="";

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestianme() {
        return destianme;
    }

    public void setDestianme(String destianme) {
        this.destianme = destianme;
    }

    public String getTotalkM() {
        return totalkM;
    }

    public void setTotalkM(String totalkM) {
        this.totalkM = totalkM;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStart_tme() {
        return start_tme;
    }

    public void setStart_tme(String start_tme) {
        this.start_tme = start_tme;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    String hirename="";
    String hirespecility="";
    String hiredesc="";
    String hirerating="";
    String sourcename="";
    String destianme="";
    String totalkM="";
    String PaymentMode="";
    String transactionId="";
    String start_tme="";
    String end_time="";
    String totalTime="";
    String totalAmount;

    public String getHiredCaregiverId() {
        return hiredCaregiverId;
    }

    public void setHiredCaregiverId(String hiredCaregiverId) {
        this.hiredCaregiverId = hiredCaregiverId;
    }

    String hiredCaregiverId="";

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    /*{"status":200,"message":"success","data":[
    {"full_name":"Shekhar Ahiwar","profile_pic":"2415f5864061b1ba8de089e0e7e05c23_1.png","user_id":"248","caregiver_id":"76","amount":"520.00","payment_mode":"online","transaction_id":"214564245","type":"ambulance","source":"guna","destination":"indore","total_kilometer":"5.30"},{"full_name":"Sohel care","profile_pic":"1bcdda1ed62478039ed1f24f40ec3e82_1.png","user_id":"248","caregiver_id":"72","amount":"600.00","payment_mode":"online","transaction_id":"214564245","type":"caregiver","start_tme":"10:45","end_time":"10:51","total_time":"6"},{"full_name":"kaseem ambulance","profile_pic":null,"user_id":"248","caregiver_id":"73","amount":"800.00","payment_mode":"online","transaction_id":"214564245","type":"ambulance","source":"bhopal","destination":"indore","total_kilometer":"5.30"}]}*/

    public HireYourCaregiverModel(String hireimage, String hirename, String hirespecility, String hiredesc, String hirerating) {
        this.hireimage = hireimage;
        this.hirename = hirename;
        this.hirespecility = hirespecility;
        this.hiredesc = hiredesc;
        this.hirerating = hirerating;
    }

    public HireYourCaregiverModel() {

    }

    public String getHirerating() {
        return hirerating;
    }

    public void setHirerating(String hirerating) {
        this.hirerating = hirerating;
    }


    public String getHireimage() {
        return hireimage;
    }

    public void setHireimage(String hireimage) {
        this.hireimage = hireimage;
    }

    public String getHirename() {
        return hirename;
    }

    public void setHirename(String hirename) {
        this.hirename = hirename;
    }

    public String getHirespecility() {
        return hirespecility;
    }

    public void setHirespecility(String hirespecility) {
        this.hirespecility = hirespecility;
    }

    public String getHiredesc() {
        return hiredesc;
    }

    public void setHiredesc(String hiredesc) {
        this.hiredesc = hiredesc;
    }
}


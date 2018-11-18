package com.dollop.syzygy.Model;

/**
 * Created by sohel on 11/21/2017.
 */

public class GetRemainderModel {
    private String notification_id;
    private String user_id;
    private String start_date;
    private String time;
    private String interval;
    private String reminder_for;
    private String required_care;
    private String reminder_type;
    private String number_of_reminder;
    private String any_reminder;
    private String created_date;

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getReminder_for() {
        return reminder_for;
    }

    public void setReminder_for(String reminder_for) {
        this.reminder_for = reminder_for;
    }

    public String getRequired_care() {
        return required_care;
    }

    public void setRequired_care(String required_care) {
        this.required_care = required_care;
    }

    public String getReminder_type() {
        return reminder_type;
    }

    public void setReminder_type(String reminder_type) {
        this.reminder_type = reminder_type;
    }

    public String getNumber_of_reminder() {
        return number_of_reminder;
    }

    public void setNumber_of_reminder(String number_of_reminder) {
        this.number_of_reminder = number_of_reminder;
    }

    public String getAny_reminder() {
        return any_reminder;
    }

    public void setAny_reminder(String any_reminder) {
        this.any_reminder = any_reminder;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}

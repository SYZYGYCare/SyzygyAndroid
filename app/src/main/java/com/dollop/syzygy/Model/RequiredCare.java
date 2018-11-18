package com.dollop.syzygy.Model;

/**
 * Created by sohel on 11/22/2017.
 */

public class RequiredCare {

    /**
     * reminder_category_id : 5
     * category_name : Medication
     */

    private String reminder_category_id;
    private String category_name;

    public String getReminder_category_id() {
        return reminder_category_id;
    }

    public void setReminder_category_id(String reminder_category_id) {
        this.reminder_category_id = reminder_category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}

package com.dollop.syzygy.Model;

/**
 * Created by sohel on 11/22/2017.
 */

public class RequiredCareType {

    /**
     * reminder_category_id : 5
     * care_type_1 : OB
     * care_type_2 : BD
     * care_type_3 : SOS+
     * care_type_4 : other
     */

    private String reminder_category_id;
    private String care_type_1;
    private String care_type_2;
    private String care_type_3;
    private String care_type_4;

    public String getReminder_category_id() {
        return reminder_category_id;
    }

    public void setReminder_category_id(String reminder_category_id) {
        this.reminder_category_id = reminder_category_id;
    }

    public String getCare_type_1() {
        return care_type_1;
    }

    public void setCare_type_1(String care_type_1) {
        this.care_type_1 = care_type_1;
    }

    public String getCare_type_2() {
        return care_type_2;
    }

    public void setCare_type_2(String care_type_2) {
        this.care_type_2 = care_type_2;
    }

    public String getCare_type_3() {
        return care_type_3;
    }

    public void setCare_type_3(String care_type_3) {
        this.care_type_3 = care_type_3;
    }

    public String getCare_type_4() {
        return care_type_4;
    }

    public void setCare_type_4(String care_type_4) {
        this.care_type_4 = care_type_4;
    }
}

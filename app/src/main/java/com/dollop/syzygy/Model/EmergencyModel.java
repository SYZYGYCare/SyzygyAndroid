package com.dollop.syzygy.Model;

import java.io.Serializable;

/**
 * Created by arpit on 4/11/17.
 */

public class EmergencyModel implements Serializable {
    String categoryId, categoryName, parentId, status, checklist_id, image;



    public String getEmergencyNo() {
        return categoryName;
    }

    public void setEmergencyNo(String categoryName) {
        this.categoryName = categoryName;
    }


}

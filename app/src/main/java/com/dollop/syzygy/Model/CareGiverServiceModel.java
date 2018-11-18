package com.dollop.syzygy.Model;

/**
 * Created by user on 10/7/2017.
 */

public class CareGiverServiceModel {
    String Service_id,Service_name,ServiceCreatedDate;

    public String getService_id() {
        return Service_id;
    }

    public void setService_id(String service_id) {
        Service_id = service_id;
    }

    public String getService_name() {
        return Service_name;
    }

    public void setService_name(String service_name) {
        Service_name = service_name;
    }

    public String getServiceCreatedDate() {
        return ServiceCreatedDate;
    }

    public void setServiceCreatedDate(String serviceCreatedDate) {
        ServiceCreatedDate = serviceCreatedDate;
    }
}

package com.dollop.syzygy.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vikas on 10/26/2017.
 */

public class UserCurrentStatusResponse
{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<UserCurrentStatus> getData() {
        return data;
    }

    public void setData(List<UserCurrentStatus> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<UserCurrentStatus> data = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }




}

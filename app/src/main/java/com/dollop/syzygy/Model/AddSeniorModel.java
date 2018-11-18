package com.dollop.syzygy.Model;

import java.io.Serializable;

/**
 * Created by Rahul jain on 07-06-2017.
 */

public class AddSeniorModel implements Serializable {
    String seniorName;
    String user_senior_id;
    String Image;
    String seniorcontact;
    String seniorGender;
    String seniorAge;
    String seniorAddress;
    String seniorDecription;
    String seniorSpical_need;
    String special_instruction;

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    String relationShip;

    public String getSpecial_instruction() {
        return special_instruction;
    }

    public void setSpecial_instruction(String special_instruction) {
        this.special_instruction = special_instruction;
    }

    public String getUser_senior_id() {
        return user_senior_id;
    }

    public void setUser_senior_id(String user_senior_id) {
        this.user_senior_id = user_senior_id;
    }

    public AddSeniorModel() {
        this.seniorName = seniorName;
        this.seniorcontact = seniorcontact;
        this.seniorGender = seniorGender;
        this.seniorAge = seniorAge;
        this.seniorAddress = seniorAddress;
        this.seniorDecription = seniorDecription;
        this.seniorSpical_need = seniorSpical_need;
    }

    public String getSeniorName() {
        return seniorName;
    }

    public void setSeniorName(String seniorName) {
        this.seniorName = seniorName;
    }

    public String getSeniorcontact() {
        return seniorcontact;
    }

    public void setSeniorcontact(String seniorcontact) {
        this.seniorcontact = seniorcontact;
    }

    public String getSeniorGender() {
        return seniorGender;
    }

    public void setSeniorGender(String seniorGender) {
        this.seniorGender = seniorGender;
    }

    public String getSeniorAge() {
        return seniorAge;
    }

    public void setSeniorAge(String seniorAge) {
        this.seniorAge = seniorAge;
    }

    public String getSeniorAddress() {
        return seniorAddress;
    }

    public void setSeniorAddress(String seniorAddress) {
        this.seniorAddress = seniorAddress;
    }

    public String getSeniorDecription() {
        return seniorDecription;
    }

    public void setSeniorDecription(String seniorDecription) {
        this.seniorDecription = seniorDecription;
    }

    public String getSeniorSpical_need() {
        return seniorSpical_need;
    }

    public void setSeniorSpical_need(String seniorSpical_need) {
        this.seniorSpical_need = seniorSpical_need;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
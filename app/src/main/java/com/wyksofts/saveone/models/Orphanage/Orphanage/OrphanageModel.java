package com.wyksofts.saveone.models.Orphanage.Orphanage;

public class OrphanageModel {

    String name;
    String location;
    String coordinates;
    String country;
    String description;
    String number_of_children;
    String phone_number;
    String bank_account;
    String bank_account_name;
    String email;
    String group_image;
    String verified;
    String what_needed;

    public OrphanageModel() {
    }

    public String getWhat_needed() {
        return what_needed;
    }

    public void setWhat_needed(String what_needed) {
        this.what_needed = what_needed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }


    public String getGroup_image() {
        return group_image;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber_of_children() {
        return number_of_children;
    }

    public void setNumber_of_children(String number_of_children) {
        this.number_of_children = number_of_children;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getTill_number() {
        return till_number;
    }

    public void setTill_number(String till_number) {
        this.till_number = till_number;
    }

    String till_number;

    public OrphanageModel(String name, String location, String coordinates,
                          String country, String description, String number_of_children,
                          String phone_number,
                          String bank_account, String bank_account_name,
                          String till_number, String url, String email, String verified, String what_needed) {
        this.name = name;
        this.location = location;
        this.coordinates = coordinates;
        this.country = country;
        this.description = description;
        this.number_of_children = number_of_children;
        this.phone_number = phone_number;
        this.bank_account = bank_account;
        this.bank_account_name = bank_account_name;
        this.till_number = till_number;
        this.group_image = url;
        this.email = email;
        this.verified = verified;
        this.what_needed = what_needed;
    }

}

package com.wyksofts.saveone.models.Orphanage.Donations;

public class DonationsModel {
    String name;
    String food_stuffs;
    String clothing;
    String education_materials;
    String location;
    String phone_number;
    String email;
    String other;
    String health;
    String isDonationReceived;

    public DonationsModel(String name, String food_stuffs, String clothing,
                          String education_materials, String location, String phone_number,
                          String email, String isDonationReceived, String other, String health) {
        this.name = name;
        this.food_stuffs = food_stuffs;
        this.clothing = clothing;
        this.education_materials = education_materials;
        this.location = location;
        this.phone_number = phone_number;
        this.email = email;
        this.isDonationReceived = isDonationReceived;
        this.other = other;
        this.health = health;
    }


    public String getIsDonationReceived() {
        return isDonationReceived;
    }

    public void setIsDonationReceived(String isDonationReceived) {
        this.isDonationReceived = isDonationReceived;
    }
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood_stuffs() {
        return food_stuffs;
    }

    public void setFood_stuffs(String food_stuffs) {
        this.food_stuffs = food_stuffs;
    }

    public String getClothing() {
        return clothing;
    }

    public void setClothing(String clothing) {
        this.clothing = clothing;
    }

    public String getEducation_materials() {
        return education_materials;
    }

    public void setEducation_materials(String education_materials) {
        this.education_materials = education_materials;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}

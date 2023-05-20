package com.jms.searchpharmacy.data.model.server;

import com.google.gson.annotations.SerializedName;

public class Hospital {
    @SerializedName("loadaddress")
    private String load_address;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("address")
    private String address;
    @SerializedName("startdate")
    private String startdate;
    @SerializedName("totaldoctor")
    private Integer total_doctor;

    public Hospital(String load_address, String name, String type, String address, String startdate, Integer total_doctor) {
        this.load_address = load_address;
        this.name = name;
        this.type = type;
        this.address = address;
        this.startdate = startdate;
        this.total_doctor = total_doctor;
    }

    public String getLoad_address() {
        return load_address;
    }

    public void setLoad_address(String load_address) {
        this.load_address = load_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public Integer getTotal_doctor() {
        return total_doctor;
    }

    public void setTotal_doctor(Integer total_doctor) {
        this.total_doctor = total_doctor;
    }
}

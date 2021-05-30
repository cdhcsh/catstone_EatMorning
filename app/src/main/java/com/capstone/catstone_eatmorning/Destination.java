package com.capstone.catstone_eatmorning;

public class Destination {
    public static final String NAME = "destination_name";
    public static final String ADDRESS = "destination_address";
    public static final String DETAIL = "destination_detail_address";

    public String destination_name;
    public String destination_address;
    public String destination_detail_address;

    public Destination(String destination_name, String destination_address, String destination_detail_address) {
        this.destination_name = destination_name;
        this.destination_address = destination_address;
        this.destination_detail_address = destination_detail_address;
    }
}

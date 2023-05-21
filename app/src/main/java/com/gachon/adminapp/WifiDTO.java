package com.gachon.adminapp;

public class WifiDTO {

    private String SSID;
    private String BSSID;
    private int RSSI;

    public WifiDTO(String ssid, String bssid, int rssi) {
        this.SSID = ssid;
        this.BSSID = bssid;
        this.RSSI = rssi;
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public int getRSSI() {
        return RSSI;
    }

    // setter 생략

}

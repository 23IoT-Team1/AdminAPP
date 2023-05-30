package com.gachon.adminapp;

public class WifiDTO {

    private String ssid;
    private String bssid;
    private String rssi;

    public WifiDTO(String ssid, String bssid, String rssi) {

        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
    }

    public String getSSID() {
        return ssid;
    }

    public String getBSSID() {
        return bssid;
    }

    public String getRSSI() {
        return rssi;
    }

}

    // setter 생략

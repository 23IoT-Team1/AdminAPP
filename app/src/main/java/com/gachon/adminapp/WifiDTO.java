package com.gachon.adminapp;

<<<<<<< HEAD

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
=======
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
>>>>>>> origin/function
    }

    // setter 생략

<<<<<<< HEAD
}
=======
}
>>>>>>> origin/function

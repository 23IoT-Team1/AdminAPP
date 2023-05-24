package com.gachon.adminapp;

import java.util.ArrayList;

//전달하는 노드의 json 형식
class node {
    int floor;
    String rp;
    String place;
    ArrayList<WifiDTO> aps;

    public node(String floor, String rp, String place, ArrayList arrayList) {

        if(floor.equals("4F")) this.floor = 4;
        else if(floor.equals("5F")) this.floor = 5;
        else this.floor = 0;

        this.rp = rp;
        this.place = place;
        this.aps = arrayList;
    }
}

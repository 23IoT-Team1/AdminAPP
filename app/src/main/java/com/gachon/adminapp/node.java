package com.gachon.adminapp;

import java.util.ArrayList;

//전달하는 노드의 json 형식
class node {
    String floor;
    String rp;
    String name;
    ArrayList<WifiDTO> aps;

    public node(String floor, String rp, String name, ArrayList arrayList) {
        this.floor = floor;
        this.rp = rp;
        this.name = name;
        this.aps = arrayList;
    }
}

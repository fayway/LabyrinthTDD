package com.nespresso.sofa.recruitment.labyrinth;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private String code;
    private Map<String, Room> relatives = new HashMap<>();
    private Map<String, Door> doors = new HashMap<>();

    public Room(String code) {
        this.code = code;
    }

    public Map<String, Door> getDoors() {
        return doors;
    }

    public Map<String, Room> getRelatives() {
        return relatives;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return code;
    }
}

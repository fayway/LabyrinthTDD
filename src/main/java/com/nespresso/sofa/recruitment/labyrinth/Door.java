package com.nespresso.sofa.recruitment.labyrinth;

import com.nespresso.sofa.recruitment.labyrinth.exception.DoorAlreadyClosedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Door {

    public final String DOOR_SEPARATOR = new String("|");
    public final String DOOR_SEPARATOR_WITH_SENSOR = new String("$");

    private Room firstRoom;
    private Room secondRoom;
    private boolean hasSensor = false;
    private boolean isClose = false;


    public Door(String path) {
        initDoorAndItsTwoRooms(path);
    }

    private void initDoorAndItsTwoRooms(String path) {
        Pattern pattern = Pattern.compile("([A-Z]){1}([" + DOOR_SEPARATOR + DOOR_SEPARATOR_WITH_SENSOR + "])([A-Z]){1}");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            firstRoom = new Room(matcher.group(1));
            secondRoom = new Room(matcher.group(3));
            hasSensor = matcher.group(2).equals(DOOR_SEPARATOR_WITH_SENSOR);

            String gateCode = firstRoom.getCode() + secondRoom.getCode();
            firstRoom.getDoors().put(gateCode, this);
            secondRoom.getDoors().put(gateCode, this);
        } else {
            throw new RuntimeException("Illegal Labyrinth constructor");
        }
    }

    public String getCode() {
        return firstRoom.getCode() + secondRoom.getCode();
    }

    public void close() {
        if (isClose) {
            throw new DoorAlreadyClosedException();
        }
        isClose = true;
    }

    public Room getFirstRoom() {
        return firstRoom;
    }

    public Room getSecondRoom() {
        return secondRoom;
    }

    public boolean hasSensor() {
        return hasSensor;
    }

    public boolean isClose() {
        return isClose;
    }

    public String toString() {
        StringBuilder str = new StringBuilder(firstRoom.toString());
        str.append(hasSensor ? DOOR_SEPARATOR_WITH_SENSOR : DOOR_SEPARATOR);
        str.append(secondRoom.toString());

        return str.toString();
    }
}

package com.nespresso.sofa.recruitment.labyrinth;

import com.nespresso.sofa.recruitment.labyrinth.exception.ClosedDoorException;
import com.nespresso.sofa.recruitment.labyrinth.exception.IllegalMoveException;

import java.util.HashMap;
import java.util.Map;

public class Labyrinth {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Door> doors = new HashMap<>();
    private Room currentRoom = null;
    private Door lastDoor = null;
    private StringBuilder sensorsReport = new StringBuilder();

    public Labyrinth(String... paths) {
        initLabyrinthRoomsAndDoors(paths);
    }

    public void popIn(String roomCode) throws IllegalMoveException {
        if (!rooms.containsKey(roomCode)) {
            throw new IllegalMoveException();
        }
        currentRoom = rooms.get(roomCode);
    }

    public void walkTo(String roomCode) {
        guardAgainstIllegalMove(roomCode);
        Room targetRoom = rooms.get(roomCode);
        crossDoorToRoom(targetRoom);
    }

    private Room guardAgainstIllegalMove(String roomCode) {
        if (!rooms.containsKey(roomCode)) {
            throw new IllegalMoveException();
        }

        Room targetRoom = rooms.get(roomCode);
        if (!currentRoom.getRelatives().containsKey(roomCode) && !targetRoom.getRelatives().containsKey(currentRoom.getCode())) {
            throw new IllegalMoveException();
        }
        return targetRoom;
    }

    private void crossDoorToRoom(Room targetRoom) {
        Door door = doors.get(currentRoom.getCode() + targetRoom.getCode());
        if (null == door) {
            door = doors.get(targetRoom.getCode() + currentRoom.getCode());
        }
        if (null != door && door.isClose()) {
            throw new ClosedDoorException();
        }
        reportDoor(door);

        lastDoor = door;
        currentRoom = targetRoom;
    }

    private void initLabyrinthRoomsAndDoors(String[] paths) {
        for (String pathStr : paths) {
            Door door = new Door(pathStr);
            doors.put(door.getCode(), door);
            addRoomToLabyrinthIfNotExists(door.getFirstRoom());
            addRoomToLabyrinthIfNotExists(door.getSecondRoom());
            addSecondRoomToFirstRoomRelatives(door);
        }
    }

    private void addRoomToLabyrinthIfNotExists(Room room) {
        if (!rooms.containsKey(room.getCode())) {
            rooms.put(room.getCode(), room);
        }
    }

    private void addSecondRoomToFirstRoomRelatives(Door door) {
        Room secondRoom = door.getSecondRoom();
        Map<String, Room> firstRoomRelatives = rooms.get(door.getFirstRoom().getCode()).getRelatives();
        firstRoomRelatives.put(secondRoom.getCode(), secondRoom);
    }

    private void reportDoor(Door door) {
        if (door.hasSensor()) {
            if (sensorsReport.length() != 0) {
                sensorsReport.append(";");
            }
            sensorsReport.append(door.getFirstRoom().getCode() + door.getSecondRoom().getCode());
        }
    }

    public void closeLastDoor() {
        if (null != lastDoor) {
            lastDoor.close();
        }
    }

    public String readSensors() {
        return sensorsReport.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Room room : rooms.values()) {
            str.append(room + ", ");
        }
        return str.toString();
    }
}

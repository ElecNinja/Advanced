package advanced.prog.project.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static advanced.prog.project.DBOperations.getAllRoomsFromDB;
import static advanced.prog.project.DBOperations.isAvailableRoom;

public class Hotel {
    private String name;
    protected static List<Room> rooms = new ArrayList<>();
    protected List<Booking> bookings = new ArrayList<>();
    public Hotel() {

    }
    public Hotel(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static void addRoom(Room room) {
        rooms.add(room);
    }
    public static List<Room> getAllRooms() {
        return rooms;
    }
    public static void loadRoomsFromDB() {
        rooms = getAllRoomsFromDB(); // assuming you import the static method or move it into Hotel
    }
    public static void syncRoomAvailability() {
        for (Room room : rooms) {
            boolean available = isAvailableRoom(room.getRoomNumber());
            room.setAvailable(available);
        }
    }
    public Booking bookRoom(Customer customer, Room room, LocalDate startDate, int nights, double totalCost) {
        for (Room rm : rooms) {
            if (rm.getRoomNumber() == room.getRoomNumber() && room.isAvailable) {
                Booking booking = new Booking(customer, room, startDate, nights, totalCost);
                bookings.add(booking);
                return booking;
            }
        }
        return null; // or throw exception
    }
}

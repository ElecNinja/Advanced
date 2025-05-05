package advanced.prog.project.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String name;


    protected List<Room> rooms = new ArrayList<>();
    protected List<Booking> bookings = new ArrayList<>();
    protected List<Rating> ratings = new ArrayList<>();
    public Hotel() {

    }

    public Hotel(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }



    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable && room.getCustomer() == null) {
                available.add(room);
            }
        }
        return available;
    }
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms); // assuming 'rooms' is your internal List<Room>
    }
    public void removeRoom(int roomNumber) {
        rooms.removeIf(room -> room.getRoomNumber() == roomNumber);
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

    public List<Booking> getBookings() {
        return bookings;
    }

}

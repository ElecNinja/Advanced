package advanced.prog.project.models;

public class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber, double pricePerNight, Customer customer) {
        super(roomNumber, pricePerNight, customer);
    }

    @Override
    public int getCapacity() {
        return 2;
    }

    @Override
    public String getRoomType() {
        return "Double";
    }
}

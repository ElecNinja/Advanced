package advanced.prog.project.models;

public class TripleRoom extends Room {
    public TripleRoom(int roomNumber, double pricePerNight, Customer customer) {
        super(roomNumber, pricePerNight, customer);
    }

    @Override
    public int getCapacity() {
        return 3;
    }

    @Override
    public String getRoomType() {
        return "Triple";
    }
}

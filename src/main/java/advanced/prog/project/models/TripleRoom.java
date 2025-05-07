package advanced.prog.project.models;

public class TripleRoom extends Room {
    public TripleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
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

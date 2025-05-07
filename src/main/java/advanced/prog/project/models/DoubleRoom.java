package advanced.prog.project.models;

public class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
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

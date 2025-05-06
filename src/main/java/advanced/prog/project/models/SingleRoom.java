package advanced.prog.project.models;

public class SingleRoom extends Room {
    public SingleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    @Override
    public String getRoomType() {
        return "Single";
    }
}

package advanced.prog.project.models;

public abstract class Room extends Hotel {
    protected int roomNumber;
    protected double pricePerNight;

    public Room(int roomNumber, double pricePerNight) {
        super();
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract int getCapacity();

    public abstract String getRoomType();

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", pricePerNight=" + pricePerNight +
                ", capacity=" + getCapacity() +
                ", type=" + getRoomType() +
                '}';
    }


    public static class SingleRoom extends Room {
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
    public static class DoubleRoom extends Room {
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


    public static class TripleRoom extends Room {
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
}

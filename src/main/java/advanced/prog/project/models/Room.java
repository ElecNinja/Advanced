package advanced.prog.project.models;

public abstract class Room extends Hotel {
    int roomNumber;
    double pricePerNight;
    String type;
    public boolean isAvailable = true;
    Customer customer;

    public Room(int roomNumber, double pricePerNight) {
        super();
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.customer = null; // no customer initially
    }


    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.isAvailable = (customer == null); // optional: update availability
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


    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}



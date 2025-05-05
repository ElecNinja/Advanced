package advanced.prog.project.models;
import java.time.LocalDate;
public class Booking {
        private Customer customer;
        private Room room;
        private LocalDate checkInDate;
        private int nights;

        private double totalCost;


        public Booking(Customer customer, Room room,
                       LocalDate checkInDate, int nights , double totalCost) {
            this.customer = customer;
            this.room = room;
            this.checkInDate = checkInDate;
            this.nights = nights;
            this.totalCost = totalCost;
        }


        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public LocalDate getCheckInDate() {
            return checkInDate;
        }

        public int getNights() {
            return nights;
        }

        public void setCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

    @Override
    public String toString() {
        return customer.getUsername() + " booked " +
                room.getRoomType() + " Room #" + room.getRoomNumber() +
                " starting " + checkInDate +
                ". Total: $" + totalCost;
    }
    }



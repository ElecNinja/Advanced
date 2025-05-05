package advanced.prog.project.models;
import java.time.LocalDate;
public class Booking {
        private int bookingId;
        private Customer customer;
        private Room room;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;


        public Booking(int bookingId, Customer customer, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {
            this.bookingId = bookingId;
            this.customer = customer;
            this.room = room;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
        }

        public int getBookingId() {
            return bookingId;
        }

        public void setBookingId(int bookingId) {
            this.bookingId = bookingId;
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

        public void setCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
        }
    }



package advanced.prog.project.models;
import java.time.LocalDate;
public class Rating {
        private int ratingId;
        private Customer customer;
        private Booking booking;
        private int score;
        private String comment;
        private LocalDate date;

        // Constructor
        public Rating(int ratingId, Customer customer, Booking booking,
                      int score, String comment, LocalDate date) {
            this.ratingId = ratingId;
            this.customer = customer;
            this.booking = booking;
            this.score = score;
            this.comment = comment;
            this.date = date;
        }

        // Getters
        public int getRatingId() {
            return ratingId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Booking getBooking() {
            return booking;
        }

        public int getScore() {
            return score;
        }

        public String getComment() {
            return comment;
        }

        public LocalDate getDate() {
            return date;
        }


    }



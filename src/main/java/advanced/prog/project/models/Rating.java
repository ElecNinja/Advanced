package advanced.prog.project.models;
import java.time.LocalDate;
public class Rating {
        private int ratingId;
        private Customer customer;
        private Booking booking;
        private int rating;
        private String comment;
        private LocalDate date;

        // Constructor
      public Rating(Customer customer){
            this.customer = customer;
        }
        public Rating(Customer customer, Booking booking) {
            this.customer = customer;
            this.booking = booking;
    }
        public Rating(Customer customer, Booking booking,
                      int rating, String comment, LocalDate date) {
            this.customer = customer;
            this.booking = booking;
            this.rating = rating;
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

        public int getRating() {
            return rating;
        }
        public String getComment() {
            return comment;
        }

        public LocalDate getDate() {
            return date;
        }

        // Setters
        public void setRatingId(int ratingId) {
            this.ratingId = ratingId;
        }
        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
        public void setBooking(Booking booking) {
            this.booking = booking;
        }
        public void setRating(int rating) {
            this.rating = rating;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
        public void setDate(LocalDate date) {
            this.date = date;
        }



    }



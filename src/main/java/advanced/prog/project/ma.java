package advanced.prog.project;
import java.util.Date;
import java.util.Scanner;

public class Rating {
    private String ratingId;
    private Customer customer;
    private String bookingId;
    private int stars;
    private String comment;
    private Date ratingDate;

    public Rating(String ratingId, Customer customer, String bookingId, int stars, String comment, Date ratingDate) {
        this.ratingId = ratingId;
        this.customer = customer;
        this.bookingId = bookingId;
        this.stars = stars;
        this.comment = comment;
        this.ratingDate = ratingDate;
    }

    // Static method to enter rating
    public static Rating enterRating(Customer customer, String bookingId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Rate your stay (1 to 5 stars):");
        int stars = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Write a comment:");
        String comment = scanner.nextLine();

        String ratingId = "R" + System.currentTimeMillis();

        return new Rating(ratingId, customer, bookingId, stars, comment, new Date());
    }

    public String getRatingSummary() {
        return "Customer: " + customer.getName() + "\nStars: " + stars + "\nComment: " + comment;
    }

    public boolean isPositive() {
        return stars >= 4;
    }
}

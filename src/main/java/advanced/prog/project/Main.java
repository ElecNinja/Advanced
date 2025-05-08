
package advanced.prog.project;
import advanced.prog.project.models.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static advanced.prog.project.models.Hotel.syncRoomAvailability;

public class Main extends Application {
    private Stage stage;
    private Hotel hotel;
    Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        this.hotel = new Hotel("Hotel Ritz");

//        DoubleRoom doubleroom = new DoubleRoom(103, 200.0);
//        DBOperations.addRoom(doubleroom);
//        DBOperations.removeRoom(101);
//        DoubleRoom singleroom = new DoubleRoom(101, 200.0);
//        DBOperations.addRoom(singleroom);
//        DBOperations.updateRoom(103,200.0, 1);


        Hotel.loadRoomsFromDB();
        this.stage = stage;
    showWelcomeScreen();
    }





    private void showWelcomeScreen() {

        VBox vb = new VBox(30);
        vb.getStyleClass().add("v-box");
        vb.setPadding(new Insets(30));
        vb.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to the Hotel Ritz!");
        welcomeLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: #4eb0e8;");

        Button loginButton = new Button("Login");

        loginButton.getStyleClass().add("button");
        Button registerButton = new Button("New Here? Register with us");

        Button ReviewsButton = new Button("Reviews");



        registerButton.getStyleClass().add("button");



        vb.getChildren().addAll(welcomeLabel, loginButton, registerButton, ReviewsButton);

        StackPane root = new StackPane();
        ImageView bgImageView = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ritz.jpg")))
        );
        bgImageView.setFitWidth(2500);
        bgImageView.setFitHeight(1200);
        bgImageView.setPreserveRatio(false);

        root.getChildren().addAll(bgImageView,  vb);
        scene = new Scene(root , 1525 , 750);
        loginButton.setOnAction(e -> showLoginScreen());
        registerButton.setOnAction(e -> showCustomerInfoPage());
        ReviewsButton.setOnAction(e -> showReviewsPage(scene));
        stage.setScene(scene);
        scene.getStylesheets().add("styles.css");
        stage.setTitle("Home Page");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        stage.show();
    }

    private void showReviewsPage(Scene scene) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Customer Reviews");
        titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: #4eb0e8;");
        titleLabel.setAlignment(Pos.CENTER);

        root.getChildren().add(titleLabel);



        // Fetch reviews from the database

        String query = "SELECT user_id, rating, date, comment FROM ratings";
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                Label noReviewsLabel = new Label("No reviews available.");
                noReviewsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #4eb0e8;");
                root.getChildren().add(noReviewsLabel);
            }
            System.out.println("test before");
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String Username = DBOperations.getUsernameID(userId); // Assuming this method fetches the username based on userId
                int rating = rs.getInt("rating");
                String date = rs.getString("date");
                String comment = rs.getString("comment");

                // Create a review card
                HBox reviewCard = new HBox(10);
                Label userLabel = new Label(Username);
                Label ratingLabel = new Label(" " + rating + " stars");
                Label dateLabel = new Label(date);

                Label commentLabel = new Label(comment);
                userLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #4ed1e8;");
                ratingLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #8eff98;");
                dateLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #9bfffe;");
                commentLabel.setStyle("-fx-font-size: 22 px; -fx-text-fill: #29d7d7;");

                reviewCard.getChildren().addAll(userLabel, ratingLabel, dateLabel);
                VBox ratingBox = new VBox(5);
                ratingBox.getChildren().addAll(reviewCard, commentLabel);
                ratingBox.setAlignment(Pos.CENTER_LEFT);
                ratingBox.setPadding(new Insets(10));
//                ratingBox.setStyle("-fx-background-color: #2a2a40; -fx-border-color: #2a2a30; -fx-border-radius: 8px;");
                ratingBox.getStyleClass().add("rating-box");

                root.getChildren().add(ratingBox);
            }
            System.out.println("test after");
        } catch (SQLException e) {
            showAlert("Database error.");
        }



        Button backButton = new Button("← Back");
        backButton.setMaxWidth(100);
        backButton.setOnAction(e -> showWelcomeScreen());
        root.getChildren().add(backButton);
        scene = new Scene(root , 1525 , 750);
        scene.getStylesheets().add("styles.css");
        stage.setTitle("Customer Reviews");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        stage.show();

    }

    private void showLoginScreen() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Hotel Ritz");
        titleLabel.setPadding(new Insets(0, 0, 50, 0));
        titleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: #4eb0e8;");
        titleLabel.setAlignment(Pos.CENTER);

        Label welcomebackLabel = new Label("Welcome Back to Hotel Ritz!");
        welcomebackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #4eb0e8;");
        welcomebackLabel.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setMaxWidth(300);
        nameField.setPromptText("Enter your name");
        nameField.setStyle("-fx-font-size: 14px; ");

        TextField passwordField = new TextField();
        passwordField.setMaxWidth(300);
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-font-size: 14px;");

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(150);
        loginButton.setStyle(" -fx-text-fill: white; -fx-font-size: 16px;");
        loginButton.setDisable(true);

        loginButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();


            if (name.isEmpty() || password.isEmpty()) {
                showAlert("All fields must be filled.");
                return;
            }

            Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
            if (conn != null) {
                try {
                    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, name);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        showCustomerDashboard(new Customer(name, password));
                    } else {
                        showAlert("Invalid username or password.");
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    showAlert("Database error.");
                }
            } else {
                showAlert("Database connection failed.");
            }
        });


        Button backButton = new Button("← Back");
        backButton.setMaxWidth(100);
        backButton.setOnAction(e -> showWelcomeScreen());

        // Enable only when both fields have values
        ChangeListener<String> listener = (obs, oldVal, newVal) -> {
            loginButton.setDisable(nameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty());
        };
        nameField.textProperty().addListener(listener);
        passwordField.textProperty().addListener(listener);

        root.getChildren().addAll(titleLabel, welcomebackLabel, nameField, passwordField, loginButton, backButton);

        scene = new Scene(root, 1525, 750);
        scene.getStylesheets().add("styles.css");
        stage.setTitle("Home Page");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.setTitle("Login - Hotel Ritz");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }



    private void showCustomerDashboard(Customer customer) {
        boolean hasCheckedIn = fetchCheckInStatus(customer.getUsername()); // DB Code
        Button checkInBtn = new Button("Check-In");
        Button checkOutBtn = new Button("Check-Out");
        Button bookingSummaryBtn = new Button("Booking Summary");
        Button backButton = new Button("← Back");
        backButton.setOnAction(e -> showLoginScreen());
        backButton.setMaxWidth(100);

        checkInBtn.getStyleClass().add("check-in");
        checkOutBtn.getStyleClass().add("check-out");

        // Initial state
        checkInBtn.setDisable(hasCheckedIn);
        checkOutBtn.setDisable(!hasCheckedIn);

        // Check-In Logic
        checkInBtn.setOnAction(e -> {
            checkInBtn.setDisable(true);
            checkOutBtn.setDisable(false);
            showBookingPage(customer);
        });

        // Check-Out Logic
        checkOutBtn.setOnAction(e -> {
            DBOperations.checkOutRoom(DBOperations.getIdUsername(customer.getUsername()));
            customer.setChecked(false);
            showRatingPage(customer);
            checkInBtn.setDisable(false);
            checkOutBtn.setDisable(true);
        });

        // Booking Summary
        bookingSummaryBtn.setOnAction(e -> {
            if (customer.isChecked()) {
                System.out.println("Booking Summary: (Dummy Data)");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Booking Found");
                alert.setHeaderText("You haven't checked in yet.");
                alert.setContentText("Please check in to view your booking summary.");
                alert.showAndWait();
            }
        });

        VBox dashboard = new VBox(20, checkInBtn, checkOutBtn, bookingSummaryBtn, backButton);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setPadding(new Insets(20));
        scene = new Scene(dashboard, 1525, 750);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setTitle("Customer Dashboard");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), dashboard);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        stage.show();
    }

    private  void showRatingPage(Customer customer){
        Stage ratingStage = new Stage();;
        Rating rating = new Rating(customer);
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Rate Your Experience");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: #4eb0e8;");
        TextField ratingField = new TextField();
        ratingField.setMaxWidth(300);
        ratingField.setPromptText("Enter your rating (1-5)");
        ratingField.setStyle("-fx-font-size: 14px; ");
        TextArea commentField = new TextArea();
        commentField.setMaxWidth(400);
        commentField.setMaxHeight(200);
        commentField.setPromptText("Enter your comment");
        commentField.setStyle("-fx-font-size: 14px;");
        Button submitButton = new Button("Submit Rating");
        submitButton.setMaxWidth(150);
        submitButton.setStyle(" -fx-text-fill: white; -fx-font-size: 16px;");
        submitButton.setOnAction(e -> {
            String ratingText = ratingField.getText().trim();
            String commentText = commentField.getText().trim();
            if (ratingText.isEmpty() || commentText.isEmpty()) {
                showAlert("All fields must be filled.");
                return;
            }
            try {
                int ratingValue = Integer.parseInt(ratingText);
                if (ratingValue < 1 || ratingValue > 5) {
                    showAlert("Rating must be between 1 and 5.");
                    return;
                }
                rating.setRating(ratingValue);
                rating.setComment(commentText);
                showAlert("Thank you for your feedback!");
                ratingStage.close();
                int user_id = DBOperations.getIdUsername(customer.getUsername());
                int booking_id = DBOperations.getBooking(user_id);
                DBOperations.insertRating(user_id, booking_id, ratingValue, LocalDate.now(), commentText);
            } catch (NumberFormatException ex) {
                showAlert("Invalid input for rating.");
            }
        });
        ratingStage.setTitle("Rate Your Experience");
        ratingStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        root.getChildren().addAll(titleLabel, ratingField, commentField, submitButton);
        scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("styles.css");
        ratingStage.setScene(scene);
        ratingStage.show();
    }

    private boolean fetchCheckInStatus(String username) {
        boolean checkedIn = false;  // default to not checked-in
        try (Connection conn = DBconnection.connect()) {
            String query = "SELECT checked FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                checkedIn = rs.getInt("checked") == 1;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
           showAlert("Database error.");
        }
        return checkedIn;
    }





    private void showCustomerInfoPage() {

        TextField userField = new TextField();
        userField.setPromptText("New user name");
        userField.setMaxWidth(350);
        TextField passField = new TextField();
        passField.setMaxWidth(350);
        passField.setPromptText("Enter password");

        Customer currentCustomer = new Customer(userField.getText(), passField.getText());





        Button nextButton = new Button("Continue to Booking");
        nextButton.setMaxWidth(250);
        nextButton.setOnAction(e -> {
            String name1 = userField.getText().trim();
            String password1 = passField.getText().trim();

            if (name1.isEmpty() || password1.isEmpty()) {
                showAlert("All fields must be filled.");
                return;
            }

            Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
            if (conn != null) {
                try {
                    // Check if username already exists
                    String checkQuery = "SELECT * FROM users WHERE username = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                    checkStmt.setString(1, name1);
                    ResultSet checkRs = checkStmt.executeQuery();

                    if (checkRs.next()) {
                        showAlert("Username already exists. Please choose a different one.");
                        checkRs.close();
                        checkStmt.close();
                        conn.close();
                        return;
                    }


                    checkRs.close();
                    checkStmt.close();

                    // Insert new user
                    String query = "INSERT INTO users (username, password, checked) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, name1);
                    stmt.setString(2, password1);
                    stmt.setInt(3, 0);
                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                        showCustomerDashboard(currentCustomer);
                    } else {
                        showAlert("Failed to register user.");
                    }

                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Database error.");
                }
            } else {
                showAlert("Database connection failed.");}

        });


        Button backButton = new Button("← Back");
        backButton.setMaxWidth(100);
        backButton.setOnAction(e -> showWelcomeScreen());

        VBox form = new VBox(10, userField, passField, nextButton, backButton);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);
        VBox.setMargin(nextButton, new Insets(10, 20, 10, 20));

        scene = new Scene(form, 1525, 750);
        scene.getStylesheets().add("styles.css");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setTitle("Customer Info");
        stage.setScene(scene);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), form);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        stage.show();
    }

    private void showBookingPage(Customer customer) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        VBox root2 = new VBox(10);
        root2.setPadding(new Insets(20));
        root2.setAlignment(Pos.CENTER);


        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Single", "Double", "Triple", "Suite");
        filterBox.setValue("All");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by room number or bed type...");

        GridPane roomGrid = new GridPane();
        roomGrid.setHgap(10);
        roomGrid.setVgap(10);
        roomGrid.setPadding(new Insets(10));

        ToggleGroup roomToggleGroup = new ToggleGroup();

        Runnable updateGrid = () -> {
            roomGrid.getChildren().clear(); // Clear previous grid content

            String filter = filterBox.getValue();
            String searchText = searchField.getText().toLowerCase();
            int col = 0, row = 0;

            for (Room room : hotel.getAllRooms()) {
                // Filter by Room Type and Search Text
                boolean matchesFilter = filter.equals("All") || room.getClass().getSimpleName().startsWith(filter);
                boolean matchesSearch = String.valueOf(room.getRoomNumber()).contains(searchText);

                if (!matchesFilter || !matchesSearch) continue;
                ToggleButton roomBtn = new ToggleButton("Room " + room.getRoomNumber());
                roomBtn.setMinSize(100, 60);
                roomBtn.setUserData(room);

                roomBtn.setToggleGroup(roomToggleGroup);

                roomBtn.setOnAction(ev -> {
                    for (Toggle toggle : roomToggleGroup.getToggles()) {
                        Room r = (Room) toggle.getUserData();
                        ((ToggleButton) toggle).setStyle(r.isAvailable ?
                                "-fx-background-color: #6fcf97;" : "-fx-background-color: #eb5757;");
                    }
                    roomBtn.setStyle("-fx-background-color: #2d9cdb; -fx-text-fill: white;");
                });
                roomBtn.setStyle(room.isAvailable ? "-fx-background-color: #6fcf97;" : "-fx-background-color: #eb5757;");
                roomBtn.setDisable(!room.isAvailable);
                Tooltip.install(roomBtn, new Tooltip("Price: $" + room.getPricePerNight()));
                roomGrid.add(roomBtn, col++, row);
                if (col == 8) {
                    col = 0;
                    row++;
                }
            }
        };
        syncRoomAvailability();
        updateGrid.run();

        filterBox.setOnAction(e -> updateGrid.run());
        searchField.setOnKeyReleased(e -> updateGrid.run());

        root.getChildren().addAll(
                new Label("Filter by Room Type:"), filterBox,
                new Label("Search:"), searchField,
                roomGrid
        );

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        TextField daysField = new TextField();
        daysField.setPromptText("Number of nights");

        Spinner<Integer> adultsSpinner = new Spinner<>(1, 10, 1);
        Spinner<Integer> childrenSpinner = new Spinner<>(0, 5, 0);

        Label nightsLabel = new Label("Number of Nights");
        Label adultsLabel = new Label("Adults");
        Label childrenLabel = new Label("Children");

        VBox nightsBox = new VBox(10, nightsLabel, daysField);
        nightsBox.setAlignment(Pos.CENTER);
        VBox adultsBox = new VBox(10, adultsLabel, adultsSpinner);
        adultsBox.setAlignment(Pos.CENTER);
        VBox childrenBox = new VBox(10, childrenLabel, childrenSpinner);
        childrenBox.setAlignment(Pos.CENTER);

        Button bookButton = new Button("Book Room");
        HBox bookingBoxContainer = new HBox(10, nightsBox, adultsBox, childrenBox);
        VBox afterBookingBox = new VBox(10, bookingBoxContainer, bookButton);
        bookingBoxContainer.setPadding(new Insets(20));
        afterBookingBox.setAlignment(Pos.CENTER);
        bookingBoxContainer.setAlignment(Pos.CENTER);
        bookButton.getStyleClass().add("modern-button");

        Label confirmationMessage = new Label();

        bookButton.setOnAction(e -> {
            Toggle selectedToggle = roomToggleGroup.getSelectedToggle();
            if (selectedToggle == null) {
                confirmationMessage.setText("⚠ Please select a room.");
                confirmationMessage.setStyle("-fx-text-fill: red;");
                confirmationMessage.setAlignment(Pos.CENTER);
                return;
            }

            try {
                Room selectedRoom = (Room) selectedToggle.getUserData();
                int roomNumber = selectedRoom.getRoomNumber();
                LocalDate start = startDatePicker.getValue();
                int days = Integer.parseInt(daysField.getText().trim());

                if (days <= 0) {
                    confirmationMessage.setText("⚠ Nights must be greater than zero.");
                    confirmationMessage.setStyle("-fx-text-fill: red;");
                    return;
                }

                Booking booking = hotel.bookRoom(customer, selectedRoom, start, days, selectedRoom.getPricePerNight() * days);
                selectedRoom.isAvailable = false;
                selectedRoom.setCustomer(customer);
                customer.setChecked(true);
                DBOperations.bookRoom(DBOperations.getIdUsername(customer.getUsername()), selectedRoom.getRoomNumber(), start, days,selectedRoom.getPricePerNight() * days);
        //        changeCheckInStatus(customer.getUsername(), true); // DB Code
                if (booking != null) {
                    confirmationMessage.setText("");
                    double totalCost = selectedRoom.getPricePerNight() * days;
                    LocalDate checkoutDate = start.plusDays(days);

                    // Color code the confirmation message
                    TextFlow confirmationTextFlow = new TextFlow();
                    confirmationTextFlow.setTextAlignment(TextAlignment.CENTER);
                    confirmationTextFlow.setStyle("-fx-font-size: 14px;");

                    Text bookingText = new Text("✅ Booked ");
                    bookingText.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(bookingText);

                    Text roomText = new Text(" Room " + roomNumber);
                    roomText.setStyle("-fx-fill: #6fcf97;");
                    confirmationTextFlow.getChildren().add(roomText);

                    Text successText = new Text(" successfully for ");
                    successText.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(successText);

                    Text nightsText = new Text(days + " night(s)");
                    nightsText.setStyle("-fx-fill: gold;");
                    confirmationTextFlow.getChildren().add(nightsText);

                    Text preTotalText = new Text("\n💰 Total Cost:");
                    preTotalText.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(preTotalText);

                    Text totalCostText = new Text(" $" + totalCost);
                    totalCostText.setStyle("-fx-fill: #6fcf97;");
                    confirmationTextFlow.getChildren().add(totalCostText);

                    Text adultsTextLabel = new Text("\n👤 Adults: ");
                    adultsTextLabel.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(adultsTextLabel);
                    Text adultsText = new Text(" " + adultsSpinner.getValue());
                    adultsText.setStyle("-fx-fill: #2d36db;");
                    confirmationTextFlow.getChildren().add(adultsText);

                    Text childrenTextLabel = new Text("\n👶 Children: ");
                    childrenTextLabel.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(childrenTextLabel);
                    Text childrenText = new Text(" " + childrenSpinner.getValue());
                    childrenText.setStyle("-fx-fill: #2d36db;");
                    confirmationTextFlow.getChildren().add(childrenText);

                    Text startText = new Text("\n📅 Start Date: ");
                    startText.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(startText);

                    Text startDateText = new Text(" " + start);
                    startDateText.setStyle("-fx-fill: #2d9cdb;");
                    confirmationTextFlow.getChildren().add(startDateText);

                    Text checkoutText = new Text("\n📅 Checkout Date: ");
                    checkoutText.setStyle("-fx-fill: white;");
                    confirmationTextFlow.getChildren().add(checkoutText);

                    Text checkoutDateText = new Text(" " + checkoutDate);
                    checkoutDateText.setStyle("-fx-fill: gold;");
                    confirmationTextFlow.getChildren().add(checkoutDateText);

                    VBox confirmationBox = new VBox(10, confirmationTextFlow);
                    confirmationBox.setAlignment(Pos.CENTER);


                    confirmationMessage.setGraphic(confirmationTextFlow);

                    Button goToDashboardButton = new Button("Go to Dashboard");
                    goToDashboardButton.setOnAction(ev -> {
                        showCustomerDashboard(customer);
                    });
                    VBox vAfterBookingBox = new VBox(10, confirmationMessage, goToDashboardButton);
                    vAfterBookingBox.setAlignment(Pos.CENTER);
                    afterBookingBox.getChildren().clear();
                    afterBookingBox.getChildren().add(vAfterBookingBox);

                    updateGrid.run();
                }
            } catch (NumberFormatException ex) {
                confirmationMessage.setText("⚠ Invalid input for nights.\n");
                confirmationMessage.setStyle("-fx-text-fill: red;");
            }
        });

        VBox.setVgrow(confirmationMessage, Priority.ALWAYS);
        root2.getChildren().addAll(
                new Label("Booking Information"),
                new Label("Check-in Date:"), startDatePicker,
                afterBookingBox
        );


        VBox root3 = new VBox(10);
        root3.setPadding(new Insets(20));
        root3.getChildren().addAll(root, root2);

        confirmationMessage.setMaxWidth(Double.MAX_VALUE/2);




        scene = new Scene(root3, 1525, 750);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setTitle("Room Booking - Hotel Ritz");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root3);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        stage.show();
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
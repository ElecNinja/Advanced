
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

public class Main extends Application {
    private Stage stage;
    private Hotel hotel;
    @Override
    public void start(Stage stage) throws IOException {
        this.hotel = new Hotel("Hotel Ritz");

//        DBOperations.addRoom(new SingleRoom(104, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(105, 2000.0));
//        DBOperations.addRoom(new TripleRoom(106, 3000.0));
//        DBOperations.addRoom(new SingleRoom(107, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(108, 2000.0));
//        DBOperations.addRoom(new TripleRoom(109, 3000.0));
//        DBOperations.addRoom(new SingleRoom(110, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(111, 2000.0));
//        DBOperations.addRoom(new TripleRoom(112, 3000.0));
//        DBOperations.addRoom(new SingleRoom(113, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(114, 2000.0));
//        DBOperations.addRoom(new TripleRoom(115, 3000.0));
//        DBOperations.addRoom(new SingleRoom(201, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(202, 2000.0));
//        DBOperations.addRoom(new TripleRoom(203, 3000.0));
//        DBOperations.addRoom(new SingleRoom(301, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(302, 2000.0));
//        DBOperations.addRoom(new TripleRoom(303, 3000.0));
//        DBOperations.addRoom(new SingleRoom(401, 1000.0));
//        DBOperations.addRoom(new DoubleRoom(402, 2000.0));
//        DBOperations.addRoom(new TripleRoom(403, 3000.0));
//        DBOperations.addRoom(new SingleRoom(501, 1000.0));
//
//        DBOperations.updateRoom(101, 1000.0, 1);
//        DBOperations.updateRoom(102, 2000.0, 1);
//        DBOperations.updateRoom(103, 3000.0, 1);

//        DBOperations.removeRoom(501);


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

        registerButton.getStyleClass().add("button");

        loginButton.setOnAction(e -> showLoginScreen());
        registerButton.setOnAction(e -> showCustomerInfoPage());

        vb.getChildren().addAll(welcomeLabel, loginButton, registerButton);

        StackPane root = new StackPane();
        ImageView bgImageView = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ritz.jpg")))
        );
        bgImageView.setFitWidth(1700);
        bgImageView.setFitHeight(875);
        bgImageView.setPreserveRatio(false);

        root.getChildren().addAll(bgImageView,  vb);
        Scene s3 = new Scene(root , 1525 , 750);
        stage.setScene(s3);
        s3.getStylesheets().add("styles.css");
        stage.setTitle("Home Page");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
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

        Scene s3 = new Scene(root, 1525, 750);
        s3.getStylesheets().add("styles.css");
        stage.setTitle("Home Page");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setScene(s3);
        stage.setTitle("Login - Hotel Ritz");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }



    private void showCustomerDashboard(Customer customer) {
       // boolean hasCheckedIn = fetchCheckInStatus(customer.getUsername()); // DB Code
        boolean hasCheckedIn = customer.isChecked();
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
            // You could later add logic here to save the booking
        });

        // Check-Out Logic
        checkOutBtn.setOnAction(e -> {
            changeCheckInStatus(customer.getUsername(), false);
            customer.setChecked(false);
            showRatingPage(customer);
            checkInBtn.setDisable(false);
            checkOutBtn.setDisable(true);
        });

        Label titleLabel = new Label("Dashboard");
        titleLabel.setPadding(new Insets(0, 0, 50, 0));
        titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: #4eb0e8;");


        VBox dashboard = new VBox(20, titleLabel, checkInBtn, checkOutBtn, backButton);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setPadding(new Insets(20));
        Scene scene = new Scene(dashboard, 1525, 750);
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
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Rate Your Experience");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: #4eb0e8;");
        TextField ratingField = new TextField();
        ratingField.setMaxWidth(300);
        ratingField.setPromptText("Enter your rating (1-5)");
        ratingField.setStyle("-fx-font-size: 14px; ");
        TextArea commentField = new TextArea();
        commentField.setMaxWidth(300);
        commentField.setMaxHeight(100);
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
//           DB Code TODO     // Save the rating to the database
//                try (Connection conn = DBconnection.connect()) {
//                    String query = "INSERT INTO ratings (username, rating, comment) VALUES (?, ?, ?)";
//                    PreparedStatement stmt = conn.prepareStatement(query);
//                    stmt.setString(1, customer.getUsername());
//                    stmt.setInt(2, ratingValue);
//                    stmt.setString(3, commentText);
//                    stmt.executeUpdate();
//                    stmt.close();
//                } catch (SQLException ex) {
//                    showAlert("Database error.");
//                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid input for rating.");
            }
        });
        ratingStage.setTitle("Rate Your Experience");
        ratingStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        root.getChildren().addAll(titleLabel, ratingField, commentField, submitButton);
        Scene scene = new Scene(root, 500, 500);
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

    private boolean changeCheckInStatus(String username, boolean checkIn) {
        try (Connection conn = DBconnection.connect()) {
            String query = "UPDATE users SET checked = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, checkIn ? 1 : 0);
            stmt.setString(2, username);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
           showAlert("Database error.");
        }
        return false;
    }





    private void showCustomerInfoPage() {

        Label titleLabel = new Label("Register with us today!");
        titleLabel.setPadding(new Insets(0, 0, 50, 0));
        titleLabel.setStyle("-fx-font-size: 35px; -fx-text-fill: #4eb0e8;");

        TextField userField = new TextField();
        userField.setPromptText("New user name");
        TextField passField = new TextField();
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

        VBox form = new VBox(10, titleLabel, userField, passField, nextButton, backButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setMaxWidth(400);
        VBox.setMargin(backButton, new Insets(10, 20, 10, 20));

        Scene scene = new Scene(form, 1525, 750);
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
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Page Title
        Label titleLabel = new Label("Room Booking");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Filter Section
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER);

        ComboBox<String> roomTypeFilter = new ComboBox<>();
        roomTypeFilter.getItems().addAll("All", "Single", "Double", "Triple");
        roomTypeFilter.setValue("All");
        roomTypeFilter.setPrefWidth(120);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by room number...");
        searchField.setPrefWidth(200);

        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        filterBox.getChildren().addAll(
                new Label("Filter by:"), roomTypeFilter,
                new Label("Search:"), searchField,
                resetButton
        );

        // Rooms Grid
        GridPane roomsGrid = new GridPane();
        roomsGrid.setHgap(15);
        roomsGrid.setVgap(15);
        roomsGrid.setAlignment(Pos.CENTER);
        roomsGrid.setPadding(new Insets(20));

        // Booking Form
        VBox bookingForm = new VBox(15);
        bookingForm.setAlignment(Pos.CENTER);
        bookingForm.setPadding(new Insets(20));
        bookingForm.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-background-radius: 10;");

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        TextField nightsField = new TextField();
        nightsField.setPromptText("Number of nights");

        Spinner<Integer> adultsSpinner = new Spinner<>(1, 10, 1);
        Spinner<Integer> childrenSpinner = new Spinner<>(0, 5, 0);

        Button bookButton = new Button("Confirm Booking");
        bookButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox formFields = new HBox(15);
        formFields.setAlignment(Pos.CENTER);
        formFields.getChildren().addAll(
                createFormField("Check-in Date", startDatePicker),
                createFormField("Nights", nightsField),
                createFormField("Adults", adultsSpinner),
                createFormField("Children", childrenSpinner)
        );
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

        bookingForm.getChildren().addAll(formFields, bookButton);

        // Add components to main container
        mainContainer.getChildren().addAll(titleLabel, filterBox, roomsGrid, bookingForm);

        // Event Handlers
        roomTypeFilter.setOnAction(e -> updateRoomsDisplay(roomsGrid, roomTypeFilter.getValue(), searchField.getText()));
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                updateRoomsDisplay(roomsGrid, roomTypeFilter.getValue(), newVal));
        resetButton.setOnAction(e -> {
            roomTypeFilter.setValue("All");
            searchField.clear();
        });

        bookButton.setOnAction(e -> handleBooking(
                customer, roomsGrid,
                startDatePicker.getValue(),
                nightsField.getText(),
                adultsSpinner.getValue(),
                childrenSpinner.getValue()
        ));

        // Initial rooms display
        updateRoomsDisplay(roomsGrid, "All", "");

        Scene scene = new Scene(mainContainer, 1525, 750);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFormField(String label, Control field) {
        VBox container = new VBox(5);
        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-font-weight: bold;");
        container.getChildren().addAll(fieldLabel, field);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private void updateRoomsDisplay(GridPane grid, String filter, String searchText) {
        grid.getChildren().clear();

        int col = 0, row = 0;
        for (Room room : hotel.getAllRooms()) {
            if (matchesFilter(room, filter) && matchesSearch(room, searchText)) {
                ToggleButton roomBtn = createRoomButton(room);
                grid.add(roomBtn, col, row);

                if (++col == 4) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private boolean matchesFilter(Room room, String filter) {
        return filter.equals("All") ||
                (filter.equals("Single") && room instanceof SingleRoom) ||
                (filter.equals("Double") && room instanceof DoubleRoom) ||
                (filter.equals("Triple") && room instanceof TripleRoom);
    }

    private boolean matchesSearch(Room room, String searchText) {
        return String.valueOf(room.getRoomNumber()).contains(searchText);
    }

    private ToggleButton createRoomButton(Room room) {
        ToggleButton btn = new ToggleButton();
        btn.setMinSize(150, 120);
        btn.setMaxSize(150, 120);

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);

        Label number = new Label("Room " + room.getRoomNumber());
        number.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label type = new Label(getRoomTypeName(room));
        type.setStyle("-fx-font-size: 14;");

        Label price = new Label("$" + room.getPricePerNight() + "/night");
        price.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        content.getChildren().addAll(number, type, price);
        btn.setGraphic(content);

        // Style by room type
        String style = "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; ";
        if (room instanceof SingleRoom) {
            btn.setStyle(style + "-fx-background-color: #e3f2fd; -fx-border-color: #bbdefb;");
        }
        else if (room instanceof DoubleRoom) {
            btn.setStyle(style + "-fx-background-color: #e8f5e9; -fx-border-color: #c8e6c9;");
        }
        else if (room instanceof TripleRoom) {
            btn.setStyle(style + "-fx-background-color: #fff3e0; -fx-border-color: #ffe0b2;");
        }

        btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                btn.setStyle(btn.getStyle() + "-fx-border-color: #3498db;");
            } else {
                btn.setStyle(btn.getStyle().replace("-fx-border-color: #3498db;",
                        room instanceof SingleRoom ? "-fx-border-color: #bbdefb;" :
                                room instanceof DoubleRoom ? "-fx-border-color: #c8e6c9;" :
                                        "-fx-border-color: #ffe0b2;"));
            }
        });

        return btn;
    }

    private String getRoomTypeName(Room room) {
        if (room instanceof SingleRoom) return "Single";
        if (room instanceof DoubleRoom) return "Double";
        if (room instanceof TripleRoom) return "Triple";
        return "";
    }

    private void handleBooking(Customer customer, GridPane roomsGrid,
                               LocalDate startDate, String nightsText,
                               int adults, int children) {
        try {
            int nights = Integer.parseInt(nightsText);
            if (nights <= 0) {
                showAlert("Error", "Number of nights must be greater than zero");
                return;
            }

            // Actual booking logic would go here
            showAlert("Success", "Room booked successfully");
            updateRoomsDisplay(roomsGrid, "All", "");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number of nights");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}

package advanced.prog.project;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {
    private Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
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
        s3.setFill(Color.rgb(42,42,62)); // This is the line that was added to fix the issue
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
        passwordField.setPromptText("Enter your email");
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
                        showCustomerDashboard();
                    } else {
                        showAlert("Invalid username or password.");
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
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

    private boolean hasCheckedIn = false; // Default to false, updated after check-in

    private void showCustomerDashboard() {
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
            hasCheckedIn = true;
            checkInBtn.setDisable(true);
            checkOutBtn.setDisable(false);
            // You could later add logic here to save the booking
        });

        // Check-Out Logic
        checkOutBtn.setOnAction(e -> {
            hasCheckedIn = false;
            checkInBtn.setDisable(false);
            checkOutBtn.setDisable(true);
        });

        // Booking Summary
        bookingSummaryBtn.setOnAction(e -> {
            if (hasCheckedIn) {
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




    private void showCustomerInfoPage() {
        TextField userField = new TextField();
        userField.setPromptText("New user name");
        TextField passField = new TextField();
        passField.setPromptText("Enter password");





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
                    String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, name1);
                    stmt.setString(2, password1);
                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                        showCustomerDashboard();
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
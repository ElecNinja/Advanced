package advanced.prog.project;

import advanced.prog.project.models.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBOperations {


    public static void addRoom(Room room) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, room.getRoomNumber());
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next()) {
                    showAlert("Room number already exists. Please choose a different one.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "INSERT INTO rooms (room_id, price, type) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, room.getRoomNumber());
                stmt.setDouble(2, room.getPricePerNight());
                stmt.setString(3, room.getRoomType());
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Room added successfully.");
                    Hotel.addRoom(room);
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

    }

    public static void removeRoom(int roomNumber) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, roomNumber);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Room number does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "DELETE FROM rooms WHERE room_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, roomNumber);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Room removed successfully.");
                } else {
                    showAlert("Failed to remove room.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }

    public static void updateRoom(int roomNumber, double price, int available) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, roomNumber);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Room number does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "UPDATE rooms SET price = ?, available = ? WHERE room_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setDouble(1, price);
                stmt.setInt(2, available);
                stmt.setInt(3, roomNumber);
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Room updated successfully.");
                } else {
                    showAlert("Failed to update room.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }

    public static void getRoom(int roomNumber) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, roomNumber);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Room number does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, roomNumber);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    showAlert("Room number: " + rs.getInt("room_id") + ", Price: " + rs.getDouble("price"));
                } else {
                    showAlert("Failed to retrieve room.");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }

    public static List<Room> getAllRoomsFromDB() {
        List<Room> roomList = new ArrayList<>();
        Connection conn = DBconnection.connect();

        if (conn != null) {
            try {
                String query = "SELECT * FROM rooms";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int roomNumber = rs.getInt("room_id");
                    double price = rs.getDouble("price");
                    String type = rs.getString("type");

                    Room room = null;

                    switch (type) {
                        case "Single":
                            room = new SingleRoom(roomNumber, price);
                            break;
                        case "Double":
                            room = new DoubleRoom(roomNumber, price);
                            break;
                        case "Triple":
                            room = new TripleRoom(roomNumber, price);
                            break;
                        default:
                            System.err.println("Unknown room type: " + type);
                            continue;
                    }

                    roomList.add(room);
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return roomList;
    }



    public static void bookRoom(int user_id, int roomNumber, LocalDate startDate, int nights, double totalCost) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, roomNumber);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Room number does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }
                else if (!checkRs.getBoolean("available")) {
                    showAlert("Room is not available.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                // Set user checked in
                String updateUserQuery = "UPDATE users SET checked = 1 WHERE user_id = ?";
                PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
                updateUserStmt.setInt(1, user_id);
                updateUserStmt.executeUpdate();
                updateUserStmt.close();

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "INSERT INTO bookings (user_id, room_id, start_date, nights, total_cost) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, user_id);
                stmt.setInt(2, roomNumber);
                stmt.setString(3, startDate.toString());
                stmt.setInt(4, nights);
                stmt.setDouble(5, totalCost);
                int rowsInserted = stmt.executeUpdate();

                // Update room availability
                String updateQuery = "UPDATE rooms SET available = 0 WHERE room_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, roomNumber);
                updateStmt.executeUpdate();
                updateStmt.close();

                if (rowsInserted > 0) {
                    showAlert("Room booked successfully.");
                } else {
                    showAlert("Failed to book room.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }
    public static boolean isAvailableRoom(int roomNumber) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        boolean isAvailable = false;
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM rooms WHERE room_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, roomNumber);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Room number does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return false;
                }

                isAvailable = checkRs.getBoolean("available");

                checkRs.close();
                checkStmt.close();
                conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}
        return isAvailable;

    }

    public static int getBooking(int user_id) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        int booking_id = 0;
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM bookings WHERE user_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, user_id);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("No bookings found for this user.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "SELECT * FROM bookings WHERE user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();

                booking_id = rs.getInt("booking_id");


                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}
        return booking_id;

    }

    public static int getIdUsername(String username) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        int user_id = 0;
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM users WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, username);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Username does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return -1;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "SELECT * FROM users WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    user_id = rs.getInt("user_id");
                } else {
                    showAlert("Failed to retrieve user.");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}
        return user_id;

    }
    public static String getUsernameID(int user_id) {

        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        String username = "";
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM users WHERE user_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, user_id);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("User ID does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return username;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "SELECT * FROM users WHERE user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    username = rs.getString("username");
                } else {
                    showAlert("Failed to retrieve user.");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}
        return username;

    }

    public static void insertRating(int user_id, int booking_id, int rating,  LocalDate date, String comment) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM ratings WHERE user_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setInt(1, user_id);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("No ratings found for this user.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "INSERT INTO ratings (user_id, booking_id, rating, date, comment) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, user_id);
                stmt.setInt(2, booking_id);
                stmt.setInt(3, rating);
                stmt.setString(4, date.toString());
                stmt.setString(5, comment);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Rating added successfully.");
                } else {
                    showAlert("Failed to add rating.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }

    public static void checkOutRoom(int user_id) {
        Connection conn = DBconnection.connect();  // Call the connect method from DBconnection
        if (conn != null) {
            try {
                // Check if username already exists
                String checkQuery = "SELECT * FROM bookings WHERE booking_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                int booking_id = getBooking(user_id);
                checkStmt.setInt(1, booking_id);
                ResultSet checkRs = checkStmt.executeQuery();

                if (!checkRs.next()) {
                    showAlert("Booking ID does not exist.");
                    checkRs.close();
                    checkStmt.close();
                    conn.close();
                    return;
                }

                // Set room availability
                String updateQuery = "UPDATE rooms SET available = 1 WHERE room_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, checkRs.getInt("room_id"));
                updateStmt.executeUpdate();
                updateStmt.close();

                // set user checked out
                String updateUserQuery = "UPDATE users SET checked = 0 WHERE user_id = ?";
                PreparedStatement updateUserStmt = conn.prepareStatement(updateUserQuery);
                updateUserStmt.setInt(1, checkRs.getInt("user_id"));
                updateUserStmt.executeUpdate();
                updateUserStmt.close();


                checkRs.close();
                checkStmt.close();

                // Insert new user
                String query = "DELETE FROM bookings WHERE booking_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, booking_id);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Room checked out successfully.");
                } else {
                    showAlert("Failed to check out room.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Database error.");
            }
        } else {
            showAlert("Database connection failed.");}

    }


    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    }



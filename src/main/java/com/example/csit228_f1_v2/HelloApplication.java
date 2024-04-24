package com.example.csit228_f1_v2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    static Integer CurrentUID;
    static Stage myONlyStage;
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
        myONlyStage = stage;

        //Create Table if not exist
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "password VARCHAR(100) NOT NULL)";
        try(Connection c = MySqlConnection.getConnection()){
            Statement statement = c.createStatement();
            statement.execute(query);

            //create another table if not exists
            query = "CREATE TABLE IF NOT EXISTS PlayerClan (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "number_of_wins INT DEFAULT 0," +
                    "number_of_losses INT DEFAULT 0," +
                    "SamuraiName VARCHAR(50) NOT NULL," +
                    "Owner INT," +
                    "FOREIGN KEY (Owner) REFERENCES users(id)" +
                    ")";
            statement.execute(query);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        //I will keep this for educational purposes
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        Text txtWelcome = new Text("Welcome to CIT");
//        txtWelcome.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 69));
//        txtWelcome.setFill(Color.RED);
////        grid.setAlignment();
//        grid.setPadding(new Insets(20));
////        grid.
//        txtWelcome.setTextAlignment(TextAlignment.CENTER);
//        grid.add(txtWelcome, 0, 0, 3, 1);
//
//        Label lbUsername = new Label("Username: ");
//        lbUsername.setTextFill(Color.LIGHTSKYBLUE);
//        lbUsername.setFont(Font.font(30));
//        grid.add(lbUsername, 0, 1);
//
//        TextField tfUsername = new TextField();
//        grid.add(tfUsername, 1, 1);
//        tfUsername.setFont(Font.font(30));
////        tfUsername.setMaxWidth(150);
//
//        Label lbPassword = new Label("Password");
//        lbPassword.setFont(Font.font(30));
//        lbPassword.setTextFill(Color.CHARTREUSE);
//        grid.add(lbPassword, 0, 2);
//
//        PasswordField pfPassword = new PasswordField();
//        pfPassword.setFont(Font.font(30));
//        grid.add(pfPassword, 1, 2);
//
//        TextField tmpPassword = new TextField(pfPassword.getText());
//        tmpPassword.setFont(Font.font(30));
//        grid.add(tmpPassword, 1, 2);
//        tmpPassword.setVisible(false);
//
//        ToggleButton btnShow = new ToggleButton("( )");
////        btnShow.setOnAction(new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent actionEvent) {
////                if (btnShow.isSelected()) {
////                    tmpPassword.setText(pfPassword.getText());
////                    tmpPassword.setVisible(true);
////                } else {
////                    tmpPassword.setVisible(false);
////                    pfPassword.setText(tmpPassword.getText());
////                }
////            }
////        });
//        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                tmpPassword.setText(pfPassword.getText());
//                tmpPassword.setVisible(true);
//            }
//        });
//
//        EventHandler<MouseEvent> release = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                tmpPassword.setVisible(false);
//                pfPassword.setText(tmpPassword.getText());
//            }
//        };
//
//        btnShow.setOnMouseReleased(release);
//        btnShow.setOnMouseExited(release);
//        grid.add(btnShow, 2,2);
//
//        Button btnLogin = new Button("Log In");
//        btnLogin.setFont(Font.font(40));
//        grid.add(btnLogin, 0, 3, 2, 1);
//
//        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                //read password
//                try (Connection c = MySqlConnection.getConnection();
//                     PreparedStatement statement = c.prepareStatement(
//                             "SELECT * FROM users WHERE name=? AND password=?")){
//
//                    String username = tfUsername.getText();
//                    String password = pfPassword.getText();
//                    statement.setString(1 , username);
//                    statement.setString(2, password);
//                    ResultSet set = statement.executeQuery();
//                    if(set.next()){
//                        CurrentUID = set.getInt(1);
//                        txtWelcome.setText("Welcome " + set.getString(2));
//                        Parent p = FXMLLoader.load(getClass().getResource("homepage.fxml")); //throws IOException
//                        Scene s = new Scene(p);
//                        myONlyStage.setScene(s);
//                        myONlyStage.show();
//                    }else{
//                        System.out.println("Invalid Credentials");
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        //Create Sign button
//        Button btnSignIn = new Button("Sign In");
//        btnSignIn.setFont(Font.font(40));
//        grid.add(btnSignIn, 1, 3, 2, 1);
//
//        btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                int userID = 0;
//                //createAccount
//                try (Connection c = MySqlConnection.getConnection();
//                     PreparedStatement statement = c.prepareStatement(
//                             "INSERT INTO users (name, password) VALUE (?, ?)",
//                             PreparedStatement.RETURN_GENERATED_KEYS
//                     )){
//
//                    String username = tfUsername.getText();
//                    String password = pfPassword.getText();
//                    statement.setString(1 , username);
//                    statement.setString(2, password);
//                    int rows = statement.executeUpdate();
//
//                    //get id of new user
//                    if(rows == 1){
//                        ResultSet newUserID = statement.getGeneratedKeys();
//
//                        if (newUserID.next()) {
//                            userID = newUserID.getInt(1);
//                        }
//                    }
//                }catch(SQLException e){
//                    e.printStackTrace();
//                }
//
//                //initialize the player's account
//                try (Connection c = MySqlConnection.getConnection();
//                     PreparedStatement statement = c.prepareStatement(
//                             "INSERT INTO playerclan (SamuraiUnit, owner) VALUE (?, ?)"
//                     )){
//
//                    statement.setString(1 , "Yari");
//                    statement.setInt(2, userID);
//                    statement.execute();
//                }catch(SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        });

        //end of my stuff
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        myONlyStage.setScene(scene);
        myONlyStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
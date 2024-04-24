package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static com.example.csit228_f1_v2.HelloApplication.CurrentUID;
import static com.example.csit228_f1_v2.HelloApplication.myONlyStage;

public class HelloController {
    public GridPane pnLogin;
    public AnchorPane pnMain;
    public VBox pnHome;
    public TextField tfUsername;
    public PasswordField pfPassword;

    @FXML
    protected void onSigninClick() throws IOException {
        int userID = 0;
            //createAccount
            try (Connection c = MySqlConnection.getConnection();
                 PreparedStatement statement = c.prepareStatement(
                         "INSERT INTO users (name, password) VALUE (?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS
                 )){

                String username = tfUsername.getText();
                String password = pfPassword.getText();
                statement.setString(1 , username);
                statement.setString(2, password);
                int rows = statement.executeUpdate();

                //get id of new user
                if(rows == 1){
                    ResultSet newUserID = statement.getGeneratedKeys();

                    if (newUserID.next()) {
                        userID = newUserID.getInt(1);
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            //initialize the player's account
            try (Connection c = MySqlConnection.getConnection();
                 PreparedStatement statement = c.prepareStatement(
                         "INSERT INTO playerclan (SamuraiUnit, owner) VALUE (?, ?)"
                 )){

                statement.setString(1 , "Yari");
                statement.setInt(2, userID);
                statement.execute();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText(null);
                alert.setContentText("You can now log in with the new account.");
                alert.show();
            }catch(SQLException e){
                e.printStackTrace();
            }
    }


    public void onLogin(){
        //read password
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM users WHERE name=? AND password=?")){

            String username = tfUsername.getText();
            String password = pfPassword.getText();
            statement.setString(1 , username);
            statement.setString(2, password);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                CurrentUID = set.getInt(1);
                Parent p = FXMLLoader.load(getClass().getResource("homepage.fxml")); //throws IOException
                Scene s = new Scene(p);
                myONlyStage.setScene(s);
                myONlyStage.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Credentials");
                alert.setHeaderText(null);
                alert.setContentText("Please type a valid username and password");
                alert.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
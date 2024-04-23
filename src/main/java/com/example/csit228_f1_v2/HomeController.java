package com.example.csit228_f1_v2;

import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HomeController {

    public ToggleButton tbNight;
    public ProgressIndicator piProgress;
    public Slider slSlider;
    public ProgressBar pbProgress;
    public TextField tfNewUsername;
    public Button btnDeploy;
    public Button btnAttack;

    public RadioButton rbtnKatana;
    public RadioButton rbtnYari;
    public RadioButton rbtnCav;

// TODO: add RadioButtons to scene


    public void onDeploy() {
        System.out.println("Deploying ...");
    }

    public void onChangeUsername(){
        int uid = HelloApplication.CurrentUID.intValue();
        try(Connection c = MySqlConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE users SET name=? WHERE id=?"
            )){
            String newName = tfNewUsername.getText();
            int id = uid;
            statement.setString(1, newName);
            statement.setInt(2 , id);

            int rows = statement.executeUpdate();
            System.out.println("Rows updated: " + rows);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

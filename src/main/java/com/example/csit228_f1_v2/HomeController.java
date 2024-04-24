package com.example.csit228_f1_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.csit228_f1_v2.HelloApplication.CurrentUID;
import static com.example.csit228_f1_v2.HelloApplication.myONlyStage;

public class HomeController {
    public TextField tfNewUsername;
    public Button btnDeploy;
    public Button btnAttack;
    public TextArea txtaReportLog;
    public TextFlow txtfReportLog;
    public ChoiceBox<String> cmbAttackList;

    public RadioButton rbtnKatana;
    public RadioButton rbtnYari;
    public RadioButton rbtnCav;
    public ToggleGroup SamuraiType;
    private String CurrentSamuraiUnit;

    //Read:
    public void initialize(){
        //initialize


        //populate Choice box
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT id, name FROM users WHERE id!=?")){

            statement.setInt(1, CurrentUID);
            ResultSet set = statement.executeQuery();

            ObservableList<String> users = FXCollections.observableArrayList();
            String username;
            while(set.next()){
                username = set.getString(2);
                users.add(username);
            }
            cmbAttackList.setItems(users);
        }catch (SQLException e){
            e.printStackTrace();
        }

        //set current Samurai Unit
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT SamuraiUnit FROM playerclan WHERE owner=?")){

            statement.setInt(1, CurrentUID);
            ResultSet set = statement.executeQuery();

            if(set.next()){
                CurrentSamuraiUnit = set.getString(1);

                Text CurrSamuraimsg = new Text("Current Samurai: "+ CurrentSamuraiUnit + "\n");
                CurrSamuraimsg.setStyle("-fx-fill: BLACK;-fx-font-weight:Bold;");
                txtfReportLog.getChildren().add(CurrSamuraimsg);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //Update: SamuraiUnit
    public void onDeploy() {
        RadioButton selectedRadioButton = (RadioButton) SamuraiType.getSelectedToggle();

        if (selectedRadioButton != null) {
            String selectedSamurai = selectedRadioButton.getText();
            CurrentSamuraiUnit = selectedSamurai;
            selectedSamurai = "Deploying: "+ selectedSamurai + "\n";

            //output to Text
            Text Deploymsg = new Text(selectedSamurai);
            Deploymsg.setStyle("-fx-fill: BLACK;-fx-font-weight:Bold;");
            txtfReportLog.getChildren().add(Deploymsg);

        } else {
            System.out.println("No weapon selected");
        }

        //update database
        try(Connection c = MySqlConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE playerclan SET SamuraiUnit=? WHERE owner=?")){

            c.setAutoCommit(false);

            String selectedSamurai = selectedRadioButton.getText();
            statement.setString(1, selectedSamurai);
            statement.setInt(2, CurrentUID);
            statement.execute();

            c.commit();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Samurai Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a samurai.");

            // Display the alert dialog
            alert.showAndWait();
        }

    }
    //Update: numberofWins or numberofLosses, Read: SamuraiUnit from EnemyPlayer
    public void onAttack() {
        String SelectedPlayer = cmbAttackList.getValue();
        String EnemySamurai;
        int winCondition = 0;
        int Eid;
        int selectedIndex = cmbAttackList.getSelectionModel().getSelectedIndex();
        if(SelectedPlayer == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Player Selected");
            alert.setContentText("Please select a player to attack");
            alert.showAndWait();
            return;
        }

        //check who wins
        //Step 1:find player
        try(Connection c = MySqlConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT id FROM users WHERE name=?")){

            statement.setString(1, SelectedPlayer);
            ResultSet playertoAttack = statement.executeQuery(); //the result set closes????? okay
            if(playertoAttack.next()){ //always put this first lolol...can you go back from next()?
                Eid = playertoAttack.getInt(1);
            }else{
                throw new SQLException("Player not found");
            }

        }catch (SQLException e){
            e.printStackTrace();
            return;
        }

        //step 2: Get other player unit
        try(Connection c = MySqlConnection.getConnection();
        PreparedStatement statement = c.prepareStatement(
                "SELECT SamuraiUnit FROM playerclan WHERE owner=?")){

                statement.setInt(1, Eid);
                ResultSet FoundSamurai = statement.executeQuery();
                if(FoundSamurai.next()){
                    EnemySamurai = FoundSamurai.getString(1);
                }else{
                    throw new SQLException("Samurai Unit not found");
                }
        }catch (SQLException e){
            e.printStackTrace();
            return;
        }

        //Step 3: compare and Update win/lose
        winCondition = compareUnit(EnemySamurai);
        try(Connection c = MySqlConnection.getConnection();
        PreparedStatement statement = c.prepareStatement(
                "UPDATE playerclan SET number_of_wins = number_of_wins + ?, number_of_losses = number_of_losses + ? WHERE owner=? ")){

            c.setAutoCommit(false);

            Text Resultmsg = new Text();
            statement.setInt(1,0);
            statement.setInt(2,0);
            statement.setInt(3, CurrentUID);
            switch(winCondition){
                case 1:
                    statement.setInt(1,1);
                    Resultmsg.setText("Your " + CurrentSamuraiUnit + " won against " + SelectedPlayer + "\n");
                    Resultmsg.setStyle("-fx-fill: GREEN;-fx-font-weight:normal;");
                    break;
                case -1:
                    statement.setInt(2,1);
                    Resultmsg.setText("Your " + CurrentSamuraiUnit + " lost against " + SelectedPlayer + "\n");
                    Resultmsg.setStyle("-fx-fill: RED;-fx-font-weight:normal;");
                    break;
                case 0:
                    Resultmsg.setText("Your " + CurrentSamuraiUnit + " tied against " + SelectedPlayer + "\n");
                    Resultmsg.setStyle("-fx-fill: ORANGE;-fx-font-weight:normal;");
                    break;
            }
            txtfReportLog.getChildren().add(Resultmsg);
            statement.execute();

            c.commit();
        }catch (SQLException e){
            e.printStackTrace();
            return;
        }
        cmbAttackList.getItems().remove(selectedIndex);
        cmbAttackList.setValue(null);
    }
    //Delete
    public void onDeleteAccount(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete Account?");

        // Display the alert dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //delete player table
                try(Connection c = MySqlConnection.getConnection();
                    PreparedStatement statement = c.prepareStatement(
                            "DELETE FROM playerclan WHERE owner=?")){
                    c.setAutoCommit(false);

                    statement.setInt(1, CurrentUID);
                    int row = statement.executeUpdate();
                    if(row == 0){
                        System.out.println("No deleted");
                        return;
                    }

                    c.commit();
                }catch(SQLException e){
                    e.printStackTrace();
                }

                //delete user table
                try(Connection c = MySqlConnection.getConnection();
                    PreparedStatement statement = c.prepareStatement(
                            "DELETE FROM users WHERE id=?")){
                    c.setAutoCommit(false);

                    statement.setInt(1, CurrentUID);
                    int row = statement.executeUpdate();
                    if(row == 0){
                        System.out.println("No deleted");
                        return;
                    }

                    c.commit();
                }catch(SQLException e){
                    e.printStackTrace();
                }

                Logout();
            }else{
                System.out.println("Cancelling...");
            }
        });
    }

    public void Logout(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(root);
            myONlyStage.setScene(scene);
            myONlyStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void onChangeUsername(){
        int uid = CurrentUID.intValue();
        try(Connection c = MySqlConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE users SET name=? WHERE id=?"
            )){
            //transaction start
            c.setAutoCommit(false);

            String newName = tfNewUsername.getText();
            statement.setString(1, newName);
            statement.setInt(2 , uid);

            int rows = statement.executeUpdate();
            if(rows != 1){
                throw new SQLException("Failed to update username");
            }

            //transaction end
            c.commit();
            //where do I place the rollback??? do I need rollback???
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int compareUnit(String EnemySamurai){ //rock paper scissors comparison
        int PlayerIndex = getSamuraiIndex(CurrentSamuraiUnit);
        System.out.println(CurrentSamuraiUnit + " : " + PlayerIndex);
        int EnemyIndex = getSamuraiIndex(EnemySamurai);
        System.out.println(EnemySamurai + " : " + EnemyIndex);
        int toReturn = 0;

        if(PlayerIndex != EnemyIndex){
            toReturn = (EnemyIndex == (PlayerIndex+1)%3) ? -1 : 1;

        }

        return toReturn;
    }

    public int getSamuraiIndex(String Samurai){
        switch (Samurai){
            case "Yari":
                return 0;
            case "Katana":
                return 1;
            case "Cav":
                return 2;
        }
        return -1;
    }
}

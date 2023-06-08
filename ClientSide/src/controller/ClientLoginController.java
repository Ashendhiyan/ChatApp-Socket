package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ClientLoginController {
    public AnchorPane root;
    public JFXTextField txtUserName;

    public static String username;

    public static ArrayList<String> users = new ArrayList<>();

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        username = txtUserName.getText().trim();
        if (!username.equals(null)){
            boolean x = false;
            if (users.isEmpty()){
                users.add(username);
                x = true;
            }
            for (String s: users) {
                if (!s.equalsIgnoreCase(username)){
                    x = true;
                    System.out.println(username);
                    break;
                }
            }
            if (x){
                Stage stage =(Stage) root.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/Client.fxml"))));
            }else{
                new Alert(Alert.AlertType.ERROR,"User Already Exists.!", ButtonType.OK).show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Enter Username..",ButtonType.OK).show();
        }
    }

    public void imgCloseOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void imgMinOnAction(MouseEvent mouseEvent) {
        Stage s = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public void txtUserName(ActionEvent actionEvent) throws IOException {
        btnLoginOnAction(actionEvent);
    }
}

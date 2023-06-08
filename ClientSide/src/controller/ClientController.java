package controller;

import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.Socket;


public class ClientController extends Thread {
    public TextArea txtArea;
    public AnchorPane root;
    public TextField txtMsg;
    public ImageView imageView;

    private FileChooser fileChooser;
    private File filePath;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    ObjectOutputStream oos = null;

    final int PORT = 5000;



    public void initialize() {
        try {
            socket = new Socket("localhost", PORT);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fullmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullmsg.append(tokens[i]);
                }
                System.out.println(fullmsg);
                if (cmd.equalsIgnoreCase(ClientLoginController.username + ":")) {
                    continue;
                } else if (fullmsg.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                txtArea.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imgCloseOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void imgMinOnAction(MouseEvent mouseEvent) {
        Stage s = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public void txtMsgOnAction(ActionEvent actionEvent) {
        String msg = txtMsg.getText().trim();
        writer.println(ClientLoginController.username + ": " + msg);
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtArea.appendText("Me: " + msg + "\n");
        txtMsg.setText("");
        if ((msg.equals("BYE")) || (msg.equals("bye"))) {
            System.exit(0);
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        txtMsgOnAction(actionEvent);
    }


    public void imgImageOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        String path = filePath.getPath();
        System.out.println(path);
        Image image = new Image(path);
        imageView = new ImageView();
        Panel panel = new Panel();
        imageView.setImage(image);
        root.getChildren().add(imageView);

    }
}

package controller;

import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientController extends Thread {
    public AnchorPane root;
    public TextField txtMsg;
    public TextArea txtArea;

    public ImageView imageView;

    Socket remoteSocket;
    BufferedReader reader;
    PrintWriter writer;


    public void initialize() {
        new Thread(() -> {
            final int PORT = 5000;
            try {
                remoteSocket = new Socket("localhost", PORT);
                System.out.println("Socket is connected with server..!");
                reader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream(), StandardCharsets.UTF_8));
                writer = new PrintWriter(remoteSocket.getOutputStream(), true);
                this.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
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
            remoteSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imgMinOnAction(MouseEvent mouseEvent) {
        Stage s = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    public void imgCloseOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void txtMsgOnAction(ActionEvent actionEvent) throws IOException {
        btnSendOnAction(actionEvent);
    }

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        String messageText = txtMsg.getText().trim();
        writer.println(ClientLoginController.username + " : " + messageText);
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtArea.appendText("Me : " + messageText + "\n");
        txtMsg.setText("");
        if ((messageText.equals("BYE")) || (messageText.equals("bye"))) {
            System.exit(0);
        }
    }

    public void imgImageOnAction(MouseEvent mouseEvent) {
    }
}

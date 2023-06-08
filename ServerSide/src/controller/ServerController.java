package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerController {
    public TextArea txtArea;
    final int PORT = 5000;
    Socket localSocket;
    ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public void btnStartOnAction(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                serverSocket =new ServerSocket(PORT);
                txtArea.appendText("Server Started..!\n");
                while (true){
                    localSocket = serverSocket.accept();
                    txtArea.appendText("Client Connected :- "+localSocket+"\n");
                    ClientHandler clientThread = new ClientHandler(localSocket,clients);
                    clients.add(clientThread);
                    System.out.println("check.."+clients);
                    clientThread.start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}

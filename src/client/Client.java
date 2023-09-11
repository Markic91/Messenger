package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public final class Client {
    private final String username;
    private final int port;
    private final String host;


    public Client(String host, int port, String username) throws IOException {
        this.port = port;
        this.host = host;
        this.username = username;
    }

    /**
     * Se connecte au serveur
     * Lit les messages de manière asynchrone
     * Lit les messages clavier de manière bloquante
     */
    public void connect()  {
        try (
        Socket clientSocket = new Socket(this.host, this.port);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Scanner sc = new Scanner(System.in);
        ) {
        System.out.println("Connecté au serveur");

        Thread messageReceiver = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        messageReceiver.start();

        String userInput;
        while (true) {
            userInput = sc.nextLine();
            String formattedMessage = formatMessage(this.username, userInput);
            out.println(formattedMessage);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatMessage(String username, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String currentTime = dateFormat.format(new Date());
        return currentTime + " " + username + " : " + message;
    }

}


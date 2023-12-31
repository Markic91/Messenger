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
    private Socket socket;
    private PrintWriter out;

    public Client(String host, int port, String username) {
        this.port = port;
        this.host = host;
        this.username = username;
    }

    /**
     * Se connecte au serveur
     */
    public void connect() throws IOException {

        Socket clientSocket = new Socket(this.host, this.port);
        System.out.println("Connecté au serveur");
        this.socket = clientSocket;
    }

    public void disconnect() throws IOException {
        this.socket.shutdownOutput();
    }

    /**
     * Lit les messages de manière asynchrone
     * @throws IOException
     */
    public void receive() throws IOException {
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

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
    }

    /**
     * Lit les messages clavier
     * arrête la lecture lors de la saisie de "stop"
     */
    public void write() throws IOException {
        Scanner sc = new Scanner(System.in);

        String userInput = "";
        while (!userInput.equals("stop")) {
            userInput = sc.nextLine();
            String formattedMessage = formatMessage(this.username, userInput);
            if (!userInput.equals("stop")){
                out.println(formattedMessage);
            }
            else {
                out.println(deconnexionMessage(this.username));
                disconnect();
            }
        }
    }
    private static String formatMessage(String username, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String currentTime = dateFormat.format(new Date());
        String formattedMessage = String.format("%s %s: %s",currentTime, username, message );
        return formattedMessage;
    }
    private static String deconnexionMessage(String username) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String currentTime = dateFormat.format(new Date());
        String s = "s'est déconnecté à ";
        String formattedMessage = String.format ("%s %s%s",username, s, currentTime);
        return formattedMessage;
    }
}


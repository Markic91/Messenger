package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public final class Client {
    private String username;
    private int port;
    private String host;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc =  new Scanner(System.in);

    public Client(String host, int port, String username) throws IOException {
        this.port = port;
        this.host = host;
        this.username = username;
    }
    public void connect() throws IOException {
        this.clientSocket = new Socket(this.host, this.port);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream());
        Thread envoyer = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                while(true){
                    msg = sc.nextLine();
                    out.println(msg);
                    out.flush();
                }
            }
        });
        envoyer.start();

        Thread recevoir = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                try {
                    msg = in.readLine();
                    while(msg!=null){
                        System.out.println("Serveur : "+msg);
                        msg = in.readLine();
                    }
                    System.out.println("Serveur déconecté");
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recevoir.start();

}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}

package client;

import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public final class Client {
    private String username;
    private int port;
    private InetAddress host;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc =  new Scanner(System.in);

    public Client(InetAddress host, int port, String username) throws IOException {
        this.port = port;
        this.host = host;
        this.username = username;
    }
    public void connect() throws IOException {
        this.clientSocket = new Socket(this.host, this.port);
    }
    public void dialog() throws IOException {

        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new PrintWriter(this.clientSocket.getOutputStream());
        Thread serve = new Thread(new Runnable() {
            String msg;
//            @Override
            public void run() {
                while(true){
                    msg = sc.nextLine();
                    out.println(msg);
                    out.flush();
                }
            }
        });
        serve.start();
        Thread reception = new Thread(new Runnable() {
            String msg;
//            @Override
            public void run() {
                try {
                    msg = in.readLine();
                    while(msg!=null){
                        System.out.println("Serveur : "+msg);
                        msg = in.readLine();
                    }
                    System.out.println("Serveur déconnecté");
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        reception.start();

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

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

}

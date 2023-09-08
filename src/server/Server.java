package server;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public final class Server {

    private final InetAddress address;
    private final int backlog;
    private final int port;

    private final String banner;
    private ServerSocket serveurSocket ;
    private Socket clientSocket ;
    private BufferedReader in;
    private PrintWriter exit;
    final Scanner sc=new Scanner(System.in);

    public Server(InetAddress address, int port, String banner) {
        this.address = address;
        this.backlog = 3;
        this.port = port;
        this.banner = banner;
    }
    public void listen() throws IOException {
        this.serveurSocket = new ServerSocket(port, backlog);
        this.clientSocket = serveurSocket.accept();
    }

    public void dialog() throws IOException {
        this.exit = new PrintWriter(this.clientSocket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));


    Thread reception = new Thread(new Runnable() {
        String msg;
//        @Override
        public void run() {
            while(true){
                msg = sc.nextLine();
                exit.println(msg);
                exit.flush();
            }
        }
    });
    reception.start();
    Thread display= new Thread(new Runnable() {
        String msg ;
//        @Override
        public void run() {
            try {
                msg = in.readLine();
                //tant que le client est connecté
                while(msg!=null){
                    System.out.println("Client : "+msg );
                    msg = in.readLine();
                }
                //sortir de la boucle si le client a déconnecté
                System.out.println("Client déconnecté");
                //fermer le flux et la session socket
                exit.close();
                clientSocket.close();
                serveurSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    display.start();
}
    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getBanner() {
        return banner;
    }
}

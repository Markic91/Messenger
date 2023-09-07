package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public final class Server {

    private final String address;

    private final int port;

    private final String banner;
    private ServerSocket serveurSocket ;
    private Socket clientSocket ;
    private BufferedReader in;
    private PrintWriter out;
    final Scanner sc=new Scanner(System.in);

    public Server(String address, int port, String banner) {
        this.address = address;
        this.port = port;
        this.banner = banner;
    }
    public void listen() throws IOException {
        this.serveurSocket = new ServerSocket(port);
        this.clientSocket = serveurSocket.accept();
    }

    public void dialog() throws IOException {
        this.out = new PrintWriter(this.clientSocket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));


    Thread envoi= new Thread(new Runnable() {
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
    envoi.start();
    Thread recevoir= new Thread(new Runnable() {
        String msg ;
        @Override
        public void run() {
            try {
                msg = in.readLine();
                //tant que le client est connecté
                while(msg!=null){
                    System.out.println("Client : "+msg);
                    msg = in.readLine();
                }
                //sortir de la boucle si le client a déconecté
                System.out.println("Client déconnecté");
                //fermer le flux et la session socket
                out.close();
                clientSocket.close();
                serveurSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    recevoir.start();
}
    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getBanner() {
        return banner;
    }
}

package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Server {
    private static List<ClientHandler> clients = new ArrayList<>();
    private final String address;
    private final int backlog;
    private final int port;

    private final String banner;
    private ServerSocket serverSocket;



//    final Scanner sc = new Scanner(System.in);

    public Server(String address, int port, String banner) {
        this.address = address;
        this.backlog = 10;
        this.port = port;
        this.banner = banner;
    }

    public void listen() throws IOException {
        this.serverSocket = new ServerSocket(this.port, this.backlog, InetAddress.getByName(this.address));
        System.out.println("Le serveur écoute sur " + this.address + ":" + this.port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nouveau Client connecté: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            ClientHandler clientHandler = new ClientHandler(clientSocket, this.banner);
            clients.add(clientHandler);
            clientHandler.start();
        }

    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private String banner;

        public ClientHandler(Socket socket, String banner) {
            this.clientSocket = socket;
            this.banner = banner;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(banner);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    broadcastMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }
}

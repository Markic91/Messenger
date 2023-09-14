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


public final class Server {
    private static List<ClientHandler> clients = new ArrayList<>();
    private final String address;
    private static final int backlog = 10;
    private final int port;

    private final String banner;
    private ServerSocket serverSocket;


    public Server(String address, int port, String banner) {
        this.address = address;
        this.port = port;
        this.banner = banner;
    }

    /**
     * Tant que le serveur écoute il accepte les connexions des clients
     * add ajoute le nouveau client à la liste de clients
     * start (hérité de Thread) permet de créer un nouveau Thread par client
     * @throws IOException
     */
    public void listen() throws IOException {
        this.serverSocket = new ServerSocket(this.port, Server.backlog, InetAddress.getByName(this.address));
        System.out.println("Le serveur écoute sur " + this.address + ":" + this.port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nouveau Client connecté: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            ClientHandler clientHandler = new ClientHandler(clientSocket, this.banner);
            clients.add(clientHandler);
            clientHandler.start();
        }
    }

    /**
     * Hérite de Thread afin de pouvoir créer un Thread par client
     */
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private String banner;

        public ClientHandler(Socket socket, String banner) {
            this.clientSocket = socket;
            this.banner = banner;
        }

        /**
         * run() démarre le Thread et permet la lecture, l'écriture et la diffusion des messages
         * puis la fermeture du Thread et la suppression de la liste de clients
         */
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

        /**
         * Diffuse les messages de chaque client
         * @param message
         */
        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }
}

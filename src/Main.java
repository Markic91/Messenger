import client.Client;
import server.Server;

public class Main {
    public static void main(String[] args) {

        if (args.length<1){
            System.err.println("Veuillez spécifier le mode client (-c) ou le mode serveur (-l)");
            return;
        }

        if (args[0].equals("-c")){
            //Alors on bascule sur le mode client
            String host = null;
            int port = 19337;
            String username = "anonymous";

            for (int i=1; i<args.length; i++){
                if (args[i].equals("-h") && i<args.length -1){
                    host=args[i+1];
                } else if (args[i].equals("-p") && i< args.length-1){
                    port=Integer.parseInt(args[i+1]);
                } else if (args[i].equals("-u") && i< args.length-1) {
                    username=args[i+1];
                }
            }

            //On crée un objet client avec les valeurs qu'on vient d'analyser
            Client client = new Client(host, port, username);

            System.out.println(client);

            //lancer le mode client

        } else if (args[0].equals("-l")) {
            //Alors, on bascule sur le mode server
            String address = "0,0,0,0";
            int port = 19337;
            String banner = null;

            for(int i=1; i<args.length-1; i++){
                if (args[i].equals("-a")){
                    address= args[i+1];
                } else if (args[i].equals("-p")) {
                    port=Integer.parseInt(args[i+1]);
                } else if (args[i].equals("-b")) {
                    banner=args[i+1];
                }
            }

            //On crée un objet server avec les valeurs qu'on vient d'analyser
            Server server = new Server(address, port,banner);

            //lancer le mode server
        }


    }
}
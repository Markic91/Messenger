package server;

public final class Server {

    private final String address;

    private final int port;

    private final String banner;

    public Server(String address, int port, String banner) {
        this.address = address;
        this.port = port;
        this.banner = banner;
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

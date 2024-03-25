package olechka.lab6.server;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Server server = new Server()) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.save();
            }));

            server.run();
        }
    }
}

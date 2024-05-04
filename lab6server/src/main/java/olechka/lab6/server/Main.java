package olechka.lab6.server;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Server server = new Server()) {
//            когда на сервере нажимаем ctrl+c  - обработка здесь
//            пооток прооходит нашу hashmap а может прийтий запрос нового клиента а сервер будет пытаться создать новый стейт
//            я не могу добавить новый стейт в мэпу пока я ее прохожу!!!
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.save();
            }));

            server.run();
        }
    }
}

package olechka.lab6.client;

import olechka.lab6.interaction.InterationClosedException;

public class Main {


    public static void main(String[] args) throws Exception {

        try (Client client = new Client("127.0.0.1")) {
            client.run();
        } catch (InterationClosedException e) {
            if (System.getProperty("os.name", "").startsWith("Windows")) {
                System.out.println("Была нажата комбинация клавиш ctrl+c. Совершен выход из программы.");
            } else {
                System.out.println("Была нажата комбинация клавиш ctrl+d. Совершен выход из программы.");
            }
        }

    }
}
package olechka.lab8.client;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(new Locale("ru"));
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Client client = new Client();
        client.openLoginWindow();
    }
}
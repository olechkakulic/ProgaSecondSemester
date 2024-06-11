package olechka.lab8.client;

import olechka.lab8.State;
import olechka.lab8.client.localization.Texts;
import olechka.lab8.commands.Command;
import olechka.lab8.exceptions.CommandFailureType;
import olechka.lab8.protocol.ProtocolMessage;
import olechka.lab8.protocol.ProtocolMessageType;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

//клиент который представляет из себя приложение.
public class Client {
    private final NetworkClient networkClient;
    private final LoginForm loginForm;
    private final StateForm stateForm;
    private final TableForm tableForm;
    private State state;

    public Client() throws IOException {
        networkClient = new NetworkClient((message) -> processMessage(message));
        loginForm = new LoginForm(this);
        stateForm = new StateForm(this);
        tableForm = new TableForm(this);

        Thread thread = new Thread(networkClient, "Network-Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public void openLoginWindow() {
        loginForm.setVisible(true);
    }

    //этот метод вызывается в потоке addActionListener
    public void login(String address, String login, String password, boolean isRegister) {
        loginForm.setButtonsEnabled(false);
        stateForm.setUsername(login);
        try {
            String[] addressParts = address.split(":");
            InetSocketAddress socketAddress = new InetSocketAddress(addressParts[0], Integer.parseInt(addressParts[1]));
            networkClient.setLoginSettings(login, password, socketAddress);

            if (isRegister) {
                networkClient.sendMessage(ProtocolMessageType.REGISTER, null);
            } else {
                networkClient.sendMessage(ProtocolMessageType.LOGIN, null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginForm, e, Texts.getCurrent().getErrorWindowTitle(), JOptionPane.ERROR_MESSAGE);
            loginForm.setButtonsEnabled(true);
        }
    }

    public void openTableWindow() {
        tableForm.setData(state.getCollection());
        tableForm.setVisible(true);
    }

    public void requestCommand(Command command) {
        try {
            networkClient.sendMessage(ProtocolMessageType.EXECUTE_COMMAND, command);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, Texts.getCurrent().getErrorWindowTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void processMessage(ProtocolMessage message) {
        switch (message.getType()) {
            case SERVER_ERROR -> {
                loginForm.setButtonsEnabled(true);

                CommandFailureType type = message.getFailureType();
                String text = Texts.getCurrent().getErrorTypes().get(type);
                JOptionPane.showMessageDialog(null, text, Texts.getCurrent().getErrorWindowTitle(), JOptionPane.ERROR_MESSAGE);
            }
            case SERVER_STATE -> {
                state = message.getState();

                tableForm.setData(state.getCollection());

                stateForm.setState(state);
                stateForm.setVisible(true);
                loginForm.setVisible(false);
                loginForm.setButtonsEnabled(true);
            }
            case EXECUTE_COMMAND -> {
                Command command = message.getCommand();
                command.execute(state);

                stateForm.repaint();

                if (tableForm.isVisible()) {
                    tableForm.setData(state.getCollection());
                }
            }
        }
    }
}

package olechka.lab8.client;

import olechka.lab8.client.localization.Texts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

//Окно с авторизацией/регистрацией.
public class LoginForm extends JFrame {
    private final Client client;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JCheckBox createAccountCheckBox;
    private JTextField addressField;

    public LoginForm(Client client) {
        this.client = client;

        setTitle(Texts.getCurrent().getLoginWindowTitle());
        setContentPane(contentPane);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(dimension.width / 2 - 250, dimension.height / 2 - 250, 500, 300);
//для разных взаимодействий есть разные listener-ы.
//        ActionEvent e - это взаимодействия с объектом.
//        addActionListener - будет обрабатывать событие
        buttonOK.addActionListener((e) -> {
            client.login(addressField.getText(), loginField.getText(), new String(passwordField.getPassword()), createAccountCheckBox.isSelected());
        });

        buttonCancel.addActionListener((e) -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction((e) -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setButtonsEnabled(boolean isEnabled) {
        buttonOK.setEnabled(isEnabled);
        buttonCancel.setEnabled(isEnabled);
    }

}

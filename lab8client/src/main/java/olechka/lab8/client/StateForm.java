package olechka.lab8.client;

import olechka.lab8.State;
import olechka.lab8.client.localization.Texts;
import olechka.lab8.commands.ClearCommand;

import javax.swing.*;
import java.awt.*;

public class StateForm extends JFrame {
    private final Client client;
    private final DrawingPanel drawingPanel;
    private final JLabel usernameLabel;

    public StateForm(Client client) {
        this.client = client;

        drawingPanel = new DrawingPanel();
        usernameLabel = new JLabel();
        drawingPanel.add(usernameLabel);

        setTitle(Texts.getCurrent().getStateWindowTitle());
        setContentPane(drawingPanel);
//заспавнится окошко с информацией об объекте
        drawingPanel.addClickConsumer((studyGroup) -> {
            ElementForm elementForm = new ElementForm(client);
            elementForm.fillStudyGroup(studyGroup);
            elementForm.setVisible(true);
        });

        // добавляем менюшку. на нашу форму со стейт
        JMenuItem addItem = new JMenuItem(Texts.getCurrent().getMenuAddElement());
        addItem.addActionListener((e) -> {
            ElementForm elementForm = new ElementForm(client);
            elementForm.setVisible(true);
        });

        JMenuItem deleteItem = new JMenuItem(Texts.getCurrent().getMenuClearItem());
        deleteItem.addActionListener((e) -> {
            ClearCommand clearCommand = new ClearCommand();
            client.requestCommand(clearCommand);
        });

        JMenuItem openTableItem = new JMenuItem(Texts.getCurrent().getOpenTableItem());
        openTableItem.addActionListener((e) -> {
            client.openTableWindow();
        });

        JMenuItem helpItem = new JMenuItem(Texts.getCurrent().getMenuHelpItem());
        helpItem.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, Texts.getCurrent().getDescriptionHelp(), Texts.getCurrent().getStateWindowTitle(), JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem logOutItem = new JMenuItem(Texts.getCurrent().getLogOutItem());
        logOutItem.addActionListener((e) -> {
            client.openLoginWindow();
            setVisible(false);
        });

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu(Texts.getCurrent().getElementMenu());
        menu.add(addItem);
        menu.add(deleteItem);
        menu.add(openTableItem);
        menu.add(logOutItem);
        menu.add(helpItem);
        menubar.add(menu);
        setJMenuBar(menubar);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(dimension.width / 2 - 500, dimension.height / 2 - 350, 1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void setState(State state) {
        drawingPanel.setCollection(state.getCollection());
    }

    public void setUsername(String username) {
        usernameLabel.setText("Вы вошли как: " + username);
    }
}

package olechka.lab8.client;

import olechka.lab8.client.localization.Texts;
import olechka.lab8.models.StudyGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;

public class TableForm extends JFrame {
    private final Client client;
    private final JTable table;
    private final JScrollPane contentPane;

    private StudyGroup[] currentGroups;

    public TableForm(Client client) {
        this.client = client;

        table = new JTable();
        contentPane = new JScrollPane(table);

        // полагаемся на автоматическую сортировку полей. ну а что)
        table.setAutoCreateRowSorter(true);

        // у таблицы есть модель - объект, описывающий внутренность таблицы (используем стандартную реализацию модели)
        table.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // MouseListener для обработки двойного клика в таблице, да)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (e.getClickCount() == 2 && index >= 0) {
                    openElementInfo(index);
                }
            }
        });

        // Не можем выбрать несколько элементов (а зачем лол)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setTitle(Texts.getCurrent().getTableWindowTitle());
        setContentPane(contentPane);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(dimension.width / 2 - 500, dimension.height / 2 - 350, 1000, 700);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void openElementInfo(int elementIndex) {
        StudyGroup studyGroup = currentGroups[elementIndex];
        ElementForm elementForm = new ElementForm(client);
        elementForm.fillStudyGroup(studyGroup);
        elementForm.setVisible(true);
    }

    //метод заполняющий таблицу
    public void setData(Collection<StudyGroup> collection) {
        currentGroups = collection.toArray(StudyGroup[]::new);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[][] data = Arrays.stream(currentGroups).map((s) -> {
            return new Object[]{s.getId(),
                    s.getName(),
                    s.getCreationDate(),
                    s.getCoordinates(),
                    s.getStudentsCount(),
                    s.getTransferredStudents(),
                    s.getSemesterEnum(),
                    s.getFormOfEducation(),
                    s.getGroupAdmin()};
        }).toArray(Object[][]::new);
        model.setDataVector(data, Texts.getCurrent().getTableTitles());
    }
}

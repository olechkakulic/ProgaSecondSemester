package olechka.lab8.client;

import olechka.lab8.client.localization.Texts;
import olechka.lab8.client.validators.NumberInputVerifier;
import olechka.lab8.commands.AddElementCommand;
import olechka.lab8.commands.RemoveElementCommand;
import olechka.lab8.commands.UpdateElementCommand;
import olechka.lab8.models.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ElementForm extends JFrame {
    private final Client client;
    private final List<JComponent> verifiedComponents;
    private JPanel contentPane;
    private JButton acceptButton;
    private JButton cancelButton;
    private JTextField xCoordinateField;
    private JTextField yCoordinateField;
    private JTextField nameField;
    private JTextField passportIDPersonField;
    private JTextField studentsCountField;
    private JTextField transferredStudentsField;
    private JComboBox<Semester> semesterEnumField;
    private JComboBox<FormOfEducation> formOfEducationField;
    private JCheckBox groupAdminField;
    private JTextField namePersonField;
    private JTextField birthdayPersonField;
    private JTextField heightPersonField;
    private JTextField weightPersonField;
    private JButton removeButton;
    private Long updateStudyGroupId;

    public ElementForm(Client client) {
        this.client = client;

        verifiedComponents = new ArrayList<>();

        setTitle(Texts.getCurrent().getElementWindowTitle());
        setContentPane(contentPane);

        pack();
        setResizable(false);

        Semester[] semesters = new Semester[]{null, Semester.FIRST, Semester.FIFTH, Semester.SIXTH, Semester.SEVENTH};
        semesterEnumField.setModel(new DefaultComboBoxModel<>(semesters));

        FormOfEducation[] formOfEducations = FormOfEducation.values();
        formOfEducationField.setModel(new DefaultComboBoxModel<>(formOfEducations));

        groupAdminField.addActionListener((e) -> {
            boolean isChecked = groupAdminField.isSelected();
            setPersonFieldsEnabled(isChecked);
        });

        addInputVerifier(xCoordinateField, new NumberInputVerifier(-47.0, null, true));
        addInputVerifier(yCoordinateField, new NumberInputVerifier(null, null, false));
        addInputVerifier(studentsCountField, new NumberInputVerifier(0.0, null, true));
        addInputVerifier(transferredStudentsField, new NumberInputVerifier(0.0, null, true));

        for (JComponent component : verifiedComponents) {
            component.getInputVerifier().shouldYieldFocus(component, contentPane);
        }

        setPersonFieldsEnabled(false);

        acceptButton.addActionListener((e) -> {
            try {
                for (JComponent component : verifiedComponents) {
                    if (!component.getInputVerifier().verify(component)) {
                        throw new NumberFormatException();
                    }
                }

                StudyGroup studyGroup = createStudyGroup();
                if (updateStudyGroupId != null) {
                    UpdateElementCommand command = new UpdateElementCommand(updateStudyGroupId, studyGroup);
                    client.requestCommand(command);
                } else {
                    AddElementCommand command = new AddElementCommand(studyGroup);
                    client.requestCommand(command);
                }

                dispose();
            } catch (NumberFormatException | DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректные данные", Texts.getCurrent().getErrorWindowTitle(), JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener((e) -> {
            RemoveElementCommand command = new RemoveElementCommand(updateStudyGroupId);
            client.requestCommand(command);

            dispose();
        });

        cancelButton.addActionListener((e) -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction((e) -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void addInputVerifier(JComponent component, InputVerifier verifier) {
        component.setInputVerifier(verifier);
        verifiedComponents.add(component);
    }

    private void setPersonFieldsEnabled(boolean isEnabled) {
        namePersonField.setEnabled(isEnabled);
        birthdayPersonField.setEnabled(isEnabled);
        heightPersonField.setEnabled(isEnabled);
        weightPersonField.setEnabled(isEnabled);
        passportIDPersonField.setEnabled(isEnabled);
    }

    public void fillStudyGroup(StudyGroup studyGroup) {
        updateStudyGroupId = studyGroup.getId();
        removeButton.setEnabled(true);
        nameField.setText(studyGroup.getName());
        xCoordinateField.setText(String.valueOf(studyGroup.getCoordinates().getX()));
        yCoordinateField.setText(String.valueOf(studyGroup.getCoordinates().getY()));
        studentsCountField.setText(String.valueOf(studyGroup.getStudentsCount()));
        transferredStudentsField.setText(String.valueOf(studyGroup.getTransferredStudents()));
        formOfEducationField.setSelectedItem(studyGroup.getFormOfEducation());
        semesterEnumField.setSelectedItem(studyGroup.getSemesterEnum());
        Person person = studyGroup.getGroupAdmin();
        if (person != null) {
            setPersonFieldsEnabled(true);
            groupAdminField.setSelected(true);

            namePersonField.setText(person.getName());
            birthdayPersonField.setText(String.valueOf(person.getBirthday()));
            heightPersonField.setText(String.valueOf(person.getHeight()));
            weightPersonField.setText(String.valueOf(person.getWeight()));
            passportIDPersonField.setText(String.valueOf(person.getPassportID()));
        } else {
            setPersonFieldsEnabled(false);
            groupAdminField.setSelected(false);
        }
    }

    public StudyGroup createStudyGroup() {
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setName(nameField.getText());
        studyGroup.setStudentsCount(Long.parseLong(studentsCountField.getText()));
        studyGroup.setTransferredStudents(Long.parseLong(transferredStudentsField.getText()));
        studyGroup.setFormOfEducation((FormOfEducation) formOfEducationField.getSelectedItem());
        studyGroup.setSemesterEnum((Semester) semesterEnumField.getSelectedItem());
        if (groupAdminField.isSelected()) {
            Person person = new Person();
            person.setName(namePersonField.getText());
            person.setBirthday(LocalDate.parse(birthdayPersonField.getText()));
            person.setHeight(Long.parseLong(heightPersonField.getText()));
            person.setWeight(Long.parseLong(weightPersonField.getText()));
            person.setPassportID(passportIDPersonField.getText());
            studyGroup.setGroupAdmin(person);
        }
        Coordinates coordinates = new Coordinates();
        coordinates.setX(Integer.parseInt(xCoordinateField.getText()));
        coordinates.setY(Double.parseDouble(yCoordinateField.getText()));
        studyGroup.setCoordinates(coordinates);
        return studyGroup;
    }

    public void setButtonsEnabled(boolean isEnabled) {
        acceptButton.setEnabled(isEnabled);
        cancelButton.setEnabled(isEnabled);
    }
}

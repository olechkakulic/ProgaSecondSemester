package olechka.lab8.client.validators;

import javax.swing.*;
import java.awt.*;

public class NumberInputVerifier extends InputVerifier {
    private final Double minimumValue, maximumValue;
    private final boolean useInteger;

    public NumberInputVerifier(Double minimumValue, Double maximumValue, boolean useInteger) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.useInteger = useInteger;
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent target) {
        if (verify(source)) {
            source.setBackground(new Color(0x66df6f));
        } else {
            source.setBackground(Color.PINK);
        }
        return true;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField) input;
        try {
            double value;
            if (useInteger) {
                value = Integer.parseInt(textField.getText());
            } else {
                value = Double.parseDouble(textField.getText());
            }
            if (minimumValue != null && value < minimumValue) {
                return false;
            }
            if (maximumValue != null && value > maximumValue) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

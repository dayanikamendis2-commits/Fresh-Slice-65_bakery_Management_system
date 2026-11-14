package database;

import javax.swing.*;

public class Validator {

    /**
     * Validates that a text field is not empty
     * @param field The JTextField to validate
     * @param fieldName The name of the field for error messages
     * @return true if field is not empty, false otherwise
     */
    public static boolean isNotEmpty(JTextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, fieldName + " cannot be empty!");
            field.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates that multiple string values are not empty
     * @param values Variable number of string values to check
     * @return true if all values are not empty, false otherwise
     */
    public static boolean isNotEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates that a text field contains a numeric value
     * @param field The JTextField to validate
     * @param fieldName The name of the field for error messages
     * @return true if field contains a valid number, false otherwise
     */
    public static boolean isNumeric(JTextField field, String fieldName) {
        try {
            Double.valueOf(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, fieldName + " must be a valid number!");
            field.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains an integer value
     * @param field The JTextField to validate
     * @param fieldName The name of the field for error messages
     * @return true if field contains a valid integer, false otherwise
     */
    public static boolean isInteger(JTextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, fieldName + " must be a valid whole number!");
            field.requestFocus();
            return false;
        }
    }

    /**
     * Validates that a text field contains a positive number
     * @param field The JTextField to validate
     * @param fieldName The name of the field for error messages
     * @return true if field contains a positive number, false otherwise
     */
    public static boolean isPositiveNumber(JTextField field, String fieldName) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (value <= 0) {
                JOptionPane.showMessageDialog(null, fieldName + " must be greater than zero!");
                field.requestFocus();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, fieldName + " must be a valid number!");
            field.requestFocus();
            return false;
        }
    }
}


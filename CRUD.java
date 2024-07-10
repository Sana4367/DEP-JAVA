package internshipTasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import DesktopAppProject.DatabaseConnectivity;

public class studentApp extends JFrame {
    public studentApp() {
        GUI();
    }

    private void GUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBackground(Color.GRAY);

        // Left Column - Buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = GridBagConstraints.RELATIVE;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.anchor = GridBagConstraints.NORTHWEST;
        buttonGbc.insets = new java.awt.Insets(5, 5, 5, 5); // Add some padding

        addButton(leftPanel, "Create Student Table", buttonGbc);
        addButton(leftPanel, "Insert Details", buttonGbc);
        addButton(leftPanel, "Read Details", buttonGbc);
        addButton(leftPanel, "Update Details", buttonGbc);
        addButton(leftPanel, "Delete Details", buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(leftPanel, gbc);

        add(mainPanel);

        setTitle("Student Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
    }

    private void addButton(JPanel panel, String label, GridBagConstraints gbc) {
        JButton button = new JButton(label);
        button.setBackground(Color.DARK_GRAY);
        button.setMaximumSize(new Dimension(180, 50));
        button.setForeground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (label) {
                    case "Create Student Table":
                        CRUDOperations.createStudentTable();
                        break;
                    case "Insert Details":
                        CRUDOperations.insertDetails();
                        break;
                    case "Read Details":
                        CRUDOperations.readDetails();
                        break;
                    case "Update Details":
                        CRUDOperations.updateDetails();
                        break;
                    case "Delete Details":
                        CRUDOperations.deleteDetails();
                        break;
                }
            }
        });
        panel.add(button, gbc);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new studentApp().setVisible(true);
            }
        });
    }
}

class CRUDOperations {
    public static void createStudentTable() {
        int response = JOptionPane.showConfirmDialog(null, "Do you want to create the student table?", "Create Table",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnectivity.getConnection();
                 Statement statement = conn.createStatement()) {

                // Create Student table
                String createTableSQL = "CREATE TABLE IF NOT EXISTS StudentDetails (" +
                        "StudentID INT PRIMARY KEY," +
                        "StudentName VARCHAR(255) NOT NULL," +
                        "FatherName VARCHAR(255) NOT NULL," +
                        "Program VARCHAR(255) NOT NULL," +
                        "Degree VARCHAR(255) NOT NULL," +
                        "CNIC VARCHAR(255) NOT NULL," +
                        "ContactNumber VARCHAR(255) NOT NULL)";
                statement.execute(createTableSQL);
                JOptionPane.showMessageDialog(null, "Student table created successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error creating table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void readDetails() {
        JFrame frame = new JFrame("Read Details");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        StringBuilder details = new StringBuilder("<html>Student Details:<br>");

        try (Connection conn = DatabaseConnectivity.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM StudentDetails")) {

            while (rs.next()) {
                details.append("ID: ").append(rs.getInt("StudentID")).append(", ");
                details.append("Name: ").append(rs.getString("StudentName")).append(", ");
                details.append("Father's Name: ").append(rs.getString("FatherName")).append(", ");
                details.append("Program: ").append(rs.getString("Program")).append(", ");
                details.append("Degree: ").append(rs.getString("Degree")).append(", ");
                details.append("CNIC: ").append(rs.getString("CNIC")).append(", ");
                details.append("Contact: ").append(rs.getString("ContactNumber")).append("<br>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            details.append("Error retrieving details: ").append(e.getMessage());
        }

        details.append("</html>");
        JLabel label = new JLabel(details.toString());
        frame.add(label);

        frame.setVisible(true);
    }

    public static void updateDetails() {
        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter ID to update:"));
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            if (checkIfIdExists(id)) {
                JTextField nameField = new JTextField(10);
                JTextField fatherNameField = new JTextField(10);
                JTextField programField = new JTextField(10);
                JTextField degreeField = new JTextField(10);
                JTextField cnicField = new JTextField(10);
                JTextField contactField = new JTextField(10);

                JPanel updatePanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);

                gbc.gridx = 0;
                gbc.gridy = 0;
                updatePanel.add(new JLabel("Name:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(nameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                updatePanel.add(new JLabel("Father's Name:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(fatherNameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                updatePanel.add(new JLabel("Program:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(programField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 3;
                updatePanel.add(new JLabel("Degree:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(degreeField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                updatePanel.add(new JLabel("CNIC:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(cnicField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                updatePanel.add(new JLabel("Contact:"), gbc);
                gbc.gridx = 1;
                updatePanel.add(contactField, gbc);

                int updateResult = JOptionPane.showConfirmDialog(null, updatePanel, "Update Details", JOptionPane.OK_CANCEL_OPTION);
                if (updateResult == JOptionPane.OK_OPTION) {
                    try (Connection conn = DatabaseConnectivity.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(
                                 "UPDATE StudentDetails SET StudentName = ?, FatherName = ?, Program = ?, Degree = ?, CNIC = ?, ContactNumber = ? WHERE StudentID = ?")) {
                        pstmt.setString(1, nameField.getText());
                        pstmt.setString(2, fatherNameField.getText());
                        pstmt.setString(3, programField.getText());
                        pstmt.setString(4, degreeField.getText());
                        pstmt.setString(5, cnicField.getText());
                        pstmt.setString(6, contactField.getText());
                        pstmt.setInt(7, Integer.parseInt(id));

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Details updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error updating details.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error updating details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void deleteDetails() {
        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter ID to delete:"));
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            if (checkIfIdExists(id)) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?",
                        "Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnectivity.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM StudentDetails WHERE StudentID = ?")) {
                        pstmt.setInt(1, Integer.parseInt(id));

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error deleting record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void insertDetails() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField fatherNameField = new JTextField(10);
        JTextField programField = new JTextField(10);
        JTextField degreeField = new JTextField(10);
        JTextField cnicField = new JTextField(10);
        JTextField contactField = new JTextField(10);

        JPanel insertPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        insertPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        insertPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        insertPanel.add(new JLabel("Father's Name:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(fatherNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        insertPanel.add(new JLabel("Program:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(programField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        insertPanel.add(new JLabel("Degree:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(degreeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        insertPanel.add(new JLabel("CNIC:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(cnicField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        insertPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(contactField, gbc);

        int result = JOptionPane.showConfirmDialog(null, insertPanel, "Insert Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DatabaseConnectivity.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO StudentDetails (StudentID, StudentName, FatherName, Program, Degree, CNIC, ContactNumber) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, Integer.parseInt(idField.getText()));
                pstmt.setString(2, nameField.getText());
                pstmt.setString(3, fatherNameField.getText());
                pstmt.setString(4, programField.getText());
                pstmt.setString(5, degreeField.getText());
                pstmt.setString(6, cnicField.getText());
                pstmt.setString(7, contactField.getText());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Details inserted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error inserting details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error inserting details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean checkIfIdExists(String id) {
        try (Connection conn = DatabaseConnectivity.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM StudentDetails WHERE StudentID = ?")) {
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
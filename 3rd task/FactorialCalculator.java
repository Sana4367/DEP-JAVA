import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FactorialCalculator {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Factorial Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        
        frame.setVisible(true);
    }
    
    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Enter a number:");
        userLabel.setBounds(10, 20, 120, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(140, 20, 120, 25);
        panel.add(userText);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(10, 60, 120, 25);
        panel.add(calculateButton);

        JLabel resultLabel = new JLabel("Factorial:");
        resultLabel.setBounds(10, 100, 250, 25);
        panel.add(resultLabel);

        // Add action listener to the button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(userText.getText());
                    if (num < 0) {
                        JOptionPane.showMessageDialog(panel, "Please enter a non-negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int factorial = 1;
                        for (int i = 1; i <= num; i++) {
                            factorial *= i;
                        }
                        resultLabel.setText("Factorial: " + factorial);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

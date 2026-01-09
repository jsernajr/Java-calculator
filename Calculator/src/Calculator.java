import java.awt.*;
import javax.swing.*;

public class Calculator {
    private final int boardwidth = 360;
    private final int boardheight = 540;
    private final Color customLightGray = new Color(212,212,210);
    private final String[] buttonValues = {
        "AC", "+/-", "%", "÷", "7", "8", "9", "×",
        "4", "5", "6", "-", "1", "2", "3", "+",
        "0", ".", "v", "="
    };
    private final JFrame frame = new JFrame("Calculator");
    private final JLabel displayLabel = new JLabel("0", SwingConstants.RIGHT);
    private final JPanel displayPanel = new JPanel();
    private final JPanel buttonPanel = new JPanel();
    private double acc = 0;
    private String pendingOp = "";
    private boolean startNew = true;

    public Calculator() { initUI(); }

    private void initUI() {
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        displayLabel.setBackground(customLightGray);
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel, BorderLayout.CENTER);
        frame.add(displayPanel, BorderLayout.NORTH);
        buttonPanel.setLayout(new GridLayout(5, 4, 1, 1));
        buttonPanel.setBackground(Color.BLACK);
        frame.add(buttonPanel, BorderLayout.CENTER);
        for (String value : buttonValues) {
            JButton button = new JButton(value);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setFocusable(false);
            button.addActionListener(e -> onButtonPress(value));
            buttonPanel.add(button);
        }
        frame.setVisible(true);
    }

    private void onButtonPress(String value) {
        String text = displayLabel.getText();
        try {
            if (value.matches("\\d")) {
                displayLabel.setText((text.equals("0") || startNew) ? value : text + value);
                startNew = false;
            } else if (value.equals(".")) {
                if (startNew) { displayLabel.setText("0."); startNew = false; }
                else if (!text.contains(".")) displayLabel.setText(text + ".");
            } else if (value.equals("AC")) {
                displayLabel.setText("0"); acc = 0; pendingOp = ""; startNew = true;
            } else if (value.equals("+/-")) {
                if (!text.equals("0")) displayLabel.setText(text.startsWith("-") ? text.substring(1) : "-" + text);
            } else if (value.equals("%")) {
                displayLabel.setText(format(Double.parseDouble(text) / 100.0)); startNew = true;
            } else if (value.equals("v")) {
                double v = Double.parseDouble(text);
                displayLabel.setText(v < 0 ? "NaN" : format(Math.sqrt(v))); startNew = true;
            } else if (value.equals("=")) {
                if (!pendingOp.isEmpty()) {
                    acc = compute(acc, Double.parseDouble(text), pendingOp);
                    displayLabel.setText(format(acc));
                    pendingOp = ""; startNew = true;
                }
            } else if (value.equals("+") || value.equals("-") || value.equals("×") || value.equals("÷")) {
                double v = Double.parseDouble(text);
                if (pendingOp.isEmpty()) acc = v;
                else if (!startNew) { acc = compute(acc, v, pendingOp); displayLabel.setText(format(acc)); }
                pendingOp = value; startNew = true;
            }
        } catch (NumberFormatException ex) { displayLabel.setText("NaN"); startNew = true; }
    }

    private double compute(double a, double b, String op) {
        if ("+".equals(op)) return a + b;
        if ("-".equals(op)) return a - b;
        if ("×".equals(op)) return a * b;
        if ("÷".equals(op)) return b == 0 ? Double.NaN : a / b;
        return b;
    }

    private String format(double v) {
        if (Double.isNaN(v)) return "NaN";
        if (v == (long) v) return String.format("%d", (long) v);
        return String.valueOf(v);
    }
}

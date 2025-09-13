import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame implements ActionListener {
    // Components
    private JTextField display;
    private JPanel buttonPanel;
    private JPanel scientificPanel;
    private JToggleButton modeSwitch;
    
    // Calculator state
    private String currentInput = "";
    private double result = 0;
    private String lastOperator = "";
    private boolean isScientificMode = false;
    private boolean newInput = true;

    public Calculator() {
        // Set up the frame
        setTitle("Java Calculator - Standard/Scientific");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());
        
        // Create display
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        add(display, BorderLayout.NORTH);
        
        // Create mode switch button
        modeSwitch = new JToggleButton("Scientific Mode");
        modeSwitch.addActionListener(this);
        add(modeSwitch, BorderLayout.SOUTH);
        
        // Create standard calculator buttons
        buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        addStandardButtons();
        
        // Create scientific calculator buttons (hidden by default)
        scientificPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        addScientificButtons();
        scientificPanel.setVisible(false);
        
        // Add panels to frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scientificPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private void addStandardButtons() {
        // First row
        buttonPanel.add(createButton("C"));
        buttonPanel.add(createButton("√"));
        buttonPanel.add(createButton("^"));
        buttonPanel.add(createButton("/"));
        
        // Second row
        buttonPanel.add(createButton("7"));
        buttonPanel.add(createButton("8"));
        buttonPanel.add(createButton("9"));
        buttonPanel.add(createButton("*"));
        
        // Third row
        buttonPanel.add(createButton("4"));
        buttonPanel.add(createButton("5"));
        buttonPanel.add(createButton("6"));
        buttonPanel.add(createButton("-"));
        
        // Fourth row
        buttonPanel.add(createButton("1"));
        buttonPanel.add(createButton("2"));
        buttonPanel.add(createButton("3"));
        buttonPanel.add(createButton("+"));
        
        // Fifth row
        buttonPanel.add(createButton("0"));
        buttonPanel.add(createButton("."));
        buttonPanel.add(createButton("±"));
        buttonPanel.add(createButton("="));
    }
    
    private void addScientificButtons() {
        // First row
        scientificPanel.add(createButton("sin"));
        scientificPanel.add(createButton("cos"));
        scientificPanel.add(createButton("tan"));
        scientificPanel.add(createButton("π"));
        
        // Second row
        scientificPanel.add(createButton("log"));
        scientificPanel.add(createButton("ln"));
        scientificPanel.add(createButton("!"));
        scientificPanel.add(createButton("e"));
        
        // Third row
        scientificPanel.add(createButton("("));
        scientificPanel.add(createButton(")"));
        scientificPanel.add(createButton("x²"));
        scientificPanel.add(createButton("x³"));
        
        // Fourth row
        scientificPanel.add(createButton("1/x"));
        scientificPanel.add(createButton("10^x"));
        scientificPanel.add(createButton("x^y"));
        scientificPanel.add(createButton("mod"));
        
        // Fifth row
        scientificPanel.add(createButton("deg"));
        scientificPanel.add(createButton("rad"));
        scientificPanel.add(createButton("Rand"));
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 25));
        return button;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        // Handle mode switch
        if (e.getSource() == modeSwitch) {
            isScientificMode = modeSwitch.isSelected();
            scientificPanel.setVisible(isScientificMode);
            modeSwitch.setText(isScientificMode ? "Standard Mode" : "Scientific Mode");
            pack(); // Resize window to fit components
            return;
        }
        
        // Handle calculator operations
        switch (command) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (newInput) {
                    currentInput = command;
                    newInput = false;
                } else {
                    currentInput += command;
                }
                display.setText(currentInput);
                break;
                
            case ".":
                if (newInput) {
                    currentInput = "0.";
                    newInput = false;
                } else if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
                display.setText(currentInput);
                break;
                
            case "±":
                if (!currentInput.isEmpty()) {
                    if (currentInput.charAt(0) == '-') {
                        currentInput = currentInput.substring(1);
                    } else {
                        currentInput = "-" + currentInput;
                    }
                    display.setText(currentInput);
                }
                break;
                
            case "C":
                currentInput = "";
                result = 0;
                lastOperator = "";
                display.setText("");
                newInput = true;
                break;
                
            case "+": case "-": case "*": case "/": case "^": case "mod":
                if (!currentInput.isEmpty()) {
                    calculate();
                    lastOperator = command;
                    newInput = true;
                }
                break;
                
            case "=":
                if (!currentInput.isEmpty() && !lastOperator.isEmpty()) {
                    calculate();
                    lastOperator = "";
                    newInput = true;
                }
                break;
                
            // Scientific functions
            case "√":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    if (num >= 0) {
                        currentInput = String.valueOf(Math.sqrt(num));
                        display.setText(currentInput);
                    } else {
                        display.setText("Error");
                        currentInput = "";
                    }
                }
                break;
                
            case "x²":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    currentInput = String.valueOf(num * num);
                    display.setText(currentInput);
                }
                break;
                
            case "x³":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    currentInput = String.valueOf(num * num * num);
                    display.setText(currentInput);
                }
                break;
                
            case "1/x":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    if (num != 0) {
                        currentInput = String.valueOf(1 / num);
                        display.setText(currentInput);
                    } else {
                        display.setText("Error");
                        currentInput = "";
                    }
                }
                break;
                
            case "sin":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    currentInput = String.valueOf(Math.sin(Math.toRadians(num)));
                    display.setText(currentInput);
                }
                break;
                
            case "cos":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    currentInput = String.valueOf(Math.cos(Math.toRadians(num)));
                    display.setText(currentInput);
                }
                break;
                case "10^x":
                    int num3 = Integer.parseInt(currentInput);
                currentInput = String.valueOf(Math.pow(10, num3));
                display.setText(currentInput);
                newInput = false;
                break;
                case "x^y":
               if (!lastOperator.isEmpty()) {
                calculate();
                lastOperator = "^";
                newInput = true;
            } else if (!currentInput.isEmpty()) {
                result = Double.parseDouble(currentInput);
                lastOperator = "^";
                newInput = true;
            }
            break;
            case "tan":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    currentInput = String.valueOf(Math.tan(Math.toRadians(num)));
                    display.setText(currentInput);
                }
                break;
                
            case "log":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    if (num > 0) {
                        currentInput = String.valueOf(Math.log10(num));
                        display.setText(currentInput);
                    } else {
                        display.setText("Error");
                        currentInput = "";
                    }
                }
                break;
                
            case "ln":
                if (!currentInput.isEmpty()) {
                    double num = Double.parseDouble(currentInput);
                    if (num > 0) {
                        currentInput = String.valueOf(Math.log(num));
                        display.setText(currentInput);
                    } else {
                        display.setText("Error");
                        currentInput = "";
                    }
                }
                break;
                
            case "!":
                if (!currentInput.isEmpty()) {
                    try {
                        int num = Integer.parseInt(currentInput);
                        if (num >= 0) {
                            currentInput = String.valueOf(factorial(num));
                            display.setText(currentInput);
                        } else {
                            display.setText("Error");
                            currentInput = "";
                        }
                    } catch (NumberFormatException ex) {
                        display.setText("Error");
                        currentInput = "";
                    }
                }
                break;
                
            case "π":
                currentInput = String.valueOf(Math.PI);
                display.setText(currentInput);
                newInput = false;
                break;
                
            case "e":
                currentInput = String.valueOf(Math.E);
                display.setText(currentInput);
                newInput = false;
                break;
                
            case "deg":
                double num = Double.parseDouble(currentInput);
                currentInput = String.valueOf(num*(180/3.14));
                display.setText(currentInput);
                newInput = false;
                break;
            case "rad":
                double num2 = Double.parseDouble(currentInput);
                currentInput = String.valueOf(num2*(3.14/180));
                display.setText(currentInput);
                newInput = false;
                // For future use if implementing angle mode switching
                break;
                
            case "Rand":
                currentInput = String.valueOf(Math.random());
                display.setText(currentInput);
                newInput = false;
                break;
        }
    }
    
    private void calculate() {
        if (lastOperator.isEmpty()) {
            result = Double.parseDouble(currentInput);
        } else {
            double inputNum = Double.parseDouble(currentInput);
            switch (lastOperator) {
                case "+":
                    result += inputNum;
                    break;
                case "-":
                    result -= inputNum;
                    break;
                case "*":
                    result *= inputNum;
                    break;
                case "/":
                    if (inputNum != 0) {
                        result /= inputNum;
                    } else {
                        display.setText("Error");
                        currentInput = "";
                        result = 0;
                        lastOperator = "";
                        newInput = true;
                        return;
                    }
                    break;
                case "^":
                    result = Math.pow(result, inputNum);
                    break;
                case "mod":
                    result %= inputNum;
                    break;
            }
        }
        currentInput = String.valueOf(result);
        display.setText(currentInput);
    }
    
    private long factorial(int n) {
        if (n == 0) return 1;
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}
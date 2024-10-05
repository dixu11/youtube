package gptO1.tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FirstAttempt extends JFrame {

    private JTextField activityField;
    private JButton addButton;
    private JTextArea logArea;
    private JButton analyzeButton;

    private ArrayList<Activity> activities;

    public FirstAttempt() {
        super("Śledzenie Produktywności");

        activities = new ArrayList<>();

        // Ustawienia layoutu
        setLayout(new BorderLayout());

        // Panel górny z wprowadzaniem aktywności
        JPanel inputPanel = new JPanel(new FlowLayout());
        activityField = new JTextField(20);
        addButton = new JButton("Dodaj Aktywność");
        inputPanel.add(new JLabel("Czynność:"));
        inputPanel.add(activityField);
        inputPanel.add(addButton);

        // Obszar tekstowy do wyświetlania logów
        logArea = new JTextArea(15, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Przyciski na dole
        JPanel bottomPanel = new JPanel(new FlowLayout());
        analyzeButton = new JButton("Analizuj");
        bottomPanel.add(analyzeButton);

        // Dodawanie komponentów do okna
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Obsługa zdarzeń
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String activityName = activityField.getText().trim();
                if (!activityName.isEmpty()) {
                    Activity activity = new Activity(activityName, LocalDateTime.now());
                    activities.add(activity);
                    logArea.append(activity.toString() + "\n");
                    activityField.setText("");
                }
            }
        });

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeActivities();
            }
        });

        // Ustawienia okna
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void analyzeActivities() {
        // Prosta analiza ilości wykonanych czynności
        JOptionPane.showMessageDialog(this, "Liczba zapisanych czynności: " + activities.size());
    }

    // Klasa wewnętrzna do przechowywania informacji o aktywnościach
    class Activity {
        String name;
        LocalDateTime time;

        public Activity(String name, LocalDateTime time) {
            this.name = name;
            this.time = time;
        }

        @Override
        public String toString() {
            return time.toString() + " - " + name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FirstAttempt());
    }
}

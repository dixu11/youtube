package gptO1.tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeAttempt extends JFrame {

    private JComboBox<String> activityComboBox;
    private JButton startStopButton;
    private JTextArea logArea;
    private boolean isTracking = false;
    private Activity currentActivity;
    private ArrayList<Activity> activities;
    private HashMap<String, Duration> activityDurations;

    private TrayIcon trayIcon;

    public UpgradeAttempt() {
        super("Śledzenie Produktywności");

        activities = new ArrayList<>();
        activityDurations = new HashMap<>();

        // Ustawienia layoutu
        setLayout(new BorderLayout());

        // Panel górny z wyborem aktywności
        JPanel inputPanel = new JPanel(new FlowLayout());
        String[] activityOptions = {"Praca", "Nauka", "Odpoczynek", "Spotkania", "Inne"};
        activityComboBox = new JComboBox<>(activityOptions);
        startStopButton = new JButton("Start");
        inputPanel.add(new JLabel("Wybierz Aktywność:"));
        inputPanel.add(activityComboBox);
        inputPanel.add(startStopButton);

        // Obszar tekstowy do wyświetlania logów
        logArea = new JTextArea(15, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Przyciski na dole
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton analyzeButton = new JButton("Analizuj");
        JButton exportButton = new JButton("Eksportuj Dane");
        bottomPanel.add(analyzeButton);
        bottomPanel.add(exportButton);

        // Dodawanie komponentów do okna
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Obsługa zdarzeń
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartStop();
            }
        });

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeActivities();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportData();
            }
        });

        // Konfiguracja tray icon
        if (SystemTray.isSupported()) {
            setupSystemTray();
        }

        // Ustawienia okna
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleStartStop() {
        if (!isTracking) {
            String activityName = (String) activityComboBox.getSelectedItem();
            currentActivity = new Activity(activityName, LocalDateTime.now());
            startStopButton.setText("Stop");
            isTracking = true;
            logArea.append("Rozpoczęto: " + activityName + " o " + currentActivity.startTime + "\n");
        } else {
            currentActivity.endTime = LocalDateTime.now();
            activities.add(currentActivity);
            Duration duration = Duration.between(currentActivity.startTime, currentActivity.endTime);
            activityDurations.merge(currentActivity.name, duration, Duration::plus);
            logArea.append("Zakończono: " + currentActivity.name + " o " + currentActivity.endTime + " (Czas trwania: " + formatDuration(duration) + ")\n");
            startStopButton.setText("Start");
            isTracking = false;
        }
    }

    private void analyzeActivities() {
        StringBuilder analysis = new StringBuilder("Czas spędzony na czynnościach:\n");
        for (String name : activityDurations.keySet()) {
            analysis.append(name).append(": ").append(formatDuration(activityDurations.get(name))).append("\n");
        }
        JOptionPane.showMessageDialog(this, analysis.toString());
    }

    private void exportData() {
        // Implementacja eksportu danych do pliku CSV
        JOptionPane.showMessageDialog(this, "Funkcja eksportu nie została jeszcze zaimplementowana.");
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%d godz %d min", hours, minutes);
    }

    private void setupSystemTray() {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            PopupMenu popup = new PopupMenu();
            MenuItem showItem = new MenuItem("Pokaż");
            MenuItem exitItem = new MenuItem("Wyjdź");

            showItem.addActionListener(e -> setVisible(true));
            exitItem.addActionListener(e -> System.exit(0));

            popup.add(showItem);
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "Śledzenie Produktywności", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> setVisible(true));

            addWindowStateListener(e -> {
                if (e.getNewState() == ICONIFIED) {
                    setVisible(false);
                    try {
                        tray.add(trayIcon);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Klasa wewnętrzna do przechowywania informacji o aktywnościach
    class Activity {
        String name;
        LocalDateTime startTime;
        LocalDateTime endTime;

        public Activity(String name, LocalDateTime startTime) {
            this.name = name;
            this.startTime = startTime;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpgradeAttempt());
    }
}

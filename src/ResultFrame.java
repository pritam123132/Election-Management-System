import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResultFrame extends JFrame {

    private final String username;
    private final String role;

    private JTable table;
    private DefaultTableModel model;

    public ResultFrame(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Election Management System - Results");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadResults();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 45, 78));
        header.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        JLabel logo = new JLabel();

        try {
            ImageIcon icon = new ImageIcon("src/logo.jpeg");

            Image image = icon.getImage().getScaledInstance(
                    180, 65, Image.SCALE_SMOOTH
            );

            logo.setIcon(new ImageIcon(image));

        } catch (Exception e) {
            logo.setText("COLLEGE LOGO");
            logo.setForeground(Color.WHITE);
        }

        header.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel(
                "Election Results",
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);
        title.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        header.add(title, BorderLayout.CENTER);

        JLabel userLabel = new JLabel(
                "User: " + username,
                SwingConstants.RIGHT
        );

        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        header.add(userLabel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ================= SIDE MENU =================
        JPanel sidePanel = new JPanel();

        sidePanel.setLayout(
                new BoxLayout(sidePanel, BoxLayout.Y_AXIS)
        );

        sidePanel.setBackground(
                new Color(30, 55, 90)
        );

        sidePanel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        JButton dashboardBtn =
                createMenuButton("Dashboard");

        JButton candidateBtn =
                createMenuButton(
                        role.equals("ADMIN")
                                ? "Candidate Management"
                                : "View Candidates"
                );

        JButton resultBtn =
                createMenuButton("View Results");

        JButton resetBtn =
                createMenuButton("Reset Password");

        JButton logoutBtn =
                createMenuButton("Logout");

        sidePanel.add(dashboardBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(candidateBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(resultBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(resetBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(logoutBtn);

        mainPanel.add(
                sidePanel,
                BorderLayout.WEST
        );

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 247, 250));

        JLabel resultTitle = new JLabel(
                "Live Election Standing & Results"
        );

        resultTitle.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        resultTitle.setForeground(
                new Color(20, 45, 78)
        );

        JButton refreshBtn = new JButton("Refresh Results");

        refreshBtn.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        refreshBtn.setBackground(new Color(20, 45, 78));
        refreshBtn.setForeground(Color.WHITE);

        topBar.add(resultTitle, BorderLayout.WEST);
        topBar.add(refreshBtn, BorderLayout.EAST);

        centerPanel.add(topBar, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{
                        "Candidate Name",
                        "Party Name",
                        "Constituency",
                        "Votes",
                        "Status"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(35);

        table.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // ================= FOOTER =================
        JLabel footer = new JLabel(
                "Made by Pritam Kumar",
                SwingConstants.CENTER
        );

        footer.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 0, 8, 0
                )
        );

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);

        // ================= BUTTON ACTIONS =================

        refreshBtn.addActionListener(
                e -> loadResults()
        );

        // Dashboard
        dashboardBtn.addActionListener(e -> {

            dispose();

            if (role.equals("ADMIN")) {
                new AdminDashboard(username)
                        .setVisible(true);
            } else {
                new UserDashboard(username)
                        .setVisible(true);
            }
        });

        // Candidates
        candidateBtn.addActionListener(e -> {

            dispose();

            new CandidateFrame(
                    username,
                    role
            ).setVisible(true);
        });

        // Results
        resultBtn.addActionListener(
                e -> loadResults()
        );

        // Reset Password
        resetBtn.addActionListener(e -> {

            dispose();

            new ResetPasswordFrame(username)
                    .setVisible(true);
        });

        // Logout
        logoutBtn.addActionListener(e -> {

            dispose();

            new ElectionLogin()
                    .setVisible(true);
        });
    }

    // ================= LOAD RESULTS =================

    private void loadResults() {

        model.setRowCount(0);

        String sql = "SELECT candidate_name, party_name, constituency, votes FROM candidates ORDER BY votes DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean first = true;
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String name = rs.getString("candidate_name");
                String party = rs.getString("party_name");
                String constituency = rs.getString("constituency");
                int votes = rs.getInt("votes");

                String status = first ? "Leading" : "Declared";
                first = false;

                model.addRow(
                        new Object[]{
                                name,
                                party,
                                constituency,
                                votes,
                                status
                        }
                );
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(
                        this,
                        "No election results available in MySQL database."
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error loading results: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= MENU BUTTON =================

    private JButton createMenuButton(
            String text
    ) {

        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(220, 45)
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        return button;
    }
}

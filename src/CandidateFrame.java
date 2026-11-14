import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CandidateFrame extends JFrame {

    // Candidate data model
    public static class Candidate {
        public int id;
        public String name;
        public String party;
        public String constituency;
        public String symbol;
        public int age;
        public int votes;

        public Candidate(int id, String name, String party,
                         String constituency, String symbol,
                         int age, int votes) {
            this.id = id;
            this.name = name;
            this.party = party;
            this.constituency = constituency;
            this.symbol = symbol;
            this.age = age;
            this.votes = votes;
        }
    }

    // In-memory candidate list cache for backward compatibility
    private static final List<Candidate> candidates = new ArrayList<>();

    private final String username;
    private final String role;

    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField;
    private JTextField partyField;
    private JTextField constituencyField;
    private JTextField symbolField;
    private JTextField ageField;
    private JTextField votesField;
    private JTextField idField;

    public CandidateFrame(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Election Management System - Candidate Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadTable();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 45, 78));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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
                role.equals("ADMIN")
                        ? "Candidate Management"
                        : "View Candidates",
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        header.add(title, BorderLayout.CENTER);

        JLabel userLabel = new JLabel(
                "User: " + username,
                SwingConstants.RIGHT
        );

        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));

        header.add(userLabel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ================= SIDE MENU =================
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(30, 55, 90));
        sidePanel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        JButton dashboardBtn = createMenuButton("Dashboard");

        JButton candidateBtn = createMenuButton(
                role.equals("ADMIN")
                        ? "Candidate Management"
                        : "View Candidates"
        );

        JButton resultBtn = createMenuButton("View Results");
        JButton resetBtn = createMenuButton("Reset Password");
        JButton logoutBtn = createMenuButton("Logout");

        sidePanel.add(dashboardBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(candidateBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(resultBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(resetBtn);
        sidePanel.add(Box.createVerticalStrut(10));

        sidePanel.add(logoutBtn);

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // ================= FORM =================
        JPanel formPanel = new JPanel(new GridLayout(2, 7, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        role.equals("ADMIN")
                                ? "Candidate Details"
                                : "Candidate Information"
                )
        );

        idField = new JTextField();
        nameField = new JTextField();
        partyField = new JTextField();
        constituencyField = new JTextField();
        symbolField = new JTextField();
        ageField = new JTextField();
        votesField = new JTextField();

        idField.setEditable(false);

        formPanel.add(new JLabel("Candidate ID"));
        formPanel.add(new JLabel("Candidate Name"));
        formPanel.add(new JLabel("Party Name"));
        formPanel.add(new JLabel("Constituency"));
        formPanel.add(new JLabel("Symbol"));
        formPanel.add(new JLabel("Age"));
        formPanel.add(new JLabel("Votes"));

        formPanel.add(idField);
        formPanel.add(nameField);
        formPanel.add(partyField);
        formPanel.add(constituencyField);
        formPanel.add(symbolField);
        formPanel.add(ageField);
        formPanel.add(votesField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Candidate Name",
                        "Party Name",
                        "Constituency",
                        "Symbol",
                        "Age",
                        "Votes"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add Candidate");
        JButton updateBtn = new JButton("Update Candidate");
        JButton deleteBtn = new JButton("Delete Candidate");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(refreshBtn);

        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= FOOTER =================
        JLabel footer = new JLabel(
                "Made by Pritam Kumar",
                SwingConstants.CENTER
        );

        footer.setFont(new Font("Arial", Font.PLAIN, 13));
        footer.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 8, 0)
        );

        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // ================= USER MODE =================
        if (role.equals("USER")) {

            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);

            idField.setEnabled(false);
            nameField.setEnabled(false);
            partyField.setEnabled(false);
            constituencyField.setEnabled(false);
            symbolField.setEnabled(false);
            ageField.setEnabled(false);
            votesField.setEnabled(false);
        }

        // ================= BUTTON ACTIONS =================

        addBtn.addActionListener(e -> addCandidate());

        updateBtn.addActionListener(e -> updateCandidate());

        deleteBtn.addActionListener(e -> deleteCandidate());

        clearBtn.addActionListener(e -> clearFields());

        refreshBtn.addActionListener(e -> loadTable());

        // Table row selection
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() != -1) {

                int row = table.getSelectedRow();

                idField.setText(
                        model.getValueAt(row, 0).toString()
                );

                nameField.setText(
                        model.getValueAt(row, 1).toString()
                );

                partyField.setText(
                        model.getValueAt(row, 2).toString()
                );

                constituencyField.setText(
                        model.getValueAt(row, 3).toString()
                );

                symbolField.setText(
                        model.getValueAt(row, 4).toString()
                );

                ageField.setText(
                        model.getValueAt(row, 5).toString()
                );

                votesField.setText(
                        model.getValueAt(row, 6).toString()
                );
            }
        });

        // ================= NAVIGATION =================

        dashboardBtn.addActionListener(e -> {
            dispose();

            if (role.equals("ADMIN")) {
                new AdminDashboard(username).setVisible(true);
            } else {
                new UserDashboard(username).setVisible(true);
            }
        });

        resultBtn.addActionListener(e -> {
            dispose();
            new ResultFrame(username, role).setVisible(true);
        });

        resetBtn.addActionListener(e -> {
            dispose();
            new ResetPasswordFrame(username).setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new ElectionLogin().setVisible(true);
        });
    }

    // ================= ADD =================

    private void addCandidate() {

        try {

            String name = nameField.getText().trim();
            String party = partyField.getText().trim();
            String constituency = constituencyField.getText().trim();
            String symbol = symbolField.getText().trim();

            if (name.isEmpty()
                    || party.isEmpty()
                    || constituency.isEmpty()
                    || symbol.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all candidate details."
                );
                return;
            }

            int age = Integer.parseInt(ageField.getText().trim());
            int votes = votesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(votesField.getText().trim());

            if (age < 18 || age > 120 || votes < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Age must be 18-120 and votes must be 0 or more."
                );
                return;
            }

            String sql = "INSERT INTO candidates(candidate_name, party_name, symbol, constituency, age, votes) VALUES(?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, party);
                ps.setString(3, symbol);
                ps.setString(4, constituency);
                ps.setInt(5, age);
                ps.setInt(6, votes);

                ps.executeUpdate();

                loadTable();
                clearFields();

                JOptionPane.showMessageDialog(
                        this,
                        "Candidate added successfully to MySQL database!"
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Age and Votes must be valid numbers."
            );
        }
    }

    // ================= UPDATE =================

    private void updateCandidate() {

        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a candidate first."
            );
            return;
        }

        try {

            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String party = partyField.getText().trim();
            String constituency = constituencyField.getText().trim();
            String symbol = symbolField.getText().trim();

            if (name.isEmpty()
                    || party.isEmpty()
                    || constituency.isEmpty()
                    || symbol.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all candidate details."
                );
                return;
            }

            int age = Integer.parseInt(ageField.getText().trim());
            int votes = votesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(votesField.getText().trim());

            if (age < 18 || age > 120 || votes < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Age must be 18-120 and votes must be 0 or more."
                );
                return;
            }

            String sql = "UPDATE candidates SET candidate_name=?, party_name=?, symbol=?, constituency=?, age=?, votes=? WHERE id=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, party);
                ps.setString(3, symbol);
                ps.setString(4, constituency);
                ps.setInt(5, age);
                ps.setInt(6, votes);
                ps.setInt(7, id);

                int affected = ps.executeUpdate();

                if (affected > 0) {
                    loadTable();
                    clearFields();

                    JOptionPane.showMessageDialog(
                            this,
                            "Candidate updated successfully in MySQL database!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Candidate ID not found."
                    );
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values for Age and Votes."
            );
        }
    }

    // ================= DELETE =================

    private void deleteCandidate() {

        if (idField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a candidate first."
            );

            return;
        }

        int id;
        try {
            id = Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Candidate ID.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this candidate from MySQL database?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            String sql = "DELETE FROM candidates WHERE id=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();

                loadTable();
                clearFields();

                JOptionPane.showMessageDialog(
                        this,
                        "Candidate deleted successfully from MySQL database!"
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // ================= LOAD TABLE =================

    private void loadTable() {

        model.setRowCount(0);
        candidates.clear();

        String sql = "SELECT id, candidate_name, party_name, symbol, constituency, age, votes FROM candidates ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("candidate_name");
                String party = rs.getString("party_name");
                String symbol = rs.getString("symbol");
                String constituency = rs.getString("constituency");
                int age = rs.getInt("age");
                int votes = rs.getInt("votes");

                candidates.add(new Candidate(id, name, party, constituency, symbol, age, votes));

                model.addRow(new Object[]{
                        id,
                        name,
                        party,
                        constituency,
                        symbol,
                        age,
                        votes
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error loading candidates: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= CLEAR =================

    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        partyField.setText("");
        constituencyField.setText("");
        symbolField.setText("");
        ageField.setText("");
        votesField.setText("");

        table.clearSelection();
    }

    // ================= GET CANDIDATES =================

    public static List<Candidate> getCandidates() {
        if (candidates.isEmpty()) {
            String sql = "SELECT id, candidate_name, party_name, symbol, constituency, age, votes FROM candidates ORDER BY id";
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    candidates.add(new Candidate(
                            rs.getInt("id"),
                            rs.getString("candidate_name"),
                            rs.getString("party_name"),
                            rs.getString("constituency"),
                            rs.getString("symbol"),
                            rs.getInt("age"),
                            rs.getInt("votes")
                    ));
                }
            } catch (Exception ignored) {
            }
        }
        return candidates;
    }

    // ================= MENU BUTTON =================

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

        button.setMaximumSize(
                new Dimension(220, 45)
        );

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        return button;
    }
}

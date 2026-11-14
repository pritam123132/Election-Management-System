import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResetPasswordFrame extends JFrame {

    private final String username;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ResetPasswordFrame(String username) {
        this.username = username;

        setTitle("Election Management System - Reset Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 45, 78));
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
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
                "Reset Password",
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
                BorderFactory.createEmptyBorder(
                        20, 10, 20, 10
                )
        );

        JButton dashboardBtn =
                createMenuButton("Dashboard");

        JButton candidateBtn =
                createMenuButton(
                        username.equals("admin")
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

        // ================= RESET PANEL =================
        JPanel centerWrapper = new JPanel(
                new GridBagLayout()
        );

        centerWrapper.setBackground(
                new Color(245, 247, 250)
        );

        JPanel resetPanel = new JPanel(
                new GridBagLayout()
        );

        resetPanel.setBackground(Color.WHITE);

        resetPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),
                        BorderFactory.createEmptyBorder(
                                30, 40, 30, 40
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        JLabel heading = new JLabel(
                "Change Your Password"
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        resetPanel.add(
                heading,
                gbc
        );

        gbc.gridwidth = 1;

        // Old Password
        gbc.gridx = 0;
        gbc.gridy = 1;

        resetPanel.add(
                new JLabel("Current Password:"),
                gbc
        );

        oldPasswordField =
                new JPasswordField(20);

        gbc.gridx = 1;

        resetPanel.add(
                oldPasswordField,
                gbc
        );

        // New Password
        gbc.gridx = 0;
        gbc.gridy = 2;

        resetPanel.add(
                new JLabel("New Password:"),
                gbc
        );

        newPasswordField =
                new JPasswordField(20);

        gbc.gridx = 1;

        resetPanel.add(
                newPasswordField,
                gbc
        );

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;

        resetPanel.add(
                new JLabel("Confirm Password:"),
                gbc
        );

        confirmPasswordField =
                new JPasswordField(20);

        gbc.gridx = 1;

        resetPanel.add(
                confirmPasswordField,
                gbc
        );

        // Reset Button
        JButton resetPasswordBtn =
                new JButton("Reset Password");

        resetPasswordBtn.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        resetPanel.add(
                resetPasswordBtn,
                gbc
        );

        centerWrapper.add(resetPanel);

        mainPanel.add(
                centerWrapper,
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

        // ================= ACTION =================

        resetPasswordBtn.addActionListener(
                e -> resetPassword()
        );

        // Dashboard
        dashboardBtn.addActionListener(e -> {

            dispose();

            if (username.equals("admin")) {
                new AdminDashboard(username)
                        .setVisible(true);
            } else {
                new UserDashboard(username)
                        .setVisible(true);
            }
        });

        // Candidate
        candidateBtn.addActionListener(e -> {

            dispose();

            String role = username.equals("admin")
                    ? "ADMIN"
                    : "USER";

            new CandidateFrame(
                    username,
                    role
            ).setVisible(true);
        });

        // Results
        resultBtn.addActionListener(e -> {

            dispose();

            String role = username.equals("admin")
                    ? "ADMIN"
                    : "USER";

            new ResultFrame(
                    username,
                    role
            ).setVisible(true);
        });

        // Reset button
        resetBtn.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        this,
                        "You are already on Reset Password page."
                )
        );

        // Logout
        logoutBtn.addActionListener(e -> {

            dispose();

            new ElectionLogin()
                    .setVisible(true);
        });
    }

    // ================= RESET PASSWORD =================

    private void resetPassword() {

        String oldPassword =
                new String(
                        oldPasswordField.getPassword()
                );

        String newPassword =
                new String(
                        newPasswordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );

        if (oldPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all password fields."
            );

            return;
        }

        if (!newPassword.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "New password and confirm password do not match."
            );

            return;
        }

        if (newPassword.length() < 4) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 4 characters."
            );

            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement check = con.prepareStatement(
                     "SELECT id FROM users WHERE username = ? AND password = ?"
             )) {

            check.setString(1, username);
            check.setString(2, oldPassword);

            try (ResultSet r = check.executeQuery()) {
                if (!r.next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Current password is incorrect.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            try (PreparedStatement up = con.prepareStatement(
                    "UPDATE users SET password = ? WHERE username = ?"
            )) {
                up.setString(1, newPassword);
                up.setString(2, username);
                up.executeUpdate();
            }

            // Also sync in-memory reference
            if (username.equals("admin")) {
                ElectionLogin.adminPassword = newPassword;
            } else {
                ElectionLogin.userPassword = newPassword;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Password changed successfully in MySQL database!"
            );

            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage(),
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

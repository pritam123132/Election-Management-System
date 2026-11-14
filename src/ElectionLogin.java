import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ElectionLogin extends JFrame {

    // Default reference passwords
    public static String adminPassword = "admin123";
    public static String userPassword = "user123";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public ElectionLogin() {

        setTitle("Election Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

            ImageIcon icon =
                    new ImageIcon("src/logo.jpeg");

            Image image =
                    icon.getImage().getScaledInstance(
                            250,
                            80,
                            Image.SCALE_SMOOTH
                    );

            logo.setIcon(new ImageIcon(image));

        } catch (Exception e) {

            logo.setText("COLLEGE LOGO");
            logo.setForeground(Color.WHITE);
            logo.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            20
                    )
            );
        }

        header.add(
                logo,
                BorderLayout.WEST
        );

        JLabel headerTitle = new JLabel(
                "ELECTION MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        headerTitle.setForeground(Color.WHITE);

        headerTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        header.add(
                headerTitle,
                BorderLayout.CENTER
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // ================= LOGIN AREA =================

        JPanel centerPanel =
                new JPanel(new GridBagLayout());

        centerPanel.setBackground(
                new Color(245, 247, 250)
        );

        JPanel loginPanel =
                new JPanel(new GridBagLayout());

        loginPanel.setBackground(Color.WHITE);

        loginPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                35,
                                50,
                                35,
                                50
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ================= TITLE =================

        JLabel loginTitle =
                new JLabel(
                        "Login",
                        SwingConstants.CENTER
                );

        loginTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        loginPanel.add(
                loginTitle,
                gbc
        );

        // ================= USERNAME =================

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        loginPanel.add(
                new JLabel("Username / Voter ID:"),
                gbc
        );

        usernameField =
                new JTextField(22);

        gbc.gridx = 1;

        loginPanel.add(
                usernameField,
                gbc
        );

        // ================= PASSWORD =================

        gbc.gridx = 0;
        gbc.gridy = 2;

        loginPanel.add(
                new JLabel("Password:"),
                gbc
        );

        passwordField =
                new JPasswordField(22);

        gbc.gridx = 1;

        loginPanel.add(
                passwordField,
                gbc
        );

        // ================= ROLE =================

        gbc.gridx = 0;
        gbc.gridy = 3;

        loginPanel.add(
                new JLabel("Role:"),
                gbc
        );

        roleComboBox =
                new JComboBox<>(
                        new String[]{
                                "ADMIN",
                                "USER"
                        }
                );

        gbc.gridx = 1;

        loginPanel.add(
                roleComboBox,
                gbc
        );

        // ================= LOGIN BUTTON =================

        JButton loginButton =
                new JButton("Login");

        loginButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        loginPanel.add(
                loginButton,
                gbc
        );

        // ================= FORGOT PASSWORD =================

        JButton forgotButton =
                new JButton("Forgot Password?");

        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);

        gbc.gridy = 5;

        loginPanel.add(
                forgotButton,
                gbc
        );

        centerPanel.add(loginPanel);

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // ================= FOOTER =================

        JLabel footer =
                new JLabel(
                        "Made by Pritam Kumar",
                        SwingConstants.CENTER
                );

        footer.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        0,
                        8,
                        0
                )
        );

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);

        // ================= ACTIONS =================

        loginButton.addActionListener(
                e -> login()
        );

        passwordField.addActionListener(
                e -> login()
        );

        forgotButton.addActionListener(
                e -> showForgotPassword()
        );
    }

    // ================= LOGIN =================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String role =
                roleComboBox
                        .getSelectedItem()
                        .toString();

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }

        // Authenticate from MySQL database
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT role FROM users WHERE username = ? AND password = ? AND role = ?"
             )) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            role + " Login Successful!"
                    );

                    dispose();

                    if (role.equals("ADMIN")) {
                        new AdminDashboard(username)
                                .setVisible(true);
                    } else {
                        new UserDashboard(username)
                                .setVisible(true);
                    }
                    return;
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Connection Error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= INVALID LOGIN =================

        JOptionPane.showMessageDialog(
                this,
                "Invalid username, password or role!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ================= FORGOT PASSWORD =================

    private void showForgotPassword() {

        JOptionPane.showMessageDialog(
                this,
                "Default Demo Accounts:\n\n"
                        + "Admin Username: admin\n"
                        + "Default Admin Password: admin123\n\n"
                        + "User Username: user\n"
                        + "Default User Password: user123\n\n"
                        + "You can change passwords after logging in\n"
                        + "from the Reset Password section.",
                "Forgot Password",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ElectionLogin()
                    .setVisible(true);

        });
    }
}

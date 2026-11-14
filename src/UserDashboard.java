import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private String username;

    public UserDashboard(String username) {

        this.username = username;

        setTitle("User Dashboard - Election Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ================= HEADER =================

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 25, 10, 25
                )
        );

        JLabel logoLabel = new JLabel();

        ImageIcon icon = new ImageIcon("src/logo.jpeg");

        Image image = icon.getImage().getScaledInstance(
                700,
                120,
                Image.SCALE_SMOOTH
        );

        logoLabel.setIcon(new ImageIcon(image));
        logoLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        header.add(logoLabel, BorderLayout.CENTER);

        // ================= SIDE MENU =================

        JPanel sideMenu = new JPanel();

        sideMenu.setPreferredSize(
                new Dimension(240, 0)
        );

        sideMenu.setBackground(
                new Color(20, 43, 76)
        );

        sideMenu.setLayout(
                new BoxLayout(
                        sideMenu,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel menuTitle = new JLabel("USER PANEL");

        menuTitle.setForeground(Color.WHITE);

        menuTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        menuTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sideMenu.add(
                Box.createVerticalStrut(25)
        );

        sideMenu.add(menuTitle);

        sideMenu.add(
                Box.createVerticalStrut(30)
        );

        JButton dashboardButton =
                createMenuButton("Dashboard");

        JButton candidateButton =
                createMenuButton("View Candidates");

        JButton resultButton =
                createMenuButton("View Results");

        JButton resetButton =
                createMenuButton("Reset Password");

        JButton logoutButton =
                createMenuButton("Logout");

        sideMenu.add(dashboardButton);
        sideMenu.add(candidateButton);
        sideMenu.add(resultButton);
        sideMenu.add(resetButton);

        sideMenu.add(
                Box.createVerticalGlue()
        );

        sideMenu.add(logoutButton);

        sideMenu.add(
                Box.createVerticalStrut(30)
        );

        // ================= CENTER =================

        JPanel centerPanel =
                new JPanel(
                        new GridBagLayout()
                );

        centerPanel.setBackground(
                new Color(245, 247, 250)
        );

        JPanel contentPanel =
                new JPanel();

        contentPanel.setBackground(
                new Color(245, 247, 250)
        );

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome, " + username + "!"
                );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        welcomeLabel.setForeground(
                new Color(20, 43, 76)
        );

        welcomeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        contentPanel.add(welcomeLabel);

        contentPanel.add(
                Box.createVerticalStrut(10)
        );

        JLabel subtitle =
                new JLabel(
                        "Election Management System"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        contentPanel.add(subtitle);

        contentPanel.add(
                Box.createVerticalStrut(35)
        );

        // ================= CARDS =================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                25,
                                25
                        )
                );

        cardsPanel.setBackground(
                new Color(245, 247, 250)
        );

        cardsPanel.setPreferredSize(
                new Dimension(850, 250)
        );

        // Candidate Card

        JPanel candidateCard =
                createCard(
                        "View Candidates",
                        "View all registered election candidates"
                );

        JButton candidateButtonCard =
                new JButton("OPEN");

        styleCardButton(
                candidateButtonCard
        );

        candidateCard.add(
                candidateButtonCard
        );

        candidateButtonCard.addActionListener(
                e -> openCandidates()
        );

        // Result Card

        JPanel resultCard =
                createCard(
                        "View Results",
                        "View the current election results"
                );

        JButton resultButtonCard =
                new JButton("OPEN");

        styleCardButton(
                resultButtonCard
        );

        resultCard.add(
                resultButtonCard
        );

        resultButtonCard.addActionListener(
                e -> openResults()
        );

        // Reset Card

        JPanel resetCard =
                createCard(
                        "Reset Password",
                        "Change your account password"
                );

        JButton resetButtonCard =
                new JButton("OPEN");

        styleCardButton(
                resetButtonCard
        );

        resetCard.add(
                resetButtonCard
        );

        resetButtonCard.addActionListener(
                e -> openResetPassword()
        );

        cardsPanel.add(candidateCard);
        cardsPanel.add(resultCard);
        cardsPanel.add(resetCard);

        contentPanel.add(cardsPanel);

        centerPanel.add(contentPanel);

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
                        14
                )
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        // ================= NAVIGATION =================

        dashboardButton.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        this,
                        "You are already on the User Dashboard."
                )
        );

        candidateButton.addActionListener(
                e -> openCandidates()
        );

        resultButton.addActionListener(
                e -> openResults()
        );

        resetButton.addActionListener(
                e -> openResetPassword()
        );

        logoutButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to logout?",
                            "Logout",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice ==
                    JOptionPane.YES_OPTION) {

                dispose();

                new ElectionLogin()
                        .setVisible(true);
            }
        });

        // ================= ADD COMPONENTS =================

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        mainPanel.add(
                sideMenu,
                BorderLayout.WEST
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );

        add(mainPanel);
    }

    // ================= OPEN CANDIDATES =================

    private void openCandidates() {

        dispose();

        new CandidateFrame(
                username,
                "USER"
        ).setVisible(true);
    }

    // ================= OPEN RESULTS =================

    private void openResults() {

        dispose();

        new ResultFrame(
                username,
                "USER"
        ).setVisible(true);
    }

    // ================= RESET PASSWORD =================

    private void openResetPassword() {

        dispose();

        new ResetPasswordFrame(
                username
        ).setVisible(true);
    }

    // ================= MENU BUTTON =================

    private JButton createMenuButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(215, 48)
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

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(35, 65, 105)
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        return button;
    }

    // ================= CARD =================

    private JPanel createCard(
            String title,
            String description
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                25, 20, 25, 20
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        titleLabel.setForeground(
                new Color(20, 43, 76)
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(15)
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html><center>"
                                + description
                                + "</center></html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        descriptionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(descriptionLabel);

        card.add(
                Box.createVerticalGlue()
        );

        return card;
    }

    // ================= CARD BUTTON =================

    private void styleCardButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(20, 43, 76)
        );

        button.setFocusPainted(false);

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setMaximumSize(
                new Dimension(130, 40)
        );
    }
}
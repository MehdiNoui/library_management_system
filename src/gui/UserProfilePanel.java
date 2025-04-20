package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfilePanel extends JPanel {
  
  public UserProfilePanel() {
      setLayout(new BorderLayout());
      setBackground(new Color(245, 245, 250));
      
      // Create header panel
      createHeaderPanel();
      
      // Create profile panel with placeholder data
      createProfilePanel();
  }
  
  private void createHeaderPanel() {
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(Color.WHITE);
      headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
      JLabel titleLabel = new JLabel("My Profile");
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
      
      headerPanel.add(titleLabel, BorderLayout.WEST);
      
      add(headerPanel, BorderLayout.NORTH);
  }
  
  private void createProfilePanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBackground(new Color(245, 245, 250));
      mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
      // Profile card
      JPanel profileCard = new JPanel();
      profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
      profileCard.setBackground(Color.WHITE);
      profileCard.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
          BorderFactory.createEmptyBorder(30, 30, 30, 30)
      ));
      
      // User avatar
      JPanel avatarPanel = new JPanel();
      avatarPanel.setBackground(Color.WHITE);
      avatarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      avatarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
      
      // Create a circular avatar with user's initials or placeholder
      JLabel avatarLabel = new JLabel("JD") {
          @Override
          protected void paintComponent(Graphics g) {
              Graphics2D g2 = (Graphics2D) g.create();
              g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              g2.setColor(new Color(0, 151, 167));
              g2.fillOval(0, 0, getWidth(), getHeight());
              super.paintComponent(g);
          }
      };
      avatarLabel.setPreferredSize(new Dimension(100, 100));
      avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
      avatarLabel.setForeground(Color.WHITE);
      avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
      avatarPanel.add(avatarLabel);
      
      // User info form
      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBackground(Color.WHITE);
      formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(10, 5, 10, 5);
      
      // First Name
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 0.0;
      JLabel firstNameLabel = new JLabel("First Name:");
      firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
      formPanel.add(firstNameLabel, gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      JTextField firstNameField = new JTextField("John", 20);
      firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(firstNameField, gbc);
      
      // Last Name
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 0.0;
      JLabel lastNameLabel = new JLabel("Last Name:");
      lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
      formPanel.add(lastNameLabel, gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.weightx = 1.0;
      JTextField lastNameField = new JTextField("Doe", 20);
      lastNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(lastNameField, gbc);
      
      // Email
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.weightx = 0.0;
      JLabel emailLabel = new JLabel("Email:");
      emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
      formPanel.add(emailLabel, gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbc.weightx = 1.0;
      JTextField emailField = new JTextField("john.doe@example.com", 20);
      emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(emailField, gbc);
      
      // Password
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.weightx = 0.0;
      JLabel passwordLabel = new JLabel("Password:");
      passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
      formPanel.add(passwordLabel, gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 3;
      gbc.weightx = 1.0;
      JPasswordField passwordField = new JPasswordField("********", 20);
      passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(passwordField, gbc);
      
      // Signup Date
      gbc.gridx = 0;
      gbc.gridy = 4;
      gbc.weightx = 0.0;
      JLabel signupDateLabel = new JLabel("Signup Date:");
      signupDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
      formPanel.add(signupDateLabel, gbc);
      
      gbc.gridx = 1;
      gbc.gridy = 4;
      gbc.weightx = 1.0;
      JTextField signupDateField = new JTextField("2023-01-15", 20);
      signupDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      signupDateField.setEditable(false);
      formPanel.add(signupDateField, gbc);
      
      // Buttons
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.setBackground(Color.WHITE);
      buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JButton saveButton = new JButton("Save Changes");
      saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
      saveButton.setBackground(new Color(0, 151, 167));
      saveButton.setForeground(Color.WHITE);
      saveButton.setBorderPainted(false);
      saveButton.setFocusPainted(false);
      
      saveButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Save profile changes (to be implemented)
              JOptionPane.showMessageDialog(
                  UserProfilePanel.this,
                  "Profile updated successfully!",
                  "Success",
                  JOptionPane.INFORMATION_MESSAGE
              );
          }
      });
      
      buttonPanel.add(saveButton);
      
      // Add components to profile card
      profileCard.add(avatarPanel);
      profileCard.add(Box.createRigidArea(new Dimension(0, 30)));
      profileCard.add(formPanel);
      profileCard.add(Box.createRigidArea(new Dimension(0, 20)));
      profileCard.add(buttonPanel);
      
      // Add profile card to main panel
      mainPanel.add(profileCard, BorderLayout.CENTER);
      
      // Add main panel to this panel
      add(mainPanel, BorderLayout.CENTER);
  }
}

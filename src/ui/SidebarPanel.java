package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(220, 0));

        JButton dashboard = new JButton("Dashboard");
        JButton computers = new JButton("Computer Manager");
        JButton sessions = new JButton("Sessions History");

        dashboard.setMaximumSize(new Dimension(180, 30));
        computers.setMaximumSize(new Dimension(180, 30));
        sessions.setMaximumSize(new Dimension(180, 30));

        dashboard.addActionListener(e -> {
            // TODO: Ask MainFrame to show the Dashboard panel
            // (for example, CardLayout.show(contentPanel, "dashboard")).
            // The sidebar should not know about the panels, so pass it a callback
            // or listener from MainFrame instead of referencing the panels here.
        });

        computers.addActionListener(e -> {
            // TODO: Ask MainFrame to show the Computer Manager panel.
        });

        sessions.addActionListener(e -> {
            // TODO: Ask MainFrame to show the Sessions History panel.
        });

        add(dashboard);
        add(Box.createVerticalStrut(5));
        add(computers);
        add(Box.createVerticalStrut(5));
        add(sessions);
    }
}
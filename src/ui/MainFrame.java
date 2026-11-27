package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Internet Cafe Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        SidebarPanel sidebar = new SidebarPanel();
        ComputerManagerPanel content = new ComputerManagerPanel();

        // TODO (later): replace the direct `content` with a content panel that uses CardLayout,
        // holding Dashboard, Computer Manager, Sessions History. The sidebar buttons will
        // switch cards (see the TODOs in SidebarPanel).

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

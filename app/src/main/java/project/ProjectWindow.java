package project;

import javax.swing.*;
import java.awt.*;

public class ProjectWindow extends JFrame {

    GridBagConstraints gbc = new GridBagConstraints();

    private JPanel container = new JPanel();

    public ProjectWindow() {
        super("COMP 3220 Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        getContentPane().setPreferredSize(new Dimension(600, 600));
        setResizable(false);
        container.setLayout(new GridBagLayout());
        add(container);

        pack();
        setVisible(true);
    }
}
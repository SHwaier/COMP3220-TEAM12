package project;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        // Use the native system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set native look and feel, exiting");
            e.printStackTrace();
            return;
        }
        
        // Invoke the main Swing application
        SwingUtilities.invokeLater(ProjectWindow::new);
    }
}
package com.alphateam.app.gui;

import com.alphateam.app.base.Process;
import com.alphateam.app.configurtions.Config;
import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Application extends JPanel implements ActionListener {

    JButton pathButton;
    JFileChooser chooser;
    String choosertitle;
    JTextField tfPath;

    public Application() {

        try {
            UIManager.setLookAndFeel (new MaterialLookAndFeel( new JMarsDarkTheme()));
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace ();
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Springboot CRUD Generator");
        headingPanel.add(headingLabel);

        // Panel to define the layout. We are using GridBagLayout
        final JPanel content = new JPanel(new GridBagLayout());
        //JPanel content = this;

        // Constraints for the layout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);
        constr.anchor = GridBagConstraints.WEST;

        // Set the initial grid values to 0,0
        constr.gridx=0;
        constr.gridy=0;

        // Declare Text fields
        final JTextField hostname = new JTextField(20);
        final JTextField username = new JTextField(20);
        final JTextField password = new JPasswordField(20);
        final JTextField SID = new JTextField(5);
        final JTextField port = new JTextField(5);
        pathButton = new JButton("Choose");
        pathButton.addActionListener(this);
        tfPath = new JTextField(30);

        content.add (new JLabel("Database :"), constr);
        constr.gridx=1;
        content.add(new JComboBox<String> (new String[]{"Oracle", "MYSQL", "PostgreSQL"}), constr);
        constr.gridx=0; constr.gridy=1;

        content.add(new JLabel("Hostname :"), constr);
        constr.gridx=1;
        content.add(hostname, constr);
        constr.gridx=0; constr.gridy=2;

        content.add(new JLabel("Username :"), constr);
        constr.gridx=1;
        content.add(username, constr);
        constr.gridx=0; constr.gridy=3;

        content.add(new JLabel("Password :"), constr);
        constr.gridx=1;
        content.add(password, constr);
        constr.gridx=0; constr.gridy=4;

        content.add(new JLabel("SID :"), constr);
        constr.gridx=1;
        content.add(SID, constr);
        constr.gridx=0; constr.gridy=5;

        content.add(new JLabel("Port :"), constr);
        constr.gridx=1;
        content.add(port, constr);
        constr.gridx=0; constr.gridy=6;

        content.add(new JLabel("Export to :"), constr);
        constr.gridx=1;

        content.add(tfPath, constr);
        constr.gridx=0; constr.gridy=7;

        content.add(pathButton, constr);
        constr.gridx=0; constr.gridy= 8;


        constr.gridwidth = 2;
        //constr.gridy=7;
        constr.anchor = GridBagConstraints.CENTER;

        // Button with text "Register"
        JButton btnGenerate = new JButton("Generate");

        // add a listener to button
        btnGenerate.addMouseListener(MaterialUIMovement.getMovement(btnGenerate, MaterialColors.LIGHT_BLUE_200));
        class InfoMessage extends AbstractAction{

            public InfoMessage() {
                putValue(Action.NAME, "Generate");
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                hostname.setEnabled(false);
                username.setEnabled(false);
                password.setEnabled(false);
                SID.setEnabled(false);
                port.setEnabled(false);
                tfPath.setEnabled(false);

                Config config = new Config();
                    config.setHostname(hostname.getText());
                    config.setUsername(username.getText());
                    config.setPassword(password.getText());
                    config.setDbName(SID.getText());
                    config.setPort(port.getText());
                    config.setOutputLocation("D:\\Software Development\\crud-generator-project\\generated-projects\\gth-crud-2");
                    config.setZipFile(tfPath.getText()+"Project.zip");
                new Process().build(config);

                JOptionPane optionPane = new JOptionPane();
                optionPane.showMessageDialog(content, "This is message info", "El codigo fuente ha sido generado.", JOptionPane.INFORMATION_MESSAGE);

                hostname.setEnabled(true);
                username.setEnabled(true);
                password.setEnabled(true);
                SID.setEnabled(true);
                port.setEnabled(true);
                tfPath.setEnabled(true);
            }
        }
        btnGenerate.setAction(new InfoMessage());

        // Add label and button to panel
        content.add(btnGenerate, constr);

        add(headingPanel);
        add(content);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result;

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
            tfPath.setText(String.valueOf(chooser.getSelectedFile()));
        }
        else {
            System.out.println("No Selection ");
        }
    }

    public static void main(String s[]) {
        JFrame frame = new JFrame("Configuration");

        Application panel = new Application();
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        frame.setMinimumSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel,"Center");
        frame.setVisible(true);
    }
}
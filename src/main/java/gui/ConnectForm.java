package gui;

import core.ServiceShare;
import core.service.events.ServiceEvents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

class ImagePanel extends JPanel{
    private BufferedImage image;
    public ImagePanel(){
        try {
            image = ImageIO.read(ServiceShare.resource("images/logoConnect.gif"));
            setPreferredSize(new Dimension(1, 60));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}

public class ConnectForm extends JDialog{
    private ImagePanel imagePanel;
    private JPanel buttonsPanel;
    private JButton connectBlockButton;
    private JButton connectLocalButton;
    private Preferences userPref;
    public ConnectForm() {
        super();
        userPref = Preferences.userRoot().node(getClass().getName());

        setSize(userPref.getInt("width", 500), userPref.getInt("height", 500));
        setLocation(userPref.getInt("x", 0), userPref.getInt("y", 0));
        if(getLocation().x == 0 && getLocation().y == 0)
            setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ServiceEvents.addEvent(TypeEvent.info, "Удалена панель событий");
                userPref.putInt("width", getWidth());
                userPref.putInt("height", getHeight());
                userPref.putInt("x", getLocation().x);
                userPref.putInt("y", getLocation().y);

//                ServiceConnect.unRegister(ConnectForm.this);
//                ServiceConnect.stop();
            }
        });
        getContentPane().setLayout(new BorderLayout());

        imagePanel = new ImagePanel();
        add(imagePanel, BorderLayout.NORTH);

        buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.GREEN);
        buttonsPanel.setLayout(new GridLayout(2, 1));

        connectBlockButton = new JButton();
        connectBlockButton.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        connectBlockButton.add(new JLabel("Подключиться к блоку"), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        connectBlockButton.add(new JLabel(" IP-адрес:"), gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setEditable(true);
        DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<>();
        defaultComboBoxModel.addElement("192.168.0.1");
        defaultComboBoxModel.addElement("192.168.1.1");
        jComboBox.setModel(defaultComboBoxModel);
        connectBlockButton.add(jComboBox, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        connectBlockButton.add(new JLabel(" Логин:"), gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        connectBlockButton.add(new JTextField(null, "", 8), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        connectBlockButton.add(new JLabel("Пароль:"), gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        connectBlockButton.add(new JTextField(null, "", 8), gridBagConstraints);

        buttonsPanel.add(connectBlockButton);

        connectLocalButton = new JButton();
        connectLocalButton.add(new JLabel("Автономный режим"));
        buttonsPanel.add(connectLocalButton);

        add(buttonsPanel, BorderLayout.CENTER);

        setModal(true);
        setVisible(true);

//        ServiceConnect.register(this);
//        ServiceConnect.start();
    }
}

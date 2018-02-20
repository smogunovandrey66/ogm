package gui;

import javax.swing.*;
import java.awt.*;

public class ConnectPanel extends JPanel{
    public ConnectPanel(){
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
//        setModal(true);
        add(new JButton("Подключиться к блоку"));
//        setLocationRelativeTo(null);
        setBackground(Color.BLUE);
        setVisible(true);

    }
}

package gui;

import core.ServiceShare;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainWindow extends FrameStoreDimensions {
    private JPanel mainPanel = new JPanel() {
        private Image image;

        {
            try {
                image = ImageIO.read(ServiceShare.resource("gui/mainwindow/backgroundMorion.bmp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);
            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                    for (int y = 0; y < getHeight(); y += ih) {
                        g.drawImage(image, x, y, iw, ih, this);
                    }
                }

            }
        }
    };

    public MainWindow() {
        super();
        setTitle("Конфигуратор ОГМ-140");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createToolBar();
        createMenu();
        createMainComponents();


        revalidate();
    }

    private void createMainComponents() {
        mainPanel.setLayout(new BorderLayout());

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode board1 = new DefaultMutableTreeNode("ВС-140");
        DefaultMutableTreeNode board2 = new DefaultMutableTreeNode("КД-122");
        root.add(board1);
        root.add(board2);

        JTree tree = new JTree(root);
        tree.setRootVisible(false);
        JPanel panelTree = new JPanel();
        panelTree.add(tree);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTree, mainPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private void createToolBar() {
        /*Создаём панель инструментов*/
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));

        /*Кпопка вызова формы подключения к блоку*/
        JButton btnConnect = new JButton(new ImageIcon(ServiceShare.resource("images/btnToolBarConnect.gif")));
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConnectForm connectForm = new ConnectForm();
            }
        });
        toolBar.add(btnConnect, BorderLayout.NORTH);

        /*Кнопка вызова формы коммутации*/
        JButton btnCommutation = new JButton(new ImageIcon(ServiceShare.resource("images/btnToolBarCommutation.gif")));
        toolBar.add(btnCommutation, BorderLayout.NORTH);

        /*Добавляем панель инструментов*/
        add(toolBar, BorderLayout.WEST);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuInstuments = new JMenu("Инструменты");
        menuBar.add(menuInstuments);
        JMenuItem menuEvents = new JMenuItem("События", new ImageIcon("images/eventIcon.gif"));
        menuInstuments.add(menuEvents);
        menuEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new EventsFrame();
            }
        });
        setJMenuBar(menuBar);
    }
}

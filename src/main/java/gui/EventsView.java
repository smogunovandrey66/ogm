package gui;

import core.service.events.IEventView;
import core.service.events.ServiceEvents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventsView extends JPanel implements IEventView {
    JPanel pnlWrapper;
    JPanel pnlLeft;
    JPanel pnlRight;
    JPanel pnlCommand;
    JTable tblLog;
    JTextArea txtLog;
    JTextField txtCommand;
    JButton btnCommand;
    JToggleButton tglCommand;
    JToggleButton tglText;
    JSplitPane splitTblTxt;
    JScrollPane scrollPane;
    public EventsView(){
        createViews();
        ServiceEvents.addEvent(TypeEvent.info, "Создана панель событий");
    }

    private void showCommand(){
        pnlCommand.setVisible(tglCommand.isSelected());
    }

    private  void showTxtLog(){
        txtLog.setVisible(tglText.isSelected());
    }
    private void makeTblLog(){
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 0);
        defaultTableModel.setColumnIdentifiers(new Object[]{"Сообщение", "Дата/время"});
        tblLog = new JTable();
        tblLog.setModel(defaultTableModel);
        scrollPane = new JScrollPane(tblLog);
        scrollPane.setPreferredSize(new Dimension(10, 100));
    }

    private void createViews(){
        setLayout(new BorderLayout());

        pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        tglCommand = new JToggleButton();
        tglCommand.setText("Команды");
        tglCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showCommand();
            }
        });
        pnlLeft.add(tglCommand);
        tglText = new JToggleButton("Текст");
        tglText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showTxtLog();
            }
        });
        pnlLeft.add(tglText);
        add(pnlLeft, BorderLayout.WEST);

        pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout());
        pnlCommand = new JPanel();
        pnlCommand.setLayout(new BoxLayout(pnlCommand, BoxLayout.X_AXIS));
        txtCommand = new JTextField(null, "", 10);
        pnlCommand.add(txtCommand);
        btnCommand = new JButton("Отправить");
        pnlCommand.add(btnCommand);
        pnlRight.add(pnlCommand, BorderLayout.NORTH);
        makeTblLog();
        txtLog = new JTextArea("", 8, 8);
        txtLog.setPreferredSize(new Dimension(100, 100));
        splitTblTxt = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, txtLog);
        pnlRight.add(splitTblTxt);
        add(pnlRight, BorderLayout.CENTER);

        showCommand();
        showTxtLog();
    }

    @Override
    public void addEvent(EventRecord eventRecord) {
        ((DefaultTableModel)tblLog.getModel()).addRow(new Object[]{eventRecord.getMessage(), eventRecord.getDate()});
    }
}

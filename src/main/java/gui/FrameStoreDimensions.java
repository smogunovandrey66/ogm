package gui;

import core.service.events.ServiceEvents;
import core.ServiceSettings;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public class FrameStoreDimensions extends JFrame{
    protected Preferences userPref;
    public FrameStoreDimensions(){
        super();

        userPref = ServiceSettings.pref().node(getClass().getName());

        setSize(userPref.getInt("width", 500), userPref.getInt("height", 500));
        setLocation(userPref.getInt("x", 0), userPref.getInt("y", 0));
        setExtendedState(userPref.getInt("MAXIMIZED_BOTH", JFrame.NORMAL));
        if(getLocation().x == 0 && getLocation().y == 0)
            setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ServiceEvents.addEvent(TypeEvent.info, "Удалена панель событий");
                userPref.putInt("width", getWidth());
                userPref.putInt("height", getHeight());
                userPref.putInt("x", getLocation().x);
                userPref.putInt("y", getLocation().y);
                userPref.putInt("MAXIMIZED_BOTH", getExtendedState());
            }
        });
    }
}

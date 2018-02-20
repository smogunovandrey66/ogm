package gui;

import core.service.events.ServiceEvents;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EventsFrame extends FrameStoreDimensions{
    private EventsView panelEvents = new EventsView();
    public EventsFrame(){
        super();
        add(panelEvents);
        ServiceEvents.addView(panelEvents);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ServiceEvents.removeView(panelEvents);
            }
        });
    }
}

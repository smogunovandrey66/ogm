package core.service.events;

import gui.EventRecord;
import gui.TypeEvent;

import java.util.ArrayList;

public class ServiceEvents implements IService {
    private ArrayList<EventRecord> listEventRecords = new ArrayList<>();
    private ArrayList<IEventView> listEventViews = new ArrayList<>();
    private static ServiceEvents serviceEvents;

    private ServiceEvents() {

    }

    public void loadEvents() {

    }

    private void addViewPrivate(IEventView iEventView) {
        if (listEventViews.indexOf(iEventView) == -1) {
            listEventViews.add(iEventView);
            for (EventRecord eventRecord : listEventRecords) {
                iEventView.addEvent(eventRecord);
            }
        }
    }

    private void removeViewPrivate(IEventView iEventView) {
        listEventViews.remove(iEventView);
    }

    private void addEventPrivate(TypeEvent typeEvent, String message) {
        EventRecord eventRecord = new EventRecord(typeEvent, message);
        listEventRecords.add(eventRecord);
        for (IEventView item : listEventViews) {
            item.addEvent(eventRecord);
        }
    }

    public static void create() {
        if (serviceEvents == null) {
            serviceEvents = new ServiceEvents();
            serviceEvents.addEventPrivate(TypeEvent.info, "Создана служба событий приложения");
        }
    }

    public static void destroy() {
        if(serviceEvents != null){
            serviceEvents.listEventRecords.clear();
            serviceEvents.listEventViews.clear();
        }
    }

    /**
     * Добавляет элемент представления
     * в список поддерживаемых представлений {@link events#listEventViews}
     *
     * @param iEventView элемент представления
     */
    public static void addView(IEventView iEventView) {
        if (serviceEvents != null) {
            serviceEvents.addViewPrivate(iEventView);
        }
    }

    synchronized public static void addEvent(TypeEvent typeEvent, String messsage) {
        serviceEvents.addEventPrivate(typeEvent, messsage);
    }

    public static void removeView(IEventView iEventView) {
        serviceEvents.removeViewPrivate(iEventView);
    }

    public static void error(String format, Object ... objects){
        System.out.println();
        String s = "";
        String.format(format, objects);
    }
}

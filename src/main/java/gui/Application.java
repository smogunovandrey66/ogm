package gui;

import core.PlatformConst;
import core.service.events.ServiceEvents;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Application {
    private static void createServices() {
        ServiceEvents.create();
    }

    private static void destroyService() {
        ServiceEvents.destroy();
//        ServiceConnect.destroy();
    }

    public  static void main(String[] args){
        createServices();
        ServiceEvents.addEvent(TypeEvent.info, String.format("Запущена программа ОГМ-140 %s", PlatformConst.makeVersion()));
        MainWindow mainWindow = new MainWindow();
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Application.destroyService();
            }
        });
    }
}

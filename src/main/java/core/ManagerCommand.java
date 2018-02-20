package core;

import java.util.ArrayList;

public class ManagerCommand extends Thread{
    private ArrayList<Command> listCommand = new ArrayList<>();


    @Override
    public void run(){
        while(true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void addCommand(Command cmd) {
        synchronized (listCommand){
            notify();
        }
    };


}

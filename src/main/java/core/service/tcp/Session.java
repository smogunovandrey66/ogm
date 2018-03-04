package core.service.tcp;

import java.util.ArrayList;

public class Session extends Thread{
    private ArrayList<Command> commands;

    @Override
    public void run() {
        commands = new ArrayList<>();

        while (!Thread.interrupted()){

        }
    }

    public void sendCommand(byte[] command, IReceiveCommand receiver){
        synchronized (commands){
            commands.add(new Command(command, receiver));
        }
    }

    public void sendCommand(byte[] command){
        this.sendCommand(command, null);
    }
}

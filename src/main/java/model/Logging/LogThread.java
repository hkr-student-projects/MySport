package model.Logging;

public class LogThread extends Thread {

    private final LogEntry entry;

    public LogThread(String name, LogEntry entry){
        super(name);
        this.entry = entry;
    }

    public void run(){
        LoggerQueue.Resource.enqueue(entry);
        LoggerQueue.Resource.processLog();
    }
}

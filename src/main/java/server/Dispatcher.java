package server;

import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher extends Thread {
    BlockingQueue<String> messages;
    CopyOnWriteArrayList<PrintWriter> allWriters;

    public Dispatcher(BlockingQueue<String> queue) {
        this.messages = queue;
        this.allWriters = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = messages.take();
                sendMsgToAll(msg);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void sendMsgToAll(String msg) {
        for (PrintWriter pw : allWriters) {
            pw.println(msg);
        }
    }

    public void addToWriterList(PrintWriter pw){
        allWriters.add(pw);
    }
}
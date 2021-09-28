package server;

import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Dispatcher extends Thread {
    BlockingQueue<String> messages;
    BlockingQueue<PrintWriter> allWriters;

    public Dispatcher(BlockingQueue<String> queue) {
        this.messages = queue;
        this.allWriters = new ArrayBlockingQueue<>(200);
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
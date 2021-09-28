package server;

import java.util.concurrent.BlockingQueue;

public class Dispatcher  implements Runnable {
    BlockingQueue<String> messages;

    public Dispatcher(BlockingQueue<String> queue) {
        this.messages = queue;
    }

    @Override
    public void run() {
        try {
            String outmsg = "";
            while (true) {
                outmsg = messages.take();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }
}
package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    public  ArrayList<ClientHandler> clientHandlers;
    private  PrintWriter pw;
    private  Scanner sc;
    private String clientUserName;
    BlockingQueue<String> que;

    public ClientHandler(Scanner sc, PrintWriter pw, ArrayList<ClientHandler> clientHandlers, String clientUserName, BlockingQueue<String> que) {
        this.sc = sc;
        this.pw = pw;
        this.clientHandlers = clientHandlers;
        this.clientUserName = clientUserName;
        this.que = que;
    }

    @Override
    public void run() {
        try {
            this.protocol();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void protocol() throws InterruptedException {
        String messageFromClient="";
//        messageFromClient = sc.nextLine();
//        pw.println("Broadcast message:");

//        pw.println("server: " + messageFromClient);
        while (!messageFromClient.equals("CLOSE#")){
            messageFromClient = sc.nextLine();
            pw.println("You sent " + messageFromClient);
//            que.put(messageFromClient);
//            String str = sc.nextLine();

//            try {
//                this.whatToDo();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    /*
    public static void whatToDo() throws IOException {
        String msg = "";
        String dataString = "";
        String dataString2 = "";
        String action = "";
        pw.println("Write CONNECT#username to connect user");
        pw.println("Write SEND#username#message to send a message to a user");
        pw.println("Write SEND#*#message to send a message to all users");
        pw.println("Write CLOSE# to close connection");
        while(!msg.equals("CLOSE#")) {
            msg = sc.nextLine();
            String[] msgArr = msg.split("#");
            action = msgArr[0].toUpperCase();
            if (msgArr.length > 1) {
                dataString = msgArr[1];
            }
            if(msgArr.length > 2){
                dataString2 = msgArr[2];
            }
            switch (action) {
                case "CONNECT":
                    Client.connectClient(dataString);
                    break;
                case "SEND":
                    if(dataString.contains("*")){
                        //TODO: send message to all
                        pw.println("send msg to all");
                    }else{
                        //TODO: send message to given user/users
                        pw.println("send msg to a user or to users");
                    }
                    break;
                default:
                    msg = "CLOSE#";
            }
        }
        pw.println(msg + " hej fra server");
    }

     */

    private void closeEverything(Socket socket, PrintWriter pw, BufferedReader br) {
        removeClientHandler();
        try {
            if(pw != null){
                pw.close();
            }
            if(br != null){
                br.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

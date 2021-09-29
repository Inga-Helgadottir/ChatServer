package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private static BufferedReader br;
    private PrintWriter pw;
    private String username;

    public Client(Socket socket, String username)  {
        try {
            this.socket = socket;
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, pw, br);
        }
    }

    public void main(String[] args) throws IOException {
        //TODO: change so it checks the hardcoded users
        //TODO: make switch case to handle the users input (in clientHandler) call from here
        /*
            input possibilities client to server
                CONNECT#username
                SEND#username#message you want to send
                CLOSE#
         */
        ClientHandler.whatToDo();
//        client.listenForMsgs();
//        client.sendMsg();
    }

    public static void connectClient(String userName){
        try {
            String username = userName;
            Socket socket = new Socket("localhost", 8080);
            Client client = new Client(socket, username);
            //TODO: call the send and listen to messages here
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void sendMsg(){
//        pw.println(username);
//
//        while(socket.isConnected()){
//            String messageToSend = scan.nextLine();
//            pw.println(username + ": " + messageToSend);
//        }
//    }


//    public void listenForMsgs(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String msgFromGroupChat;
//                while(socket.isConnected()){
//                    try {
//                        msgFromGroupChat = br.readLine();
//                        pw.println(msgFromGroupChat);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    private void closeEverything(Socket socket, PrintWriter pw, BufferedReader br) {
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

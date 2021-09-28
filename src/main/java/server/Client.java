package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner scan;
    private PrintWriter pw;
    private String username;

    public Client(Socket socket, String username)  {
        try {
            this.socket = socket;
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            this.scan = new Scanner(socket.getInputStream());
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, pw, scan);
        }
    }

    public static void main(String[] args) throws IOException {
        //TODO: change so it checks the hardcoded users
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 8080);
        Client client = new Client(socket, username);
        client.listenForMsgs();
        client.sendMsg();
    }

    public void sendMsg(){
        try {
            pw.println(username);

            while(socket.isConnected()){
                String messageToSend = scan.nextLine();
                pw.println(username + ": " + messageToSend);
            }
        } catch (Exception e) {
            closeEverything(socket, pw, scan);
        }
    }

    public void listenForMsgs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = scan.nextLine();
                        pw.println(msgFromGroupChat);
                    }catch(Exception e){
                        closeEverything(socket, pw, scan);
                    }
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, PrintWriter pw, Scanner scan) {
        try {
            if(pw != null){
                pw.close();
            }
            if(scan != null){
                scan.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

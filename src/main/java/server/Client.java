package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static BufferedReader br;
    private static PrintWriter pw;
    private String username;
    private Scanner keyboard;

    public Client(Socket socket, String username)  {
        try {
            this.socket = socket;
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.keyboard = new Scanner(System.in);
        } catch (IOException e) {
            closeEverything(socket, pw, br);
        }
    }

    public static void main(String[] args) {
//        String username = args[0];
        //TODO: change so it checks the hardcoded users
        //TODO: make switch case to handle the users input (in clientHandler) call from here
        /*
            input possibilities client to server
                CONNECT#username
                SEND#username#message you want to send
                SEND#username, anotherUsername#message you want to send
                SEND#*#message you want to send to all
                CLOSE#
         */
        connectClient("inga");
    }

    public static void connectClient(String userName){
        try {

            String returnMsg="";
            Scanner scanner = new Scanner(System.in);
            Socket socket = new Socket("localhost", 8080);
            Scanner scan = new Scanner(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("CLIENT: indtast brugernavn:");
            String username = scanner.nextLine();
            pw.println("CONNECT#"+username);
            returnMsg = scan.nextLine();
            while(!returnMsg.equals("CLOSE#")){
                pw.println(scanner.nextLine());
                returnMsg = scan.nextLine();
                System.out.println("SERVERsend: "+returnMsg);
            }
            System.out.println(returnMsg);
            socket.close();


            //TODO: call the send and listen to messages here
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

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

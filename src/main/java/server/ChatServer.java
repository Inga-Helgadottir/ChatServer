package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatServer
{

    private ServerSocket ss;

    //Call server with arguments like this: 8088
    public static void main(String[] args) throws UnknownHostException
    {
        int port = 8088;

        try
        {
            if (args.length == 1)
            {
                port = Integer.parseInt(args[0]);
            } else
            {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne)
        {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

    }


    private void startServer(int port) throws IOException
    {
        ss = new ServerSocket(port);
        System.out.println("Chat-server started - listening on" + port);

        while (true)
        {
            System.out.println("Waiting for user");
            Socket socket = ss.accept();
            System.out.println("User --- connected");
            //TODO: ved "user --- connected" skal --- udfyldes med den givne users navn

        }

    }

    private void handleClient(Socket socket) throws IOException
    {

        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
        pw.println("Welcome to the chat-server. Type 'quit' to disconnect");
        pw.println("You can now start chatting!");
        String message = scanner.nextLine();

        while (!message.equals("quit"))
        {
            //TODO: Hvad skal chatserveren kunne?
            //TODO: En "who is online" kommando, som returnerer en string med connected users og printer den til klienten

        }

        pw.println("Successfully disconnected from server.");
        socket.close();
    }

    public void startServer()
    {
        while (!ss.isClosed())
        {
            try
            {
                Socket s = ss.accept();
                System.out.println("A new client has connected!");
                ClientHandler ch = new ClientHandler(s);

                Thread t = new Thread(ch);
                t.start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void closeServerSocket()
    {
        try
        {
            if (ss != null)
            {
                ss.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

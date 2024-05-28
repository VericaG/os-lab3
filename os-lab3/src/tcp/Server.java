package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private int port;
    public Server(int port){
        this.port=port;
    }

    @Override
    public void run() {
        System.out.println("Server starting");
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server is running");
        while(true){
            Socket socket=null;
            try {
                socket=serverSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER: new client");
            new Worker(socket).start();

        }
    }

    public static void main(String[] args) {
        Server server=new Server(6400);
        server.start();
    }

    //    public static void main(String[] args) {
    //        Server server=new Server(7000);
    //        server.start();
    //    }
}
package udp;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {

    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public Client(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, serverPort);

        try {
            socket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length, address, serverPort);
            socket.receive(packet);
            System.out.println(new String(packet.getData(), 0, packet.getLength()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String serverName =  "localhost";


        Client loginClient = new Client(serverName, 4445, "login");
        loginClient.start();
        try {
            loginClient.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Client testClient1 = new Client(serverName, 4445, "test mssg");
        testClient1.start();
        try {
            testClient1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Client testClient2 = new Client(serverName, 4445, "test mssg");
        testClient2.start();
        try {
            testClient2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Client logoutClient = new Client(serverName, 4445, "logout");
        logoutClient.start();
        //        Client client = new Client(System.getenv("SERVER_NAME"), Integer.parseInt(System.getenv("SERVER_PORT")), "login");
//        client.start();
    }

}
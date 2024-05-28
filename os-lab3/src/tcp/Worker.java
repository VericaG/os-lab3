package tcp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{

    private Socket socket;
    private static int counter=0;
    private static Lock lock=new ReentrantLock();
    private int localCounter;
    public Worker(Socket socket){
        this.socket=socket;
        localCounter=0;
    }
    @Override
    public void run() {
        BufferedReader reader=null;
        BufferedWriter writer=null;
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            List<String> input=new ArrayList<>();
            String line;

            while(!(line=reader.readLine()).equals("")){
                input.add(line);
            }
            String firstLine=input.get(0);
            if(!firstLine.equals("login")){
                reader.close();
                writer.close();
                this.socket.close();
            }
            localCounter++;
            writer.write("logged in"+"\n");
            for(int i=1;i<input.size();i++){
                if(input.get(i).equals("logout")){
                    writer.write("logged out \n");
                    localCounter++;
                    break;
                }
                writer.write(input.get(i)+"\n");
                localCounter++;
            }
            System.out.println("here");
            lock.lock();
            counter+=localCounter;
            writer.write(counter+"\n");
            lock.unlock();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader!=null)
                    reader.close();
                if(writer!=null)
                    writer.close();
                if(!this.socket.isClosed())
                    this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
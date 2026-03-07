import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class TermiTalk {
    static ServerSocket server;
    static Socket socket;
    static TermiTalk talk = new  TermiTalk();
    public static void main(String[] args) throws IOException, URISyntaxException {
        talk.startServer();
        talk.chat();
    }
    void startServer() {
        try {
            server = new ServerSocket(8888);
            System.out.println("Server started. Waiting for client...");
            socket = server.accept();
            System.out.println("Client connected!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    void chat() throws IOException, URISyntaxException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Thread receiveThread = new Thread(() -> {
            try{
                String msg = " ";
                while(true){
                    msg = in.readLine();
                    if(msg.equalsIgnoreCase("server.exit"))
                    System.exit(0);
                    if(msg.equalsIgnoreCase("server.open-chrome"))
                        Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
                    if(msg.equalsIgnoreCase("server.open-music"))
                        Desktop.getDesktop().browse(new URI("https://music.youtube.com"));
                    System.out.println("Client: " + msg);
                }
            } catch(Exception e) {
                System.out.println("Error");
            }
        });
        receiveThread.start();
        Thread sendThread  = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            String msg = " ";
            while(true){
                msg = sc.nextLine();
                out.println("Server: " + msg);
            }
        });
        sendThread.start();
    }
}
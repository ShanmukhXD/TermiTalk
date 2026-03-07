import java.net.*;
import java.io.*;
import java.util.*;

public class TermiTalk {
    static ServerSocket server;
    static Socket socket;
    static TermiTalk talk = new  TermiTalk();
    public static void main(String[] args) throws IOException {
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
            e.printStackTrace();
        }
    }
    void chat() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);
        String msg = " ";
        while (true) {
            msg = in.readLine();
            System.out.println("Client: " + msg);
            msg = sc.nextLine();
            out.println("Server: " + msg);
        }
    }
}
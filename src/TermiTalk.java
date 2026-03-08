import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.net.URI;

public class TermiTalk {
    public static void main(String[] args) {
        TermiTalk talk = new TermiTalk();
        talk.runServer();
    }

    void runServer() {
        try (ServerSocket server = new ServerSocket(8888)) {
            System.out.println("Server started. Waiting for client...");
            try (Socket socket = server.accept()) {
                String clientIP = socket.getInetAddress().getHostAddress();
                System.out.println("Connected from: " + clientIP);
                if (!(clientIP.equals("192.168.29.142") || clientIP.equals("192.168.29.229"))) {
                    System.out.println("Unauthorized client. Closing connection.");
                    return;
                }
                System.out.println("Client connected!");
                chat(socket);
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    void chat(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            Thread receiveThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.startsWith("server.")) {
                            String cmd = msg.substring(7).toLowerCase();
                            switch (cmd) {
                                case "exit":
                                    System.exit(0);
                                break;
                                case "open-chrome" :
                                    new ProcessBuilder("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe").start();
                                break;
                                case "open-music" :
                                    if (Desktop.isDesktopSupported())
                                        Desktop.getDesktop().browse(new URI("https://music.youtube.com"));
                                break;
                            }
                        }
                        System.out.println("Client: " + msg);
                    }
                } catch (Exception e) {
                    System.out.println("Receive thread error: " + e.getMessage());
                }
            });
            receiveThread.start();

            Thread sendThread = new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String msg = sc.nextLine();
                    out.println(msg);
                }
            });
            sendThread.start();

            receiveThread.join();
            sendThread.join();

        } catch (Exception e) {
            System.out.println("Chat error: " + e.getMessage());
        }
    }
}

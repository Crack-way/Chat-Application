import java.net.*;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server() {

        try {
            server = new ServerSocket(7777);

            System.out.println("Server is ready to accept connection");
            System.out.println("waiting....");
            socket = server.accept();

            // for reading data from the client.
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // for sending data to the client.
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void startReading() {

        // Read the data

        Runnable r1 = () -> {
            System.out.println("Start reading");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client want to terminated");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);

                }
            }

            catch (Exception e) {

                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();

    }

    public void startWriting() {

        // take the data from user and send

        Runnable r2 = () -> {

            System.out.println("Writer started");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();

                        break;
                    }
                }
            }

            catch (Exception e) {
                System.out.println("Connection closed");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {

        new Server();

    }
}
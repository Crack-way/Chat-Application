import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {

        try {
            System.out.println("Sending Request to server");
            socket = new Socket("localhost", 7777);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            System.out.println("connection closed");
        }

    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

                System.out.println("key released" + e.getKeyCode());

                if (e.getKeyCode() == 10) {

                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            }

        });
    }

    private void createGUI() {

        // Create GUI...
        this.setTitle("Client Message[END]");
        this.setSize(600, 600);
        // Making the windows in center
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon());
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // for border layout

        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {

        // Read the data

        Runnable r1 = () -> {
            System.out.println("Start reading in client");

            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server want to terminated");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);

                        socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);

                    messageArea.append("Server:" + msg + "\n");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        };

        new Thread(r1).start();

    }

    // If you want to do chatting from console based.
    // public void startWriting() {

    // // take the data from user and send

    // Runnable r2 = () -> {
    // System.out.println("Writer started in client.");
    // try {

    // while (!socket.isClosed()) {

    // BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
    // String content = br1.readLine();

    // out.println(content);
    // out.flush();

    // if (content.equals("exit")) {
    // socket.close();
    // break;
    // }
    // }

    // } catch (Exception e) {
    // System.out.println("Connection closed");
    // }

    // };

    // new Thread(r2).start();

    // }

    public static void main(String[] args) {

        System.out.println("This is client..");

        new Client();

    }
}
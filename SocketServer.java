
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    public static final int PORT_NUMBER = 4020;

    protected Socket socket;

    private SocketServer(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {
        InputStream in = null;
//        OutputStream out = null;
        try {
            in = socket.getInputStream();
//            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;
            while ((request = br.readLine()) != null) {
                System.out.println("Message received:" + request);
                request += '\n';
//                out.write(request.getBytes());
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
//                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("SocketServer Example");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                /**
                 * create a new {@link SocketServer} object for each connection
                 * this will allow multiple client connections
                 */
                Socket s = server.accept();
                new SocketServer(s);
                new Socket_sender(s);
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

class Socket_sender implements Runnable {
    Socket s;
    Thread t;

    Socket_sender(Socket socket) {
        s = socket;
        t = new Thread(this, String.valueOf(s));
        t.start();
    }

    @Override
    public void run() {
        PrintWriter out2 = null;
        try {
            out2 = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("server: ");
            String userInput = null;
            try {
                userInput = stdIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /** Exit on 'q' char sent */
            if ("q".equals(userInput)) {
                break;
            }
            out2.println(userInput);
                System.out.println("server: " + userInput);
        }


    }
}


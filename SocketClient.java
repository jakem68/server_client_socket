import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    public static void main(String args[]) {
        String host = "127.0.0.1";
        int port = 4020;
        new SocketClient(host, port);
    }

    public SocketClient(String host, int port) {
        try {
            String serverHostname = new String("127.0.0.1");

            System.out.println("Connecting to host " + serverHostname + " on port " + port + ".");

            Socket echoSocket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                echoSocket = new Socket(serverHostname, 4020);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }
            new Socket_receiver(in);

            /** {@link UnknownHost} object used to read from console */
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("client: ");
                String userInput = stdIn.readLine();
                /** Exit on 'q' char sent */
                if ("q".equals(userInput)) {
                    break;
                }
                out.println(userInput);
//                System.out.println("server: " + in.readLine());
            }

            /** Closing all the resources */
            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Socket_receiver implements Runnable {
    BufferedReader in;
    //    Socket s;
    Thread t;

    Socket_receiver(BufferedReader in_from_server) {
//        s = Socket;
        in = in_from_server;
        t = new Thread(this, String.valueOf(in));
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("server: " + in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
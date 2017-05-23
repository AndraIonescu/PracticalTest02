package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private int port;

    private List<String> data = new ArrayList();

    private EditText serverTextEditText;

    public ServerThread(int port) {

        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();

        }
        Log.v(Constants.TAG, "stopServer() method invoked");
    }

    public synchronized void setData(String ora) {
        this.data.add(ora);
        Log.d(Constants.TAG, data.toString());
    }

    public synchronized List<String> getData() {
        return data;
    }

    public synchronized void removeData(String ora) {
        Log.d(Constants.TAG, ora);
        this.data.remove(ora);
        Log.d(Constants.TAG, data.toString());
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: "+ioException.getMessage());

        }
    }

    private class CommunicationThread extends Thread {

        private Socket socket;

        public CommunicationThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
                PrintWriter printWriter = Utilities.getWriter(socket);
                printWriter.println(serverTextEditText.getText().toString());
                socket.close();
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            }
        }
    }

}

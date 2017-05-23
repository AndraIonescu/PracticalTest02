package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    String serverAddr = "utcnist.colorado.edu";
    int port;
    TextView responseView;
    String queryStr;

    public ClientAsyncTask(int port,  String queryStr, TextView responseView) {
        this.serverAddr = serverAddr;
        this.port = port;
        this.queryStr = queryStr;
        this.responseView = responseView;
    }

    @Override
    protected Void doInBackground(String... params) {

        Socket socket;
        String response = "";
        try {
            socket = new Socket(serverAddr, 13);
//            PrintWriter clientPr = Utilities.getWriter(socket);
//            clientPr.write(queryStr+"\n");
//            clientPr.flush();

            BufferedReader clientBr = Utilities.getReader(socket);
            String currentLine;
            int i = 0;

            while ((currentLine = clientBr.readLine()) != null) {
                    publishProgress(currentLine);
            }

            Log.i(Constants.TAG, currentLine);

            //while (!socket.isClosed()) {
//            response += clientBr.readLine();
            //}
//            this.publishProgress(response);

            socket.close();

        } catch (Exception e) {e.printStackTrace();}

        return null;
    }

    @Override
    protected void onPreExecute() {
        responseView.setText("");

    }

    @Override
    protected void onProgressUpdate(String... progress) {
        responseView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}

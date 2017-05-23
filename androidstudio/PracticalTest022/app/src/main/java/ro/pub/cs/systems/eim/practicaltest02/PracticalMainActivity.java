package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalMainActivity extends AppCompatActivity {

    EditText ora;
    EditText port;
    TextView response;
    Button start;
    Button stop;
    Button set;
    Button reset;
    Button poll;
    ServerThread serverThread;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private SetClickListener setClickListener = new SetClickListener();
    private ResetClickListener resetClickListener = new ResetClickListener();
    private PollClickListener pollClickListener = new PollClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_main);

        this.ora = (EditText) findViewById(R.id.editText);
        this.port = (EditText) findViewById(R.id.editText2);

        this.response = (TextView) findViewById(R.id.response);

        this.start = (Button) findViewById(R.id.start);
        this.stop = (Button) findViewById(R.id.stop);
        this.set = (Button) findViewById(R.id.set);
        this.reset = (Button) findViewById(R.id.reset);
        this.poll = (Button) findViewById(R.id.poll);

        start.setOnClickListener(buttonClickListener);
        stop.setOnClickListener(buttonClickListener);
        set.setOnClickListener(setClickListener);
        reset.setOnClickListener(resetClickListener);
        poll.setOnClickListener(pollClickListener);

    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
    }

    private class PollClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AsyncTask clientAsyncTask = new ClientAsyncTask(Integer.parseInt(port.getText().toString()), ora.getText().toString(), response).execute();
        }
    }

    private class ResetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String hour = ora.getText().toString();

            if (serverThread.getData().contains(hour)) {
                serverThread.removeData(hour);
            }
        }
    }

    private class SetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String hour = ora.getText().toString();

            if (!serverThread.getData().contains(hour)) {
                serverThread.setData(hour);
            }
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (start.equals((Button) view)) {
                Log.d(Constants.TAG, "apas buton");
                serverThread = new ServerThread(Integer.parseInt(port.getText().toString()));
                serverThread.startServer();
            }


            if (stop.equals((Button) view)) {
                serverThread.stopServer();
            }
        }

    }
}

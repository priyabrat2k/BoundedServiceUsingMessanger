package net.appifiedtech.boundedserviceusingmessanger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isBinded = false;
    private Messenger messenger;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textViewStatus);
    }

    public void getSecondMessage(View view) {
        Message message = Message.obtain(null,MyMessangerService.JOB_2);
        message.replyTo = new Messenger(new ReplyMessageHandler());
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getFirstMessage(View view) {
        Message message = Message.obtain(null,MyMessangerService.JOB_1);
        message.replyTo = new Messenger(new ReplyMessageHandler());
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            isBinded = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(!isBinded){
            Intent intent = new Intent(MainActivity.this,MyMessangerService.class);
            bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
            isBinded = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBinded){
            unbindService(serviceConnection);
            isBinded = false;
        }
    }

    public class ReplyMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            String messageStr ;

            switch (msg.what){
                case MyMessangerService.JOB_1_REPLY:
                    messageStr = msg.getData().getString("reply_message");
                    textView.setText(messageStr);
                    break;
                case MyMessangerService.JOB_2_REPLY:
                    messageStr = msg.getData().getString("reply_message");
                    textView.setText(messageStr);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}

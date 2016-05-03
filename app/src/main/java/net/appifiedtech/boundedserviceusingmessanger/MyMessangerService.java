package net.appifiedtech.boundedserviceusingmessanger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by Priyabrat on 03-05-2016.
 */
public class MyMessangerService extends Service {

    public static final int JOB_1 = 1;
    public static final int JOB_2 = 2;
    public static final int JOB_1_REPLY = 3;
    public static final int JOB_2_REPLY = 4;

    private Messenger messenger = new Messenger(new IncomingMessageHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    public class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Message message;
            String messageStr;
            Bundle bundle = new Bundle();
            switch (msg.what)
            {
                case JOB_1:
                    messageStr = "This is the 1st message from service";
                    message = Message.obtain(null,JOB_1_REPLY);
                    bundle.putString("reply_message",messageStr);
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case JOB_2:
                    messageStr = "This is 2nd message from service";
                    message  = Message.obtain(null,JOB_2_REPLY);
                    bundle.putString("reply_message",messageStr);
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}

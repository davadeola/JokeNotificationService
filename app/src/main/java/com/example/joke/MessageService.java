package com.example.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MessageService extends IntentService {

    public static final String EXTRA_MESSAGE = "Message";
    private Handler handler;
    public static  final  int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID="sendJoke";
    private NotificationManagerCompat notificationManagerCompat;

//    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        handler = new Handler();
//
//
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }

    public MessageService() {
        super("MessageService");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {

        createNotificationChannel();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        synchronized (this){
            try {
                wait(1000);
            }catch (InterruptedException error){
                error.printStackTrace();
            }
        }

        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
        
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notification1", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager jokeManager = getSystemService(NotificationManager.class);
        jokeManager.createNotificationChannel(channel);

    }

    private void showText(final String text) {
        //Log.v("DelayedMessageService", "What is the secret of the comedy "+text);


        Intent i = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_joke)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .build();

       // NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);



//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}

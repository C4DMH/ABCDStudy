package gwicks.com.abcdstudy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import gwicks.com.abcdstudy.Setup.FinishInstallScreen;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String workName = "Work";

    private SharedPreferences prefs;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: ");


        String title = "EARS EMA";
        String message = "Please complete another EMA";

        Log.d(TAG, "onMessageReceived: message: " + message + " titel: " + title);
        Log.d(TAG, "onMessageReceived: all: " + remoteMessage.getData());

        boolean bool = Boolean.parseBoolean(remoteMessage.getData().get("reset"));
        Log.d(TAG, "onMessageReceived: boolean is:  " + bool);

        // The reset boolean = TRUE, so we need to restart the app, not start the EMA's

        if(bool){
            sendResetNotification("EARS TOOL", "Please press to restart");

            // attempt to restart the app
            WorkManager mWorkManager;

            mWorkManager = WorkManager.getInstance(this);
            PeriodicWorkRequest mRequest = new PeriodicWorkRequest.Builder(WorkManagerUsage.class, 30, TimeUnit.MINUTES ).build();
            mWorkManager.enqueueUniquePeriodicWork(workName, ExistingPeriodicWorkPolicy.REPLACE.REPLACE,mRequest);




        }else{
            //sendNotification(title, message);
        }



        // TODO why is this not called everytime???? Confused
        // TODO does this only happen when in foreground?
        // TODO: seems to be only when in foreground

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.

        //TODO if the app is in the foreground, I need to do something here to call sendNotification(), because it will not hire, probs not much chance of that happening though
        // TODO the data key values are caught in MainActivity, should be easy to do something with them

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                //scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title1 = remoteMessage.getNotification().getTitle();
            String message2 = remoteMessage.getNotification().getBody();
            Log.d(TAG, "onMessageReceived: message: " + message2 + " titel: " + title1);
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //Log.d(TAG, "onMessageReceived: data: " + remoteMessage.getData().toString());
        }


    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }



    private void sendResetNotification(String title, String messageBody){
        Random random = new Random();

        int nxt = random.nextInt(99);
        Log.d(TAG, "sendNotification: what the actual fuck");
        Intent intent = new Intent(this, FinishInstallScreen.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, nxt, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "CHANNEL_ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.noti_icon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
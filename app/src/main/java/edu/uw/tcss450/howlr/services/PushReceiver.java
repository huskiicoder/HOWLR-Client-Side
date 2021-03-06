package edu.uw.tcss450.howlr.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;


import edu.uw.tcss450.howlr.AuthActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.ui.friends.Friends;
import edu.uw.tcss450.howlr.ui.messages.chats.ChatMessage;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * The class for receiving push notifications.
 */
public class PushReceiver extends BroadcastReceiver {

    /**
     * The notification for receiving a new message from Pushy.
     */
    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";
    public static final String RECEIVED_NEW_FRIEND = "new friend request from pushy";

    /**
     * The channel ID.
     */
    private static final String CHANNEL_ID = "1";

    /**
     * The method for what happens when a pushy message is received.
     * @param context The context
     * @param intent The intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //The following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String typeOfMessage = intent.getStringExtra("type");

        if (typeOfMessage.equals("msg")) {
            ChatMessage message = null;
            int chatId = -1;
            try {
                String firstName = intent.getStringExtra("firstname");
                String lastName = intent.getStringExtra("lastname");
                System.out.println(intent.getStringExtra("message"));
                message = ChatMessage.createFromJsonString(intent.getStringExtra("message"),
                        firstName, lastName);
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Message received in foreground: " + message);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_MESSAGE);
                i.putExtra("chatMessage", message);
                i.putExtra("chatid", chatId);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        .setContentTitle("Message from: " + message.getSender())
                        .setContentText(message.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        } else if (typeOfMessage.equals("contact")) {
            Friends friends = null;
            try {
                friends = Friends.createFromJsonString(intent.getStringExtra("sender"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Friend request received in foreground: " + friends);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_FRIEND);
                i.putExtra("invitation", friends);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + friends.getFirstName() + " " + friends.getLastName());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        .setContentTitle("Friend Request from: " + friends.getFirstName() + " " + friends.getLastName())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        }
    }
}
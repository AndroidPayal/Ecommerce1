package payal.cluebix.www.ecommerce.Handlers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import payal.cluebix.www.ecommerce.CenterActivity;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by CG-DTE on 26-07-2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "notifiscreen";
    RemoteMessage remoteMessage1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        this.remoteMessage1=remoteMessage;
        //Displaying data in log
        //It is optional

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));//remoteMessage.getNotification().getBody()
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());

        //Calling method to generate notification
       sendNotification(remoteMessage.getData().get("message"));
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
       Intent intent=null;
        Log.d("payalji",""+messageBody.charAt(messageBody.length()-1));
        switch (0)//Integer.parseInt(messageBody.charAt(messageBody.length()-1)+"")
        {
        /*    case 0:
                intent = new Intent(this,Leave_request_admin.class);
                break;
            case 1:
                intent= new Intent(this,Task_Assign.class);
                break;
            case 2:
                intent=new Intent(this,Task_employee.class);
                break;
            case 3:
                intent=new Intent(this,Status_Leave.class);
                break;*/
            default:
                intent= new Intent(this,CenterActivity.class);
                intent.putExtra("cartTransition","dash");
        }

       intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
       PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
         PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo2)
               .setContentTitle("MultiVendor Application")//remoteMessage1.getNotification().getTitle()
                .setContentText(messageBody.substring(0,messageBody.length()))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
           .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
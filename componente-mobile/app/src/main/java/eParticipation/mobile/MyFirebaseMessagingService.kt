package eParticipation.mobile

import android.app.NotificationManager
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification?.body != null) {
            sendNotification(remoteMessage.notification!!.body)
        } else {
            sendNotification("El body es null")
        }
        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = getActivity(
            this, 0 /* Request code */, intent,
            FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "Firebase_MSG"
    }
}

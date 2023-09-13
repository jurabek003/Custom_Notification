package uz.turgunboyevjurabek.customnotification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.turgunboyevjurabek.customnotification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var notificationManager: NotificationManagerCompat
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        binding.button.setOnClickListener {
            notificationManager.notify(1,notifyBuilder())
        }

    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun notifyBuilder(): Notification {
        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed)
        val expandedView = RemoteViews(packageName, R.layout.notification_expanded)
        val clickIntent = Intent(this, NotificationReceiver::class.java)
        val clickPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(this, 0, clickIntent, FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, clickIntent, FLAG_NO_CREATE)
        }

        collapsedView.setTextViewText(R.id.textViewCollapse, "This is Collapse!")

        expandedView.setImageViewResource(R.id.image_view_expanded,
            R.drawable.ic_launcher_background)
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent)

        return NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()).build()
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name = "Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
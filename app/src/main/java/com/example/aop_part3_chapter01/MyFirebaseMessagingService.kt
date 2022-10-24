package com.example.aop_part3_chapter01

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
         createNotificationChannel()
    }

   override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        createNotificationChannel()

        val type = remoteMessage().data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = message.data["title"]
        val message = message.data["message"]
        
        type ?:return


        NotificationManagerCompat.from(this)
            .notify(1, createNotification(type.id, title, message))
    }
    
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION
            
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }private fun createNotification(type: NotificationType,
                                       title: String?,
                                       message: String?)
        : Notification{
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("notificationType", "${type.title}타입")
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)
            val noticationBuiler =  NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(paddingIntent)
                .setAutoCancle(true)

            when(type){
                NotificationType.NORMAL ->Unit
                NotificationType.EXPANDABLE->
                {
                    noticationBuiler.setStyle{
                        NotificationCompat.BigTextStyle().bigText(
                           "😀 😃 😄 😁 😆 😅 😂 🤣 🥲 🥹 ☺️ 😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚 😋 😛 😝 😜 🤪 🤨 🧐 🤓 😎 🥸 🤩 🥳 😏 😒 😞 😔 😟 😕 🙁"
                        )
                    }
                }
                NotificationType.CUSTOM->{
                    noticationBuiler.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(
                            RemoteViews(
                                packageName,
                                R.layout.view_custom_notification
                            ).apply{
                                setTextViewText(R.id.title, title)
                                setTextViewText(R.id.message, message)
                            }
                        )
                }
            }
            return notificationBuilder.build()
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }

    companion object{
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID  = "Channel Id"
    }  
}

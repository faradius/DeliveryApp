package com.alex.deliveryapp.services

import com.alex.deliveryapp.channel.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingClient: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        //Es un mapa de valores
        val data = remoteMessage.data
        val title = data["title"]
        val body = data["body"]
        val idNotification = data["id_notification"]

        if (!title.isNullOrBlank() && !body.isNullOrBlank() && !idNotification.isNullOrBlank()){
            showNotification(title,body,idNotification)
        }
    }

    private fun showNotification(title:String, body:String, idNotification:String){
        val helper = NotificationHelper(baseContext)
        val builder = helper.getNotification(title, body)
        val id = idNotification.toInt()
        helper.getManager().notify(id, builder.build())
    }
}
package ru.spbau.chat

import ru.spbau.grpc.chat.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageGetter {
    private fun printMessage(message: Message) {
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(message.utcTime)
        println("<${sdf.format(resultdate)}> [${message.senderName}] ${message.text}")
    }

    @Synchronized fun addMessage(message: Message) {
        printMessage(message)
    }
}
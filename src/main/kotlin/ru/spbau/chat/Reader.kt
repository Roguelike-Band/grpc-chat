package ru.spbau.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spbau.grpc.chat.Message

class Reader(private val name: String, private val messageGetter: MessageGetter) {
    fun getInputFlow(): Flow<Message> = flow {
        while (true) {
            val message = readLine()
            val protobufMessage = Message.newBuilder()
                .setText(message)
                .setSenderName(name)
                .setUtcTime(System.currentTimeMillis())
                .build()
            messageGetter.addMessage(protobufMessage)
            emit(protobufMessage)
        }
    }
}
package ru.spbau.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spbau.grpc.chat.Message

class Reader(private val name: String) {
    fun getInputFlow(): Flow<Message> = flow {
        while (true) {
            val message = readLine()
            emit(
                Message.newBuilder()
                    .setText(message)
                    .setSenderName(name)
                    .setUtcTime(System.currentTimeMillis())
                    .build()
            )
        }
    }
}
package ru.spbau.chat

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import ru.spbau.grpc.chat.ChatServerGrpcKt
import ru.spbau.grpc.chat.Message
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ChatClient constructor(
    private val channel: ManagedChannel
) : Closeable {
    private val stub = ChatServerGrpcKt.ChatServerCoroutineStub(channel)

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    fun connect(messageFlow: Flow<Message>) {
        stub.routeChat(messageFlow)
    }
}

fun runClient(host: String, port: Int, name: String) {
    val client = ChatClient(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build())

    val messageGetter = MessageGetter()
    val reader = Reader(name, messageGetter)
    client.connect(reader.getInputFlow())
}





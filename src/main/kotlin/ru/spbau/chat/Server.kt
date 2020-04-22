package ru.spbau.chat

import com.google.protobuf.util.Durations
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ChatServer private constructor(
    val port: Int,
    val server: Server
) {
    constructor(
        serverBuilder: ServerBuilder<*>,
        port: Int,
        messages: Collection<Message>
    ) : this(
        port = port,
        server = serverBuilder.addService(RouteGuideService(messages)).build()
    )

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@ChatServer.stop()
                println("*** server shut down")
            }
        )
    }

    fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    class RouteGuideService(
        val messages: Collection<Message>
    ) : ChatGrpcKt.RouteGuideCoroutineImplBase() {
        private val messages = ConcurrentHashMap<Message>()
        override suspend fun recordMessage(requests: Flow<Message>): Any {
        }

        override fun messageChat(requests: Flow<Message>): Flow<Message> = flow {

        }

    }
}

fun main(args: Array<String>) {
    val port = 8980
    val server = ChatServer(port)
    server.start()
    server.blockUntilShutdown()
}
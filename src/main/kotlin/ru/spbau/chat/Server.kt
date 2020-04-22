package ru.spbau.chat

import com.google.common.base.Stopwatch
import com.google.common.base.Ticker
import com.google.common.io.ByteSource
import com.google.protobuf.util.Durations
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ChatServer private constructor(
    val port: Int,
    val server: Server
) {
    constructor(port: Int) : this(port, defaultMessageSource())

    constructor(
        serverBuilder: ServerBuilder<*>,
        port: Int,
        features: Collection<Message>
    ) : this(
        port = port,
        server = serverBuilder.addService(RouteGuideService(features)).build()
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
            requests.collect { request ->
            }
            return RouteSummary.newBuilder().apply {
            }.build()
        }

        override fun routeChat(requests: Flow<Message>): Flow<Message> = flow {
            requests.collect { note ->
                val notes: MutableList<RouteNote> = routeNotes.computeIfAbsent(note.location) {
                    Collections.synchronizedList(mutableListOf<RouteNote>())
                }
                for (prevNote in notes.toTypedArray()) { // thread-safe snapshot
                    emit(prevNote)
                }
                notes += note
            }
        }

    }
}

fun main(args: Array<String>) {
    val port = 8980
    val server = ChatServer(port)
    server.start()
    server.blockUntilShutdown()
}
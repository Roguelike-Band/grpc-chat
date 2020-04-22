package ru.spbau.chat

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spbau.grpc.chat.ChatServerGrpc
import ru.spbau.grpc.chat.Message
import java.util.concurrent.ConcurrentHashMap

class ChatServer constructor(
    val port: Int,
    val login: String,
    private val serverHelper: ServerHelper
) {
    val server: Server
    init {
        val serverBuilder = ServerBuilder.forPort(port)
        server = serverBuilder.addService(RouteGuideService(mutableListOf())).build()
    }

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
    ) : ChatServerGrpc.ChatServerImplBase() {
        override fun routeChat(responseObserver: StreamObserver<Message>): StreamObserver<Message> {
            return object: StreamObserver<Message> {


                override fun onNext(value: Message) {
                    responseObserver.onNext()
                }


                override fun onError(t: Throwable?) {
                    TODO("not implemented")
                }

               
                override fun onCompleted() {
                    TODO("not implemented")
                }

            }
        }
    }
}

class ServerHelper {
}

fun main(args: Array<String>) {
    val port = args[0]
    val login = args[1]
    val helper = ServerHelper()
    val server = ChatServer(port.toInt(), login, helper)
    server.start()
    server.blockUntilShutdown()
}
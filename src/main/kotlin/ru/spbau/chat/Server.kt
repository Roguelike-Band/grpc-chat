package ru.spbau.chat

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.spbau.grpc.chat.ChatServerGrpc
import ru.spbau.grpc.chat.Message
import java.security.InvalidParameterException

class ChatServer constructor(
    val port: Int,
    val login: String,
    val serverHelper: ServerHelper,
    val messageGetter: MessageGetter
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

    inner class RouteGuideService(
        val messages: Collection<Message>
    ) : ChatServerGrpc.ChatServerImplBase() {
        override fun routeChat(responseObserver: StreamObserver<Message>): StreamObserver<Message> {
            return object: StreamObserver<Message> {

                init {
                    serverHelper.setResponseObserver(responseObserver)
                }

                override fun onNext(value: Message) {
                    messageGetter.addMessage(value)
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

class ServerHelper(private val name: String, private val messageGetter: MessageGetter) {

    fun setResponseObserver(responseObserver: StreamObserver<Message>) {
        GlobalScope.launch {
            val reader = Reader(name, messageGetter)
            reader.getInputFlow().collect {
                responseObserver.onNext(it)
            }
        }
    }
}

fun main(args: Array<String>) {
    when (args.size) {
        2 -> {
            val port = args[0]
            val login = args[1]
            val messageGetter = MessageGetter()
            val helper = ServerHelper(login, messageGetter)
            val server = ChatServer(port.toInt(), login, helper, messageGetter)
            server.start()
            server.blockUntilShutdown()
        }
        3 -> {
            val port = args[0]
            val login = args[1]
            val host = args[2]
            runClient(host, port = port.toInt(), name = login)
        }
        else -> {
            throw InvalidParameterException()
        }
    }
}
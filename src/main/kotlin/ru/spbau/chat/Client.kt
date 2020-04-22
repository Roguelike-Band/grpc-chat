package ru.spbau.chat

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import ru.spbau.grpc.chat.GreeterGrpcKt
import java.awt.Rectangle
import java.io.Closeable
import java.util.concurrent.TimeUnit

class ChatClient private constructor(
    private val channel: ManagedChannel
) : Closeable {
    private val stub = GreeterGrpcKt.GreeterCoroutineStub(channel)

    constructor(
        channelBuilder: ManagedChannelBuilder<*>,
        dispatcher: CoroutineDispatcher
    ) : this(channelBuilder.executor(dispatcher.asExecutor()).build())

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    fun getFeature(latitude: Int, longitude: Int) = runBlocking {
        println("*** GetFeature: lat=$latitude lon=$longitude")

        val request = point(latitude, longitude)
        val feature = stub.getFeature(request)

        if (feature.exists()) {
            println("Found feature called \"${feature.name}\" at ${feature.location.toStr()}")
        } else {
            println("Found no feature at ${request.toStr()}")
        }
    }
}

fun main(args: Array<String>) {
    val features = defaultFeatureSource().parseJsonFeatures()
    Executors.newFixedThreadPool(10).asCoroutineDispatcher().use { dispatcher ->
        val channel = ManagedChannelBuilder.forAddress("localhost", 8980).usePlaintext()
        RouteGuideClient(channel, dispatcher).use {
            it.getFeature(409146138, -746188906)
            it.getFeature(0, 0)
            it.listFeatures(400000000, -750000000, 420000000, -730000000)
            it.recordRoute(it.generateRoutePoints(features, 10))
            it.routeChat()
        }
    }
}

private fun point(lat: Int, lon: Int): Point =
    Point.newBuilder()
        .setLatitude(lat)
        .setLongitude(lon)
        .build()
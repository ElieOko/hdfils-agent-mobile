package com.partners.hdfils_agent.data.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val channel = Channel<Int>()

    launch {
        // Envoi de cinq nombres
        for (x in 1..5) {
            println("Envoi de $x")
            channel.send(x)
        }
        channel.close() // On ferme le channel après l'envoi
    }

    launch {
        // Réception des éléments jusqu'à la fermeture du channel
        for (y in channel) {
            println("Réception de $y")
        }
    }
}
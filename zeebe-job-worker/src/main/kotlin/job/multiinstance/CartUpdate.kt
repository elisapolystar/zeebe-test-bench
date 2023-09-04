/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.1. You may not use this file
 * except in compliance with the Zeebe Community License 1.1.
 */
package job.multiinstance
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.ZeebeClientBuilder
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.client.api.worker.JobHandler
import java.time.Duration
import java.util.Scanner

object CartUpdate {
    @JvmStatic
    fun main(args: Array<String>) {
        val defaultAddress = "localhost:26500"
        val clientBuilder: ZeebeClientBuilder = ZeebeClient
            .newClientBuilder()
            .gatewayAddress(defaultAddress)
            .usePlaintext()

        val jobType = "update-cart"
        clientBuilder.build().use { client ->
            println("Opening job worker for updating cart.")
            client
                .newWorker()
                .jobType(jobType)
                .handler(CartUpdateHandler())
                .timeout(Duration.ofSeconds(10))
                .open().use {
                    println("Updating cart job worker is opened. Please create a process instance.")
                    waitUntilSystemInput("exit")
                }
        }
    }

    private fun waitUntilSystemInput(exitCode: String) {
        Scanner(System.`in`).use { scanner ->
            while (scanner.hasNextLine()) {
                val nextLine = scanner.nextLine()
                if (nextLine.contains(exitCode)) {
                    return
                }
            }
        }
    }
    private fun delay() { Thread.sleep(1000) }
    private class CartUpdateHandler : JobHandler {
        override fun handle(client: JobClient, job: ActivatedJob) {
            var remainInCart = emptyArray<Item>()
            var orderValue = 0
            val items = job.variablesAsMap["items"] as ArrayList<*>
            println("There are ${items.size} items in cart: $items")
            delay()
            for (item in items) {
                val itemObject = Gson().fromJson(item.toString(), Item::class.java)
                if (itemObject.inCart <= itemObject.inStock) {
                    println("$itemObject is in stock.")
                    delay()
                    remainInCart = remainInCart.plus(itemObject)
                    orderValue += itemObject.value * itemObject.inCart
                } else {
                    println("Not enough $itemObject in stock. Removing item from cart")
                    delay()
                }
            }
            val updatedVariables = mapOf(
                "items" to remainInCart,
                "orderValue" to orderValue,
                "orderId" to job.variablesAsMap["orderId"],
            )
            println("Remaining items in cart are ${remainInCart.joinToString(", ")} with total order value $orderValue.")
            // here: business logic that is executed with every job
            // removeItemFromCart(arrayOf(job.variablesAsMap["items"]))
            client.newCompleteCommand(job.key).variables(updatedVariables).send().join()
        }
    }

    private data class Item(
        @SerializedName("value") val value: Int,
        @SerializedName("inCart") val inCart: Int,
        @SerializedName("inStock") val inStock: Int,
    )
}

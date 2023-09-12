package workflowmanager.job

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import org.springframework.stereotype.Component

@Component
class MultiInstance {
    private fun delay() { Thread.sleep(1000) }

    @JobWorker(type = "parse-order-items")
    fun parseOrderItems(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        val item = job.variablesAsMap["item"] as HashMap<*, *>
        println("Cart includes ${item["inCart"]} of item ID ${item["id"]}")
        delay()
    }

    @JobWorker(type = "cart-update")
    fun cartUpdate(client: JobClient, job: ActivatedJob) {
        data class Item(
            @SerializedName("id") val id: Int,
            @SerializedName("value") val value: Int,
            @SerializedName("inCart") val inCart: Int,
            @SerializedName("inStock") val inStock: Int,
        )
        var remainInCart = emptyArray<Item>()
        var orderValue = 0
        val items = job.variablesAsMap["items"] as ArrayList<*>
        for (item in items) {
            val itemObject = Gson().fromJson(item.toString(), Item::class.java)
            if (itemObject.inCart <= itemObject.inStock) {
                println("$itemObject is in stock.")
                remainInCart = remainInCart.plus(itemObject)
                orderValue += itemObject.value * itemObject.inCart
            } else {
                println("Not enough $itemObject in stock. Removing item from cart")
            }
        }
        val updatedVariables = mapOf(
            "items" to remainInCart,
            "orderValue" to orderValue,
            "orderId" to job.variablesAsMap["orderId"],
        )
        println("Remaining items in cart are ${remainInCart.joinToString(", ")} with total order value $orderValue.")

        client.newCompleteCommand(job.key).variables(updatedVariables).send().join()
        delay()
    }

    @JobWorker(type = "initiate-payment")
    fun initiatePayment(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Payment initiated.")
        delay()
    }

    @JobWorker(type = "ship-with-insurance")
    fun shipWithInsurance(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Insurance added. Order shipped.")
    }

    @JobWorker(type = "ship-without-insurance")
    fun shipWithoutInsurance(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Order shipped without insurance.")
    }
}
package job.multiinstance

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.ZeebeClientBuilder
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.client.api.worker.JobHandler
import java.time.Duration
import java.util.Scanner

/**
 * When connecting to a cluster in Camunda Cloud, this application assumes that the following
 * environment variables are set:
 *
 *  * ZEEBE_ADDRESS
 *  * ZEEBE_CLIENT_ID
 *  * ZEEBE_CLIENT_SECRET
 *  * ZEEBE_AUTHORIZATION_SERVER_URL
 *
 * When `ZEEBE_ADDRESS` is not set, it connects to a broker running on localhost with
 * default ports
 */
object ShipWithoutInsurance {
    @JvmStatic
    fun main(args: Array<String>) {
        val defaultAddress = "localhost:26500"
        val envVarAddress = System.getenv("ZEEBE_ADDRESS")
        val clientBuilder: ZeebeClientBuilder = ZeebeClient
            .newClientBuilder()
            .gatewayAddress(defaultAddress)
            .usePlaintext()

        val jobType = "ship-without-insurance"
        clientBuilder.build().use { client ->
            println("Opening job worker for shipping without insurance.")
            client
                .newWorker()
                .jobType(jobType)
                .handler(ShipWithoutInsuranceHandler())
                .timeout(Duration.ofSeconds(10))
                .open().use {
                    println("Shipping without insurance job worker is opened.")
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
    private class ShipWithoutInsuranceHandler : JobHandler {
        override fun handle(client: JobClient, job: ActivatedJob) {
            client.newCompleteCommand(job.key).send().join()
            println("Order shipped without insurance.")
            delay()
        }
    }
}
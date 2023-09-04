/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.1. You may not use this file
 * except in compliance with the Zeebe Community License 1.1.
 */
package job.moneyloan

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.ZeebeClientBuilder
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.client.api.worker.JobHandler
import java.time.Duration
import java.util.Scanner

object SendRejectionLetter {
    @JvmStatic
    fun main(args: Array<String>) {
        val defaultAddress = "localhost:26500"
        val clientBuilder: ZeebeClientBuilder = ZeebeClient
            .newClientBuilder()
            .gatewayAddress(defaultAddress)
            .usePlaintext()

        val jobType = "send-rejection-letter"
        clientBuilder.build().use { client ->
            println("Opening job worker for sending rejection letter.")
            client
                .newWorker()
                .jobType(jobType)
                .handler(SendRejectionLetterHandler())
                .timeout(Duration.ofSeconds(10))
                .open().use {
                    println("Sending rejection letter job worker is opened. Waiting for debt variables.")
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
    private class SendRejectionLetterHandler : JobHandler {
        override fun handle(client: JobClient, job: ActivatedJob) {
            client.newCompleteCommand(job.key).send().join()
            println("Sorry, your debt is too high to proceed. Loan request is rejected")
            delay()
        }
    }
}

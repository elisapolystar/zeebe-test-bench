package workflowmanager.job

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import org.springframework.stereotype.Component

@Component
class SubProcess {
    private fun delay() { Thread.sleep(1000) }

    @JobWorker(type = "collect-money")
    fun collectMoney(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Money collected.")
        delay()
    }

    @JobWorker(type = "fetch-items")
    fun fetchItems(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Item fetched.")
        delay()
    }

    @JobWorker(type = "ship-parcel")
    fun shipParcel(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Parcel shipped.")
    }
}
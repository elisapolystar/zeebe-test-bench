package workflowmanager.job

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import org.springframework.stereotype.Component

@Component
public class MoneyLoan {
    @JobWorker(type = "send-acceptance-letter")
    fun sendAcceptanceLetter(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("The loan request is accepted. Processing to transfer money.")
        Thread.sleep(1000)
    }

    @JobWorker(type = "send-rejection-letter")
    fun sendRejectionLetter(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Sorry, your debt is too high to proceed. Loan request is rejected.")
    }

    @JobWorker(type = "transfer-money")
    fun transferMoney(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Money has been transferred successfully.")
    }
}

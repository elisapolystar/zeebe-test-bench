package workflowmanager.job

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.JobWorker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class MoneyLoan {
    @Autowired
    lateinit var zeebeClient: ZeebeClient

    @JobWorker(type = "send-acceptance-letter")
    fun sendAcceptanceLetter(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("The loan request is accepted. Processing to transfer money.")
        Thread.sleep(1000)
    }

    private var iteration = 0
    @JobWorker(type = "send-rejection-letter")
    fun sendRejectionLetter(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send()
        if (iteration < 5) println("Sorry, your debt is too high to proceed. The rejection letter is sent. Waiting until the letter is received.")
        iteration += 1
        if (iteration == 5) {
            zeebeClient.newSetVariablesCommand(job.processInstanceKey).variables("{\"received\":true}").send().join()
            println("Letter has been received.")
        }
    }

    @JobWorker(type = "transfer-money")
    fun transferMoney(client: JobClient, job: ActivatedJob) {
        client.newCompleteCommand(job.key).send().join()
        println("Money has been transferred successfully.")
    }
}

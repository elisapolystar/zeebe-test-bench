package workflowmanager.instancecreator

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File

@Component
class MoneyLoanInstanceCreator {

    @Autowired
    private lateinit var client: ZeebeClient

    private val relativePath: String = "./src/main/resources/variables/money-loan"

    private fun delay() {
        Thread.sleep(10000)
        println()
    }

    fun createInstance(filename: String): ProcessInstanceEvent? {
        return client
            .newCreateInstanceCommand()
            .bpmnProcessId("money-loan")
            .latestVersion()
            .variables(File("$relativePath/$filename").readText())
            .send()
            .join()
    }

    @Scheduled(fixedDelay = 500000)
    fun createDeclinedInstance() {
        val event = createInstance("decline.json")

        println("Instance for money-loan created with key " + event?.processInstanceKey + " is created.")
        delay()
    }

    @Scheduled(fixedDelay = 500000)
    fun createAcceptedInstance() {
        val event = createInstance("accept.json")

        println("Instance for money-loan created with key " + event?.processInstanceKey + " is created.")
        delay()
    }
}

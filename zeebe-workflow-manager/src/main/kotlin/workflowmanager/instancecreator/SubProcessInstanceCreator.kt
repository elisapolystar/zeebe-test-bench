package workflowmanager.instancecreator

import io.camunda.zeebe.client.ZeebeClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SubProcessInstanceCreator {
    @Autowired
    private lateinit var client: ZeebeClient

    private fun delay() {
        Thread.sleep(10000)
        println()
    }

    @Scheduled(fixedDelay = 500000)
    fun createSubProcessInstance() {
        val event = client
            .newCreateInstanceCommand()
            .bpmnProcessId("order-subprocess")
            .latestVersion()
            .send()
            .join()
        println("Instance for order-subprocess created with key " + event.processInstanceKey + " is created.")
        delay()
    }
}

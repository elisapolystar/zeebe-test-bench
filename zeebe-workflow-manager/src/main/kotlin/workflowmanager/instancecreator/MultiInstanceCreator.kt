package workflowmanager.instancecreator

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File

@Component
class MultiInstanceCreator {
    @Autowired
    private lateinit var client: ZeebeClient

    private val relativePath: String = "./src/main/resources/variables/multi-instance-process"

    private fun delay() {
        Thread.sleep(10000)
        println()
    }

    fun createInstance(filename: String): ProcessInstanceEvent? {
        return client
            .newCreateInstanceCommand()
            .bpmnProcessId("multi-instance-process")
            .latestVersion()
            .variables(File("$relativePath/$filename").readText())
            .send()
            .join()
    }

    @Scheduled(fixedDelay = 500000)
    fun createAllInStockInstance() {
        val event = createInstance("all-in-stock.json")
        println(
            "Instance for multi-instance-process created with key " +
                event?.processInstanceKey +
                " is created.",
        )
        delay()
    }

    @Scheduled(fixedDelay = 500000)
    fun createSomeInStockInstance() {
        val event = createInstance("some-in-stock.json")

        println(
            "Instance for multi-instance-process created with key " +
                event?.processInstanceKey +
                " is created.",
        )
        delay()
    }

    @Scheduled(fixedDelay = 500000)
    fun createNoneInStockInstance() {
        val event = createInstance("none-in-stock.json")
        println(
            "Instance for multi-instance-process created with key " +
                event?.processInstanceKey +
                " is created.",
        )
        delay()
    }
}

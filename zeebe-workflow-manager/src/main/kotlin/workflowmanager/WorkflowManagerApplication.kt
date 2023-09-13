package workflowmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("workflowmanager")
// Uncomment @EnableScheduling to automatically create process instances via gradle bootrun
// @EnableScheduling
open class WorkflowManagerApplication

fun main(args: Array<String>) {
    runApplication<WorkflowManagerApplication>(args = args)
    println("All processes have been deployed. Job workers are up and running.")
}

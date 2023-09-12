package workflowmanager

import lombok.extern.slf4j.Slf4j
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan("workflowmanager")
@EnableScheduling
@Slf4j
open class Main

fun main(args: Array<String>) {
    runApplication<Main>(args = args)
    println("All processes have been deployed. Job workers are up and running.")
}

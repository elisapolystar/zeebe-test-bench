package processmanager

import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration

@Configuration
@Deployment(resources = ["classpath*:/bpmn/*.bpmn"])
open class Deployments

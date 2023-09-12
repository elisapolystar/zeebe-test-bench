package workflowmanager

import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.context.annotation.Configuration

@Configuration
@Deployment(resources = ["classpath*:/bpmn/*.bpmn"])
open class Deployments

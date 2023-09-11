# zeebe-test-bench

## Description
This repository provides a test bench for building a monitoring workflow application. It includes:
- A development environment deployed to Docker with different services (Zeebe, Kafka, Postgres...)
- Process files (in `.bpmn` format, located under `./bpmn`) to be deployed to Zeebe.
- Scripts to deploy these processes to Zeebe using `zbctl`.
- Job workers that can be called from the BPMN files.

## Installation
The following application should be installed: 
#### Required
- [WSL2 (for Windows)](https://learn.microsoft.com/en-us/windows/wsl/install). Use WSL instead of Command Prompt / PowerShell from now on.
- Ubuntu (for Windows). Download this in the Microsoft Store after installing WSL2. Restarting your machine after installation.
- [SDKMAN](https://sdkman.io/install)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) 
- [Docker Desktop](https://docs.docker.com/engine/install/#desktop)
- Java JDK v17: `sdk install java 17`
- Gradle v8.1.1: `sdk install gradle 8.1.1`
- Kotlin: `sdk install kotlin`
- [Camunda Modeler](https://camunda.com/download/modeler/)

#### Recommended
- `zbctl`
- `jq`: `sudo apt-get install jq`
- [IntelliJ IDEA Community Edition](https://www.jetbrains.com/help/idea/installation-guide.html)

## Setup
- Start Docker Desktop
- Start the development environment: `./start.sh`
    - Check the logs of the container in Docker Desktop. If the error `java.lang.ClassNotFoundException: io.zeebe.exporters.kafka.KafkaExporter` exists (and potentially you are using an ARM Mac), do the following steps:
        - Create an `exporters` folder at the root of this repository.
        - Download `zeebe-kafka-exporter.jar` file through this [link](https://repo1.maven.org/maven2/io/zeebe/zeebe-kafka-exporter/3.1.1/zeebe-kafka-exporter-3.1.1-jar-with-dependencies.jar -O /exporters/zeebe-kafka-exporter.jar) and move this jar inside the `exporters` folder created above
        - Open `docker-compose-arm.yml` (or `docker-compose.yml` if  you are using Intel Mac) and comment out the `get-kafka-exporter` service.
        - Run `./start.sh` again.
- Open Zeebe Simple Monitor at `localhost:8083`. No process should be visible yet, but the UI should be.
    - If not, restart `zeebe-simple-monitor` container in Docker Desktop until its log displays something like "Started ZeebeSimpleMonitorApp in x seconds".

## Interaction with Zeebe broker using zbctl
- `zbctl` is used to interact with Zeebe. If you cannot install them locally, `zbctl` binary in `bin` directory can be used as an alternative. Each binary should be used in its corresponding operating system.

| Operating System |   zbctl binary   |
|:----------------:|:----------------:|
|       Linux      | bin/zbctl        |
|       OS X       | bin/zbctl.darwin |
|      Windows     | bin/zbctl.exe    |


- Deploy a process: `zbctl --insecure deploy ./bpmn/<process-name.bpmn>`
- Create an instance for a process: `zbctl --insecure create instance <process-id> --variables <necessary variables>`. The process ID can be checked by either on the command line after deploying the process, or when opening the process' file in Camunda Modeler. Some variables are pre-defined under `./variables`.
- Create a job worker: `zbctl --insecure create worker <task-definition-type> --handler <command>`. Task definition's type is set in Modeler.
- Publish a message: `zbctl --insecure publish message <task-definition-type> --correlationKey=<key>`. The correlation key is set in Modeler as a variable (e.g., "orderId"), and "orderId" must be included when creating the process instance.

## Running job workers without zbctl
`zeebe-job-worker` is a Gradle project which includes pre-defined job workers and process definitions. To start:
- Build the project: `gradle build`.
- Deploy all processes and start all job workers: `gradle bootrun`

Each job worker can print one or more output lines to the command line upon whenever it's job worker is needed.

## Full example on setting up the environment and monitor a process instance
1. Start the environment: `./start.sh`. Open Zeebe Simple Monitor at `localhost:8083` and monitor the status after each step below.
2. Deploy all processes and start all job workers: `cd zeebe-job-worker && gradle build && gradle bootrun`
6. Publish a message: `zbctl --insecure publish message "payment-received" --correlationKey="2"`. (Why 2? Check `some-in-stock.json`)`.


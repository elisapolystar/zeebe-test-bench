# zeebe-test-bench

## Description
This repository provides a test bench for building a monitoring workflow application. It includes:
- An development environment deployed to Docker with different services (Zeebe, Kafka, Postgres...)
- Process files (in `.bpmn` format, located under `./bpmn`) to be deployed to Zeebe.
- Scripts to deploy these processes to Zeebe using `zbctl`.
- Job workers that can be called from the BPMN files.

## Installation
The following application should be installed: 
#### Required
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) 
- [Docker Desktop](https://docs.docker.com/engine/install/#desktop)
- [Java JDK v17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Kotlin](https://kotlinlang.org/docs/command-line.html#install-the-compiler)
- [Camunda Modeler](https://camunda.com/download/modeler/)
- [IntelliJ IDEA](https://www.jetbrains.com/help/idea/installation-guide.html)

#### Recommended
- `zbctl`
- `jq`

## Setup
- Start Docker Desktop
- Start the development environment: `./start.sh`
- Open Zeebe Simple Monitor at `localhost:8083`. No process should be visible yet, but the UI should be 

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
`zeebe-job-worker` is a Gradle project which includes pre-defined job workers. To run:
- Open `zeebe-job-worker` in IntelliJ IDEA.
- Open a job worker file (e.g., `CartUpdate.kt`) and click the `Run` button. Note that the run configuration (next to the run button) should be `Current File`.

Each job worker can print one or more output lines to the command line upon whenever it's job worker is needed.

## Full example on setting up the environment and monitor a process instance
1. Start the environment: `./start.sh`. Open Zeebe Simple Monitor at `localhost:8083` and monitor the status after each step below.
2. Deploy a process: `zbctl --insecure deploy ./bpmn/multi-instance-process.bpmn`
3. Create an instance: ```zbctl --insecure create instance multi-instance-process --variables "`jq '.' variables/multi-instance-process/some-in-stock.json`"```. (If jq is not installed, replace the command between " " by the content in `some-in-stock.json`).
4. Run the predefined job-worker: Open `CartUpdate.kt` of `zeebe-job-worker` in IntelliJ and run the file. The logs should start showing.
5. Run a job-worker in command line: `zbctl --insecure create worker initiate-payment --handler cat`.
6. Publish a message: `zbctl --insecure publish message "payment-received" --correlationKey="2"`. (Why 2? Check `some-in-stock.json`)`.


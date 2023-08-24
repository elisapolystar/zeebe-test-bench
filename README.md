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
- [Gradle v7.6.1](https://gradle.org/next-steps/?version=7.6.1&format=all) (Clicking the link will automatically download the zip file. Please unzip and follow the instructions.)
- [Camunda Modeler](https://camunda.com/download/modeler/)

#### Recommended
- `zbctl`
- `jq`
- [IntelliJ IDEA](https://www.jetbrains.com/help/idea/installation-guide.html)

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
- Option 1: In terminal, change the directory to `zeebe-job-worker` and run `gradle bootRun`
- Option 2: Open `zeebe-job-worker` in IntelliJ IDEA and choose the Run button.

Each job worker can print one or more output lines to the command line upon whenever it's job worker is needed.

## Full example on setting up the environment and monitor a process instance
1. Start the environment: `./start.sh`. Open Zeebe Simple Monitor at `localhost:8083` and monitor the status after each step below.
2. Deploy a process: `zbctl --insecure deploy ./bpmn/multi-instance-process.bpmn`
3. Create an instance: ```zbctl --insecure create instance multi-instance-process --variables "`jq '.' variables/multi-instance-process/some-in-stock.json`"```. (If jq is not installed, replace the command between " " by the content in `some-in-stock.json`).
4. Run the predefined job-worker: `cd zeebe-job-worker && gradle bootRun`.
5. Run a job-worker in command line: `zbctl --insecure create worker initiate-payment --handler cat`
6. Publish a message: `zbctl --insecure publish message "payment-received" --correlationKey="2"`. (Why 2? Check `some-in-stock.json`)`.


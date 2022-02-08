# Task Scheduler - Team 2 Electric Boogaloo
![SE306 Team 2 Logo](docs/logo.png)

This project aims to solve an NP-hard scheduling problem, assigning tasks from a Directed Acyclic Graph (DAG) to a given number of processors. The algorithm is computed on a given number of threads running in parallel. The current release uses a sequential Depth-First Branch and Bound Search to generate an optimal schedule; a schedule that uses a minimal amount of time to complete all tasks. This project also has a visualisation component, live updating with the search and provides meaningful information about the progress of the algorithm as it is running. 

### ![Read The Wiki!](https://github.com/SoftEng306-2021/project-1-project-1-team-2/wiki)

## Screenshots
![Visualisation Example](https://github.com/butterplup/Task-Scheduler/blob/main/docs/Wiki/Visualisation/visualisation.PNG)

## Running the Project
If you want to run the latest release, click on the [Releases](https://github.com/butterplup/Task-Scheduler/releases) link and download the latest scheduler.jar. Follow the instructions under ****Usage**** to run. 
## Project Setup
To set up this project and view the source code for editing/building yourself:
1. Clone the repo into a directory, by running the following command in your shell:
```shell
$ git clone https://github.com/butterplup/Task-Scheduler.git 
```
2. Import this project into IntelliJ as a Maven project.
## Building
### Dependencies
- Maven
- Java Development Kit >= 8
### Instructions
From the directory, run the following command in your shell:
```shell
# Package into a self-contained jar in the target/directory
mvn package
```
This will create the jar which you can execute by following the instructions under ****Usage****. 
## Usage
### Dependencies
- Java 8

You can specify various arguments while running the scheduler. See below for your options:
```shell
java -jar scheduler.jar INPUT.dot P [OPTION]
# Mandatory Arguments:
# INPUT.dot A task graph with integer weights in dot file format
# P         Number of processors to schedule the INPUT graph on (minimum 1)
#
# Optional Arguments:
# -p N      Use N cores (threads) for parallel execution
# -v        Enable search visualisation
# -o OUTPUT Output filename (Default is INPUT−output.dot)
```
Examples:
* ```java -jar scheduler.jar INPUT.dot 2 -o MYOUTPUT.dot```: Run scheduler.jar to schedule INPUT.dot to 2 processors, and output to MYOUTPUT.dot
* ```java -jar schedular.jar INPUT.dot 4 -p 2 -v```: Run scheduler.jar on 2 threads in parallel to schedule INPUT.dot to 4 processors, with search visualisation enabled

## Contributors
* [Johnathan](https://github.com/johnathan-coe)
* [Stephy](https://github.com/colaMeowSY)
* [Joel](https://github.com/butterplup)
* [Henry](https://github.com/randomguy7373)
* [Kieran](https://github.com/kieran-byte)
* [Tyler](https://github.com/dogeliness)

# Project 1 - Team 2 Electric Boogaloo
![SE306 Team 2 Logo](docs/logo.png)
This project aims to solve an NP-hard scheduling problem, assigning tasks from a Directed Acyclic Graph (DAG) to a given number of processors. The algorithm is computed on a given number of threads running in parallel. The current release uses a sequential Depth-First Branch and Bound Search to generate an optimal schedule; a schedule that uses a minimal amount of time to complete all tasks. This project also has a visualisation component, live updating with the search and provides meaningful information about the progress of the algorithm as it is running. 
## Project Setup
1. Clone repo into a directory:
```shell
$ git clone https://github.com/SoftEng306-2021/project-1-project-1-team-2.git 
```
2. Import this project into Intellij as a Maven project.
## Building
### Dependencies
- Maven
- Java Development Kit >= 8
### Instructions
From the directory:
```shell
# Package into a self-contained jar in the target/directory
mvn package
```
## Usage
### Dependencies
- Java 8
```shell
java -jar scheduler.jar INPUT.dot P [OPTION]
# Mandatory Arguments:
# INPUT.dot A task graph with integer weights in dot file format
# P         Number of processors to schedule the INPUT graph on (minimum 1)
#
# Optional Arguments:
# -p N      Use N cores for parallel execution
# -v        Enable search visualisation
# -o OUTPUT Output filename (Default is INPUT−output.dot)
```

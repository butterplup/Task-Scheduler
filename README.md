# SE306 Team 2 Project 1
This project aims to solve an NP-hard scheduling problem, assigning tasks from a Directed Acyclic Graph(DAG) to a given number of processors. The current release uses a sequential Depth-First-Branch and Bound Search to find an optimal schedule that gives a minimal amount of time to complete all tasks.
## Building
### Dependencies
- Maven
- Java >8
### Project Setup
1. Clone repo with https://github.com/SoftEng306-2021/project-1-project-1-team-2.git
```shell
$git clone https://github.com/SoftEng306-2021/project-1-project-1-team-2.git 
```
2. Import this project into Intellij as a Maven project.
### Instructions
```shell
# Package into a self-contained jar in the target/ directory
mvn package
```
## Usage
### Dependencies
- Java 8
```shell
java -jar scheduler.jar INPUT.dot P [OPTION]
# Mandatory Arguments:
# INPUT.dot A task graph with integer weights in dot format
# P         Number of processors to schedule the INPUT graph on
#
# Optional Arguments:
# -p N      Use N cores for parallel execution
# -v        Enable search visualisation
# -o OUTPUT Output filename (Default is INPUT−output.dot)
```

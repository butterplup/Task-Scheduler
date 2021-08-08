# SE306 Team 2 Project 1
## Building
### Dependencies
- Maven
- Java >8
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

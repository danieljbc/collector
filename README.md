# COLLECTOR Framework - INF2102

Inspired by how data mining processes in software repositories are structured, the COLLECTOR framework aims to facilitate data mining processes composed of three main steps: input (or data collection), processing and communication (or exporting), in this respective order. This help is through abstractions that facilitate paralellism and data management that respects the different steps of the process, making the process more traceable, and therefore replicable.

The framework also allows for continuous data collection, which is useful in cases where the raw data source is constantly updated, as it frequently happens when working with software repositories. Its extensible arquitecture allow for the creation of very diverse applications (e.g. bots that monitor changes in software quality metrics and/or how code smells are afected by a change in GitHub pull requests, applications for the continuous generation of datasets, etc). Nevertheless, a sample program that instantiates the framework will also be developed as part of this project.

## Building

The implementation uses gradle as a dependency manager. In case you don't have a local installation of gradle, all gradle commands in the following lines can be replaced by the usage of `gradlew <command>`. The gradlew executable is present in this repository.

To download the dependencies and build the .class files, run the `gradle build` command on the root of the repository (same folder as the build.gradle file).

After the execution of the previous command, the command `gradle distZip` can generate a binary distribution compressed in a .zip file. The execution of this distribution is covered in the Execution subsection.

## Testing

After the project is built using `gradle build` (covered in the previous section), you can use the `gradle test` command to execute the automated tests.

## Executing

After the project is built using `gradle build` (covered in the first section), you can use the `gradle run` command to execute the automated tests.

## IntelliJ IDEA

This project supports the usage of IntelliJ IDEA to automate the setup and execution of the aforementioned gradle steps. 

To use this, just import the project folder with the IDE.

# Documentation

* [Requirements Document](./docs/REQUIREMENTS.md)
* [Test Guide and Logs](./docs/TESTS.md)

# Models

## Class Diagram - Framework

![Class Diagram - Framework](./docs/ClassDiagramFramework.png)

## Class Diagram - Instantiation

![Class Diagram - Framework](./docs/ClassDiagramInstantiation.png)
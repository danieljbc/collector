# Requirements Document

## Framework

### Functional
| ID | Requirement
| --- | ------------
| FR01 | The system should support the implementation of synchronous and asynchronous tasks, representing three separate stages of a data collection process: collection, processing and communication.
| FR02 | The system should support the persistence of the data collected by the framework by abstracting the database access.
| FR03 | The instantiation of the framework should be able to start and stop the execution of tasks from the framework at any point.
| FR04 | At any point, it should be possible to export a snapshot the data contained on the database for a specific step, allowing for traceability in each step, and consequently also facilitating reproducibility.

### Non Functional

| ID | Requirement
| --- | ------------
| NFR01 | The framework should use the mongoDB database.
| NFR02 | The system should be able to use multiple threads in order to improve performance in asynchronous tasks.

## Instantiation

### Functional

| ID | Requirement
| --- | ------------
| FR01 | The system should collect commit messages and author information for all commits in a remote repository.
| FR02 | Using messages and author information from commits (FR01), for each of those commits, the system should collect data such as message length and the number of previous commits by that same author.
| FR03 | The system should be able to export the information collected in the processing step (FR02) to the .csv format.

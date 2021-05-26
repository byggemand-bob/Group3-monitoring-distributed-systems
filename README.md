# Group3-monitoring-distributed-systems
This repository contains the code for the C-Mon framework. It consists of 4 projects:
- a **Client** project which can be used to create nodes with request sending capabilities
- a **Server** project which can be used to create nodes with request receiving capabilities and basic request sending to Monitor Server
- a **Monitor-Client** project which is used to implement the functionality for the **Server** and **Client** projects by creating mustache templates
- a **Monitor** project which contains the implementation of the Monitor Server, this handles the analyzis and notification in C-Mon.

## Requirements
The following are required in order to use the framework:
- Java version 11 or higher
- Maven or IDE with integrated Maven instance

## Setup
To start using the framework, copies of the node type that you want to implement can be made. Before the API generation can be started it is necessary to provide the OpenAPI specification (only for **Server** and **Client**), a default specification can be found in "*<project_root>/src/main/resources/api_specification.yaml*", the content can either be overridden or a new file can be created. Note that if a new file is created this needs to be specified in the respective POM.xml file which can be found in each projects root folder, for the org.openapitools plugin the content of the \<inputSpec> tag needs to changed to reflect this.

When individual projects have been made for the nodes for the system and the API specification provided, the code generation can be triggered by running Maven on the projects with the goals "clean compile" or "clean install", this will trigger the OpenAPI codegeneration and make sure that all necessary libraries are downloaded and installed.

Changing the port on which the spring projects start can be configured using the **application.properties** file, which can be found at "*<project_root>/src/main/resources/*", by inserting a key-value pair on the form *key=value* with the following: "server.port=\<port>".

## Configuration
It is possible for the users of C-Mon to configure some of the functionality of the framework.
The tables below describes what configurations are available. These are configured in the **configuration.properties** file which are located for each project at: "<project_root>/src/main/resources/configuration.properties". These are configured by putting the configuration on a single line for each configuration in the form *key=value*, where the *key* is the configuration from the tables.

### Client and Server configuration properties

| Configuration (key) | Required | Description |
|:--------------------|:--------:|-------------|
| ID | Yes | The ID of the node, this should be unique for each node in the system, this is to identify the node for the Monitor Server. |
| MonitorServerAddress | Yes | The IP and PORT of the Monitor Server formatted as IP:PORT, prepending with http:// is not required. IP is the IP of the Monitor Server and PORT is the port the Monitor Server has been started on. |
| DaysToKeepFailedMessages | No | Used for clean-up of old messages, it determines the number of days that failed unsent messages stays in the database. Default is 30 days, it cannot go below 0 days. |
| SQLPath | No | The path where the SQLite database should reside, the path is relative from the root of the module (Example: "./Client"). Default is "./src/main/resources/sqlite/db/"|
| DataBaseFileName | No | The name of the SQLite instance. Default: "queue.db" |
| MonitorDataSendingCPUThreshold | No | The lower CPU usage threshold before messages can be sent to the Monitor Server from the lazy queue. Default: 0.2 (20%). |

Table 1: Shows the configuration properties available for the **Server** and **Client** instances of the framework.

***

### Monitor Server configuration properties

| Configuration (key) | Required | Description |
|:--------------------|:--------:|-------------|
| DaysToKeepFailedMessages | No | Used for clean-up of old messages, it determines the number of days that failed unsent messages stays in the database. Default is 30 days, it cannot go below 0 days. |
| SQLPath | No | The path where the SQLite database should reside, the path is relative from the root of the module (Example: "./Client"). Default is "./src/main/resources/sqlite/db/"|
| DataBaseFileName | No | The name of the SQLite instance. Default: "queue.db" |
| ConstraintsSpecificationFilePath | Yes | The path to the constraint specification, which specify the constraints for the system. This specification follows the JSON schema found [here](./Monitor/src/main/resources/validation/constraintSchema.json).

Table 2: Shows the configuration properties available for the **Monitor Server** instance of the framework.

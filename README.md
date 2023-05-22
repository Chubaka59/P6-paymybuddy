# PayMyBuddy
An application to transfer money to contacts.
This app uses Java to run.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Java 17
- Maven 3.6.2

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://www.oracle.com/java/technologies/downloads/#jdk17-windows

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySql

https://www.mysql.com/downloads/

4.Set environment variables

To run the program, it must be start with the following environment variables

SPRING_DATASOURCE_URL (mysql database)
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

### Testing

The app has unit tests and integration tests written.

To run the tests from maven, go to the folder that contains the pom.xml file and execute the below command.

`mvn site`

### Class diagram

![Lefebvre_Joffrey_uml_04_2023](https://github.com/Chubaka59/P6-paymybuddy/assets/119501964/89ae089c-c383-422d-8c29-1ccbbcae3f16)

### Database modeling scheme

![Lefebvre_Joffrey_mpd_04_2023](https://github.com/Chubaka59/P6-paymybuddy/assets/119501964/1b5ca28b-b2c4-4ab8-ab9a-705a82d3cabe)

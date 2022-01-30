[![CI](https://github.com/Bartosz-D3V/millionpugs-challenge/actions/workflows/build.yml/badge.svg)](https://github.com/Bartosz-D3V/millionpugs-challenge/actions/workflows/build.yml)

# Millionpugs Challenge

## Introduction
The following project has been created to fulfil requirements for the interview programming task at [MillionPugs](https://www.millionpugs.com/).

## Requirements
Create REST endpoint for reading users bank account (defined in PLN) in USD. Use [NBP API](http://api.nbp.pl/) for currency conversion.

## Running application

### Spring boot
To run application as a Spring Boot application use gradle:
```shell
./gradlew bootRun
```

### Docker container
To build a jar file and run it inside docker container use docker:
```shell
docker build -t millionpugs-challenge .
docker run -dp 8080:8080 millionpugs-challenge:latest
```

_Please note that running application inside container takes time during the first build as it has to download Gradle and JDK 11._

### Testing

#### Database
By default, application run on localhost and port 8080.

Application database is auto-populated during startup.

To access H2 console open your browser at http://localhost:8080/h2-console

This is in-memory database used just for development purposes. All data will be lost when application will stop.

| Property     | Value                   |
|--------------|-------------------------|
| Driver Class | org.h2.Driver           |
| JDBC Url     | jdbc:h2:mem:millionpugs |
| Username     | user                    |
| Password     | password                |

#### Postman
To quickly test endpoint use Postman collection provided in [postman](https://github.com/Bartosz-D3V/millionpugs-challenge/tree/master/postman)

#### OpenAPI
API has been documented using OpenAPI format and is available in [open-api](https://github.com/Bartosz-D3V/millionpugs-challenge/tree/master/open-api)

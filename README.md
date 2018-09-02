# RoboTrade-API [![CircleCI](https://circleci.com/gh/petivagyok16/robotrade-api/tree/master.svg?style=svg)](https://circleci.com/gh/petivagyok16/robotrade-api/tree/master)

### Description

This API is the link between RoboTrade stock trader service and its clients.

### How to install and run the project
- import root folder as a project to IntelliJ
- install maven dependencies
- install Lombok plugin inside IntelliJ (preferences/plugins/browse plugins)
- Run a MongoDB container using Docker: `docker run -p 27071:27017 -d mongo`
- Use your favorite mongo client to visit mongoDB e.g. [Robo 3T](https://robomongo.org/)
- If you are experiencing issues during the startup consider to click `File/Invalidate Caches/restart`
 in IntelliJ and/or inside `Preferences/Settings` -> `Build/Execution, Deployment` -> `Compiler` -> `Annotation Processors`
 click `Enable annotation processing`

 ### How to use
 - Open [Postman](https://www.getpostman.com/), call `auth/signin` endpoint with valid credentials e.g. `username: test, password: test` and save the token to the environment variables in postman as `token`which is generated by the server
 - Check the API documentation below for better overview
 - [API Documentation](https://documenter.getpostman.com/view/659719/RWTptGtb)

### Requirements
- JDK 1.8
- IntelliJ or Eclipse
- Project language level 8 - Lambdas, type annotations etc.
- Target bytecode version: 1.8

### Architecture

![RoboTrade architecture](https://drive.google.com/file/d/1IqKuznRrZS8_lM-nz8CTqABtPULnmRTq/view?usp=sharing)

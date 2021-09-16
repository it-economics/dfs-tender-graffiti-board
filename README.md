# dfs-tender-graffiti-board

[![CircleCI](https://circleci.com/gh/it-economics/dfs-tender-graffiti-board/tree/master.svg?style=svg)](https://circleci.com/gh/it-economics/dfs-tender-graffiti-board/tree/master)

This repository contains a simple application which let you add messages to a graffiti board so that every one can see
them.

The application is provided in two parts. First the backend part which provides an API and socket endpoint through these
messages can be persisted and fetched. The second part consist of a frontend application which provides a simple ui and
uses the backend to create and retrieve messages.

## How to run it

### Backend

Navigate to `service` folder first. You can then run the application using Gradle wrapper

````shell
# On Mac or Linux
./gradlew bootRun

# On Windows
./gradlew.bat bootRun
````

This will run the application at [http://localhost:8085](http://localhost:8085).

### Frontend

Navigate to `ui` folder in order to use the following commands.

### Install frontend dependencies

When you want to start it for the first time you need to install frontend dependencies first.

````shell
yarn
````

Afterwards you can run the app.

### Run app

You can use the following command to run a local development server

````shell
yarn start
````

This will run the app in the development mode. Open [http://localhost:3000](http://localhost:3000) to view it in the
browser.

### Further reading

A more detailed documentation about available scripts can be found [here](./ui/README.md) 
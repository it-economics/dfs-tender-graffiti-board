# dfs-tender-graffiti-board

[![CircleCI](https://circleci.com/gh/it-economics/dfs-tender-graffiti-board/tree/master.svg?style=svg)](https://circleci.com/gh/it-economics/dfs-tender-graffiti-board/tree/master)

This repository contains a simple application which let you add messages to a graffiti board so that every one can see
them.

The application is provided in two parts. First the backend part which provides an API and socket endpoint through these
messages can be persisted and fetched. The second part consist of a frontend application which provides a simple ui and
uses the backend to create and retrieve messages.

## API endpoints

You can use the `/messages` endpoint to fetch all messages or to post a new message

### Authorization

The `messages` endpoint is secured using `Basic Auth` with username `John` and password `Doe`.

### Get all messages

````shell
curl \
  -H "Authorization: Basic Sm9objpEb2U=" \
  -X GET http://localhost:8085/messages
````

### Post message

````shell
curl \
  -d '{"author":"John Doe", "message":"Hello World"}' \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic Sm9objpEb2U=" \
  -X POST http://localhost:8085/messages
````

### Subscribe to messages

There is also an endpoint where you can listen to new messages using Server Sent Events mechanism.

````shell
curl \
  -H "Authorization: Basic Sm9objpEb2U=" \
  -N http://localhost:8085/messages/subscribe
````

## WebSocket endpoint

The frontend uses WebSockets to retrieve new messages in realtime. The endpoint is exposed at `/message-socket`.

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

A more detailed documentation about available scripts can be found [here](./ui/README.md). 
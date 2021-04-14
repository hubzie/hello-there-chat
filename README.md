# Hello There Chat

## Run server
First of all you have to create database (sample scripts are in database folder).
```
cd server
mvn compile
mvn exec:java -Dexec.mainClass="pl.hellothere.server.Server"
```
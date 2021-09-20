#!/bin/bash

./gradlew clean shadowJar -q &> /dev/null
java -jar Memallook-App/build/libs/Memallook-App-1.0-all.jar
#!/bin/bash
docker build -t biospheere/codebasebot .
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD"
docker push biospheere/codebasebot
#!/bin/bash

git pull

docker build --no-cache -t spass_content_test .

#!/bin/bash
  
APP=con_spass_content_test

docker stop $APP

docker rm $APP

docker run -d -p 8200:8200 --name=$APP spass_content_test

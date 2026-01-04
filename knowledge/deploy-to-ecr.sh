#!/bin/bash

aws ecr create-repository --repository-name knowledge-indexing
printf "\n****knowledge-indexing repository created\n"

#docker build -t knowledge-indexing .
#docker tag knowledge-indexing:latest <ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:1.0.0
#docker push <ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:1.0.0

aws sts get-caller-identity

aws ecr get-login-password --region us-east-1 \
| docker login --username AWS --password-stdin 335242194235.dkr.ecr.us-east-1.amazonaws.com

docker build -t knowledge-indexing .
printf "\n****Built knowledge-indexing docker image\n"

docker tag \
  335242194235.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:1.0.0 \
  335242194235.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:latest

docker push 335242194235.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:1.0.0
docker push 335242194235.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing:latest
printf "\n****Pushed knowledge-indexing docker iamge to ecr\n"
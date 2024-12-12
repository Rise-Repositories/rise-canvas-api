#!/bin/bash

mvn clean install -DskipTests

account=$(aws sts get-caller-identity --query Account --output text)

aws ecr get-login-password --region us-east-1 | sudo docker login --username AWS --password-stdin $account.dkr.ecr.us-east-1.amazonaws.com

respository=$(aws ecr describe-repositories --repository-names rise --region us-east-1)

if [ -z "$respository" ]; then
    aws ecr create-repository --repository-name rise --region us-east-1
    echo "Repository created"
else
    echo "Repository already exists"
fi

sudo docker build -t rise-api .
sudo docker tag rise-api:latest $account.dkr.ecr.us-east-1.amazonaws.com/rise:latest
sudo docker push $account.dkr.ecr.us-east-1.amazonaws.com/rise:latest

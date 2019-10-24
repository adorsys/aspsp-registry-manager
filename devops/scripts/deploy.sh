#!/bin/bash
set -e

# install openshift-cli
curl -LsSf -O curl -LsSf -O https://github.com/openshift/origin/releases/download/${OC_VERSION}/openshift-origin-client-tools-${OC_VERSION}-${OC_VERSION_COMMIT}-linux-64bit.tar.gz
tar -zxvf openshift-origin-client-tools-${OC_VERSION}-${OC_VERSION_COMMIT}-linux-64bit.tar.gz
sudo mv openshift-origin-client-tools-${OC_VERSION}-${OC_VERSION_COMMIT}-linux-64bit/oc /usr/local/bin/oc

# install helm https://github.com/adorsys/dockerhub-pipeline-images/blob/master/ci-helm/2.12/Dockerfile
curl -LsSf -O https://storage.googleapis.com/kubernetes-helm/helm-${HELM_VERSION}-linux-amd64.tar.gz
tar -zxvf helm-${HELM_VERSION}-linux-amd64.tar.gz
sudo mv linux-amd64/helm /usr/local/bin/helm

# push docker image
docker login -u $DOCKER_USER -p $DOCKER_PASSWORD $DOCKER_REGISTRY
docker push $IMAGE

# deploy helm chart
oc login $OPENSHIFT_SERVER --token $HELM_TOKEN
helm init --client-only
helm upgrade --install --wait \
  --tiller-namespace xs2a-adapter-tiller \
  --namespace aspsp-registry-manager-dev \
  aspsp-registry-manager-dev \
  devops/charts/aspsp-registry-manager

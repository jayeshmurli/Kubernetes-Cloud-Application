#!/bin/bash

kubectl apply -f ../backend/namespacecreation.yaml

kubectl apply -f service-account.yaml

kubectl apply -f es-svc.yaml

kubectl apply -f elasticsearch-rs.yaml
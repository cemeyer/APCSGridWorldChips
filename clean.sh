#!/bin/bash

pushd GridWorldCode > /dev/null
ant clean
popd > /dev/null

pushd GridWorldCode/projects/gridchallenge/ >/dev/null
./clean.sh
popd >/dev/null

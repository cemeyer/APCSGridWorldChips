#!/bin/bash

pushd GridWorldCode > /dev/null
ant
popd > /dev/null

pushd GridWorldCode/projects/gridchallenge/ >/dev/null
./build.sh
popd >/dev/null

if [ "-r" = "$1" ]; then
  pushd GridWorldCode/projects/ >/dev/null
  ./run.sh
  popd >/dev/null

  ./clean.sh
fi

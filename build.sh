#!/bin/bash

if [ "-framework" = "$1" ]; then
  pushd GridWorldCode > /dev/null
  ant
  popd > /dev/null
fi

pushd GridWorldCode/projects/gridchallenge/ >/dev/null
./build.sh
popd >/dev/null

if [ "-r" = "$1" -o "-r" = "$2" ]; then
  pushd GridWorldCode/projects/ >/dev/null
  ./run.sh
  popd >/dev/null
fi

if [ "-c" = "$1" -o "-c" = "$2" ]; then
  ./clean.sh
fi

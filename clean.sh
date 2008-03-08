#!/bin/bash

if [ "-framework" = "$1" ]; then
  pushd GridWorldCode > /dev/null
  ant clean
  popd > /dev/null
fi

pushd GridWorldCode/projects/gridchallenge/ >/dev/null
./clean.sh
popd >/dev/null

#!/bin/bash
pushd ~/src/java/gridchallenge/GridWorldCode/projects/ >/dev/null
java -cp ~/src/java/gridchallenge/GridWorldCode/gridworld.jar:. gridchallenge.GridChallengeRunner
popd >/dev/null

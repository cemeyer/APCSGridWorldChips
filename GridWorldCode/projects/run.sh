#!/bin/bash
pushd ~/src/java/gridch/GridWorldCode/projects/ >/dev/null
#java -cp ~/src/java/gridch/GridWorldCode/gridworld.jar:. gridchallenge.GridChallengeRunner
java -cp ~/src/java/gridch/GridWorldCode/dist/GridWorldCode/gridworld.jar:. gridchallenge.GridChallengeRunner
popd >/dev/null

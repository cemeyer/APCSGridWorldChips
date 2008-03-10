#!/bin/bash

file=`date +'%Y%m%d-%H%M'`

zip -q -r -9 "$HOME/GridWorldCode-$file" GridWorldCode

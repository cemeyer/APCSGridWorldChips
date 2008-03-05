#!/bin/bash

for dir in . gc; do
  pushd $dir > /dev/null

  if [ "`echo *.class`" != '*.class' ]
  then
    rm *.class
  fi

  popd $dir > /dev/null
done

cd GridWorldCode
ant clean
cd ..

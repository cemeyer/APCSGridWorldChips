#!/bin/bash

if [ "$1" = "-d" ]
then
  debug="-Xlint:unchecked"
else
  debug=""
fi

for dir in . gc; do
  pushd $dir > /dev/null

  if [ "`echo *.java`" != '*.java' ]
  then
    javac *.java $debug
  fi

  popd $dir > /dev/null
done

cd GridWorldCode
ant compile
cd ..

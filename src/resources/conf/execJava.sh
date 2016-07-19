#!/bin/bash

BASEDIRSCRIPT=$(readlink -f "$0")
BASEDIR=$(dirname ${BASEDIRSCRIPT})

java -cp "${BASEDIR}/lib/*" org.appdynamics.extension.appdextract.SimpleExtract ${BASEDIR}/conf/AppDMetricExtract.xml

# EOF

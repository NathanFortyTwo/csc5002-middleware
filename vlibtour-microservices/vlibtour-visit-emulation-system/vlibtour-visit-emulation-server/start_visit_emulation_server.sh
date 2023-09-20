#!/bin/bash

ARGS=$*

export PATHSEP=':'

CLASSPATH=${CLASSPATH}${PATHSEP}./target/dependency/*${PATHSEP}./target/classes

CLASS=vlibtour.vlibtour_visit_emulation_server.VisitEmulationServer

# Start the client
CMD="java --add-opens=java.base/java.lang=ALL-UNNAMED -cp ${CLASSPATH} ${CLASS} ${ARGS}"

# this script is launched by sourcing => & and export
$CMD &
echo "$!" > ~/.vlibtour/visit_emulation_server

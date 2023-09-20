#!/bin/bash

ARGS=$*

export PATHSEP=':'

CLASSPATH=${CLASSPATH}${PATHSEP}./target/dependency/*${PATHSEP}./target/classes

CLASS=vlibtour.vlibtour_lobby_room_server.VLibTourLobbyServer

# Start the client
CMD="java --add-opens=java.base/java.lang=ALL-UNNAMED -cp ${CLASSPATH} ${CLASS} ${ARGS}"

# this script is launched by sourcing => & and export
$CMD &
echo "$!" > ~/.vlibtour/lobby_room_server

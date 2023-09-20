#!/bin/bash

ARGS=$*

PATHSEP=':'

CLASSPATH=${CLASSPATH}${PATHSEP}./target/dependency/*${PATHSEP}./target/classes

CLASS=vlibtour.vlibtour_tour_management_admin_client.VlibTourTourManagementAdminClient

# Start the client
CMD="java --add-opens=java.base/java.lang=ALL-UNNAMED -cp ${CLASSPATH} ${CLASS} ${ARGS}"

$CMD

#!/bin/bash

# killall java # to think about the possibility of processes from old executions!

export VLIBTOURDIR=${PWD}

# create directory to save process numbers (in order to kill them at the end)
if [ -d ~/.vlibtour ]; then
    rm -f ~/.vlibtour/*
else
    mkdir -p ~/.vlibtour
fi

echo -e "\e[1;44m  \e[1;33m ==================================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m = If necessary, clean, stop, and remove glassfish docker container = \e[0m "
echo -e "\e[1;44m  \e[1;33m ==================================================================== \e[0m "

if netstat -nlp 2> /dev/null | grep -q "[[:space:]]8080"; then
    docker exec glassfish asadmin undeploy vlibtour-tour-management-bean
    docker exec glassfish asadmin stop-database
    docker exec glassfish asadmin stop-domain domain1
    docker stop glassfish
    docker rm glassfish
fi
if netstat -nlp 2> /dev/null | grep -q "[[:space:]]4848"; then
    docker exec glassfish asadmin undeploy vlibtour-tour-management-bean
    docker exec glassfish asadmin stop-database
    docker exec glassfish asadmin stop-domain domain1
    docker stop glassfish
    docker rm glassfish
fi
if netstat -nlp 2> /dev/null | grep -q "[[:space:]]8181"; then
    docker exec glassfish asadmin undeploy vlibtour-tour-management-bean
    docker exec glassfish asadmin stop-database
    docker exec glassfish asadmin stop-domain domain1
    docker stop glassfish
    docker rm glassfish
fi

if ! docker image list | grep 'glassfish-tsp-csc' > /dev/null; then
    echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
    echo -e "\e[1;44m  \e[1;33m =          Build Docker container for Glassfish          = \e[0m "
    echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
    docker build -t glassfish-tsp-csc - < $VLIBTOURDIR/vlibtour-microservices/vlibtour-tour-management-system/glassfish-dockerfile
fi

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =          Start Docker container for Glassfish          = \e[0m "
echo -e "\e[1;44m  \e[1;33m =          and start Glassfish and Derby                 = \e[0m "
echo -e "\e[1;44m  \e[1;33m =          and deploy vlibtour-tour-management-bean      = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker run -itd --name glassfish -p 3700:3700 -p 8080:8080 -p 4848:4848 -p 8181:8181 -v $PWD/vlibtour-microservices/vlibtour-tour-management-system:/root/vlibtour-tour-management-system glassfish-tsp-csc
docker exec glassfish asadmin start-domain domain1
docker exec glassfish asadmin start-database
docker exec glassfish asadmin deploy /root/vlibtour-tour-management-system/vlibtour-tour-management-bean/target/vlibtour-tour-management-bean.jar
sleep 1

echo -e "\e[1;44m  \e[1;33m =============================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =        Run the tour management admin client application     = \e[0m "
echo -e "\e[1;44m  \e[1;33m = in order to populate the Derby database with POIs and tours = \e[0m "
echo -e "\e[1;44m  \e[1;33m =============================================================== \e[0m "
# populate the database with the POIs and the tours
(cd ./vlibtour-applications/vlibtour-tour-management-admin-client; ./start_tour_management_admin_client.sh populate toursAndPOIs)
sleep 1
# start the rabbitmq server
if netstat -nlp 2> /dev/null | grep -q "5672"; then
    docker stop rabbitmq
    docker rm rabbitmq
fi
sleep 1
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =          Start Docker container for RabbitMQ           = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker run -itd --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
sleep 10

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =              Start the lobby room server               = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# start the lobby room server
(cd ./vlibtour-microservices/vlibtour-lobby-room-system/vlibtour-lobby-room-server; ./start_lobby_room_server.sh)
# pid to kill at the end in ~/.vlibtour/lobby_room_server
sleep 3

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =           Start the visit emulation server             = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# start the visit emulation server
procNumber="$(netstat -nlp | grep 127.0.0.1:8888 | cut -d"N" -f2 | cut -d"/" -f1)"
if [ -n "$procNumber" ]; then
    echo "There is an old visit emulation server running; remove proc $procNumber"
    kill -9 "$procNumber"
fi
(cd ./vlibtour-microservices/vlibtour-visit-emulation-system/vlibtour-visit-emulation-server; ./start_visit_emulation_server.sh Dalton ParisBigTour)
# pid to kill at the end in ~/.vlibtour/visit_emulation_server
sleep 3

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =            Start the tourist applications              = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# start the tourist applications
# USAGE: userId (either Joe or Avrel) mode (initiate or join) tourId visitAlias (for the group)
(cd vlibtour-applications/vlibtour-tourist-application; ./start_tourist_application_w_emulated_location.sh Avrel initiate ParisBigTour Dalton)
# pid to kill at the end in ~/.vlibtour/tourist_applications
sleep 1
(cd vlibtour-applications/vlibtour-tourist-application; ./start_tourist_application_w_emulated_location.sh Joe join ParisBigTour Dalton)
# pid to kill at the end in ~/.vlibtour/tourist_applications
sleep 1

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =          Hit return to end the demonstration           = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
read x

echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =                 Stop the tourist applications          = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# kill the tourist applications, just in case
while read pid; do
    if [ $(ps aux | grep -c $pid) -gt 1 ]; then
        kill -9 $pid
    fi
done < ~/.vlibtour/tourist_applications
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =               Stop the lobby room server               = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# kill the lobby room server
kill -9 $(cat ~/.vlibtour/lobby_room_server)
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =            Stop the visit emulation server             = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
# kill the visit emulation server
kill -9 $(cat ~/.vlibtour/visit_emulation_server)
# stop the rabbitmq server
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =                 Stop RabbitMQ Docker container         = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker stop rabbitmq
docker rm rabbitmq

# empty the database, undeploy the bean, and stop the database and the domain
(cd ./vlibtour-applications/vlibtour-tour-management-admin-client; ./start_tour_management_admin_client.sh empty toursAndPOIs)
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =    Clean, stop and remove Glassfish Docker container   = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker exec glassfish asadmin undeploy vlibtour-tour-management-bean
docker exec glassfish asadmin stop-database
docker exec glassfish asadmin stop-domain domain1
docker stop glassfish
docker rm glassfish

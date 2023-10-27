#!/bin/bash

export VLIBTOURISTAPPLICATION=${PWD}
export VLIBTOURDIR=${PWD}/..

echo -e "\e[1;44m  \e[1;33m ============================================= \e[0m "
echo -e "\e[1;44m  \e[1;33m =      Integration tests of proxies to      = \e[0m "
echo -e "\e[1;44m  \e[1;33m =         microservice Lobby Room           = \e[0m "
echo -e "\e[1;44m  \e[1;33m = + microservice Group Communication System = \e[0m "
echo -e "\e[1;44m  \e[1;33m =  + microservice Visit Emulation Server    = \e[0m "
echo -e "\e[1;44m  \e[1;33m ============================================= \e[0m "

# start the visit emulation server
procNumber="$(netstat -nlp | grep 127.0.0.1:8888 | cut -d"N" -f2 | cut -d"/" -f1)"
if [ -n "$procNumber" ]; then
    echo "There is an old visit emulation server running; remove proc $procNumber"
    kill -9 "$procNumber"
fi
(
    cd $VLIBTOURISTAPPLICATION/../../vlibtour-microservices/vlibtour-visit-emulation-system/vlibtour-visit-emulation-server
    ./start_visit_emulation_server.sh Dalton ParisBigTour
)
# pid to kill at the end in ~/.vlibtour/visit_emulation_server
sleep 3

# start the rabbitmq server
if docker ps -a 2>/dev/null | grep -q "rabbitmq"; then
    docker stop rabbitmq
    docker rm rabbitmq
fi
sleep 1
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =          Start docker container for rabbitmq           = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker run -itd --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
sleep 10

# start the lobby room server
(
    cd $VLIBTOURISTAPPLICATION/../../vlibtour-microservices/vlibtour-lobby-room-system/vlibtour-lobby-room-server
    ./start_lobby_room_server.sh
)
# pid to kill at the end in ~/.vlibtour/lobby_room_server
sleep 3

mvn failsafe:integration-test failsafe:verify
result=$?

# kill the lobby room server
kill -9 $(cat ~/.vlibtour/lobby_room_server)

# kill the visit emulation server
kill -9 $(cat ~/.vlibtour/visit_emulation_server)

# stop the rabbitmq server
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =                 Stop rabbitmq docker container         = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker stop rabbitmq
docker rm rabbitmq

if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests.\033[0m"
    exit 1
fi

exit 0

#!/bin/bash

echo -e "\e[1;44m  \e[1;33m =========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =      Integration tests of microservice Lobby Room       = \e[0m "
echo -e "\e[1;44m  \e[1;33m =========================================================== \e[0m "

# start the rabbitmq server
if netstat -nlp 2> /dev/null | grep -q "5672"; then
    docker stop rabbitmq
    docker rm rabbitmq
fi
sleep 1
echo -e "\e[1;44m  \e[1;33m =========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =        Start docker container for rabbitmq              = \e[0m "
echo -e "\e[1;44m  \e[1;33m =========================================================== \e[0m "
docker run -itd --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
sleep 10

# start the lobby room server
./start_lobby_room_server.sh
# pid to kill at the end in ~/.vlibtour/lobby_room_server
sleep 3

mvn failsafe:integration-test failsafe:verify
result=$?

# kill the lobby room server
kill -9 $(cat ~/.vlibtour/lobby_room_server)

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

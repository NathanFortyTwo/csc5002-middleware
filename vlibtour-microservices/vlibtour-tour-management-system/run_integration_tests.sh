#!/bin/bash

export VLIBTOURTOURMANAGEMENT=${PWD}

echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "
echo -e "\e[1;44m  \e[1;33m =      Integration tests of microservice Tour Management       = \e[0m "
echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "

cd $VLIBTOURTOURMANAGEMENT/vlibtour-tour-management-integration-tests/

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
    echo -e "\e[1;44m  \e[1;33m =          Build docker container for glassfish          = \e[0m "
    echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
    docker build -t glassfish-tsp-csc - < $VLIBTOURTOURMANAGEMENT/glassfish-dockerfile
fi

echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "
echo -e "\e[1;44m  \e[1;33m =             Start docker container for glassfish,            = \e[0m "
echo -e "\e[1;44m  \e[1;33m =    undeploy previous vlibtour-tour-management application    = \e[0m "
echo -e "\e[1;44m  \e[1;33m =             and start again glassfish and derby              = \e[0m "
echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "
docker run -itd --name glassfish -p 3700:3700 -p 8080:8080 -p 4848:4848 -p 8181:8181 -v $VLIBTOURTOURMANAGEMENT/vlibtour-tour-management-bean/:/root/vlibtour-tour-management-bean glassfish-tsp-csc
docker exec glassfish asadmin start-domain domain1
docker exec glassfish asadmin start-database
docker exec glassfish asadmin deploy /root/vlibtour-tour-management-bean/target/vlibtour-tour-management-bean.jar
sleep 1

java --add-opens=java.base/java.lang=ALL-UNNAMED -cp ./target/dependency/*:./target/classes vlibtour.vlibtour_tour_management_bean_integration_test.VlibTourTourManagementBeanIntegrationTests
result=$?

# empty the database, undeploy the bean, and stop the database and the domain
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
echo -e "\e[1;44m  \e[1;33m =         Undeploy bean, stop database and domain        = \e[0m "
echo -e "\e[1;44m  \e[1;33m =        Stop and clean glassfish docker container       = \e[0m "
echo -e "\e[1;44m  \e[1;33m ========================================================== \e[0m "
docker exec glassfish asadmin undeploy vlibtour-tour-management-bean
docker exec glassfish asadmin stop-database
docker exec glassfish asadmin stop-domain domain1
docker stop glassfish
docker rm glassfish

if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests.\033[0m"
    exit 1
fi

exit 0

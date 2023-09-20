#!/bin/bash

# killall java # to think about the possibility of processes from old executions!

export VLIBTOURDIR=${PWD}

cd $VLIBTOURDIR/vlibtour-microservices/
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

cd $VLIBTOURDIR/vlibtour-applications/vlibtour-tourist-application
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

exit 0

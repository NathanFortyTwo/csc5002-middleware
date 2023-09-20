#!/bin/bash

export VLIBTOURMICROSERVICESDIR=${PWD}

cd $VLIBTOURMICROSERVICESDIR/vlibtour-visit-emulation-system/vlibtour-visit-emulation-server
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

cd $VLIBTOURMICROSERVICESDIR/vlibtour-bikestation
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

cd $VLIBTOURMICROSERVICESDIR/vlibtour-tour-management-system
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

cd $VLIBTOURMICROSERVICESDIR/vlibtour-lobby-room-system/vlibtour-lobby-room-server
./run_integration_tests.sh
result=$?
if ! [ "$result" = 0 ]; then
    echo -e "\033[0;31mSome problems in the integration tests... Do not continue.\033[0m"
    exit 1
fi

exit 0

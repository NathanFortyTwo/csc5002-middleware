#!/bin/bash

echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "
echo -e "\e[1;44m  \e[1;33m =   Integration tests of microservice Visit Emulation Server   = \e[0m "
echo -e "\e[1;44m  \e[1;33m ================================================================ \e[0m "
if ! mvn failsafe:integration-test failsafe:verify; then
    echo -e "\033[0;31mSome problems in the integration tests.\033[0m"
    exit 1
fi

exit 0

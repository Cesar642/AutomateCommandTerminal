#!/bin/bash
if [ $# -ne 2 ] ; then
    echo "usage: untarInsallRepli.sh serverListFile commandsFile"
    exit 1
fi
bold=$(tput bold)
normal=$(tput sgr0)

TIME=`date +"%d-%b-%y_%H_%M_%S"`
fileoutput="output_$TIME.out"

echo "Target servers:"
targetServers=$(cat $1|awk 'BEGIN{ORS=" "}{print $1}')
totalServers=$(cat $1|wc -l)
echo -e "\033[1;37m$targetServers \033[0m"
echo -e "\033[1;37m$totalServers servers in total \033[0m"

while true; do
read -p "Do you want to proceed? (yes/no) " yn
case $yn in
        [Yy]es ) while true; do
                   read -p "In Background? (yes/no) " bkg
                   case $bkg in
                     [Yy]es ) echo "Background process";
                              jdk-21.0.1/bin/java -cp .:expectj-2.0.7.jar:jsch-0.1.55.jar:commons-logging-1.2.jar:NewExpectj.jar com.business.client.NewAutomateCommand $1 $2 $user $passwordUser $fileoutput &
                              break;;
                     [Nn]o ) echo "Not Brackground process";
                              jdk-21.0.1/bin/java -cp .:expectj-2.0.7.jar:jsch-0.1.55.jar:commons-logging-1.2.jar:NewExpectj.jar com.business.client.NewAutomateCommand $1 $2 $user $passwordUser $fileoutput
                              break;;
                     * ) echo invalid response;;
                   esac
                 done
                 break;;
        [Nn]o ) echo Exiting...;
                exit;;
        * ) echo invalid response;;

esac
done


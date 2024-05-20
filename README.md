# AutomateCommandTerminal

## Multiprocess terminal executor  and multithread emulator program

This program allows to execute multiple commands into N servers at the same time. It uses JExpect library and connects to servers using ssh spawn threads.

- Inputs:
File with commands to execute in the N target servers
File with server names and passwords (optional) to connect to the target servers.
Password and user parameters to use in the central server (Bastion) that has access to the N target servers.

- Output:
Unique log file with the commands executed on each servers (stdout and stderr)

Example:

>servers.txt:
```
d14a0c919b5e password
039af89ab5e8 password
f95af38c47f3 password
```

>commands.txt:
>
```
#This is a comment like in a normal bash script
#Below a control variable
host=$(hostname)
#Below the commands to execute
pwd|awk -v host=$host '{print "** " host " " $0}'
whoami|awk -v host=$host '{print "** " host " " $0}'
df -kh|awk -v host=$host '{print "** " host " " $0}'
date|awk -v host=$host '{print "** " host " " $0}'
su -
whoami|awk -v host=$host '{print "** " host " " $0}'
```
>Execution:
```
export user=<user>
export passwordUser=<password>
```
```
./checkServersAuto.sh servers.txt commands.txt
```

![Automation](https://github.com/Cesar642/AutomateCommandTerminal/assets/44422221/19f0e998-27ce-4e89-b678-0787dd0ddd36)


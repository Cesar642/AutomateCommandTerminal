# AutomateCommandTerminal

## Multiprocess terminal executor  and multithread emulator program

This program allows to execute multiple commands into N servers at the same time. It uses JExpect library and connects to servers using ssh spawn threads.

** __In current version the expect string to be expected from target servers is the \@\<hostname\>, for example [root@ubuntuhost4 ~]__

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

### Working tree directory
```
├── checkServersAuto.sh
├── commons-logging-1.2.jar
├── expectj-2.0.7.jar
├── getDetailsOutput.sh
├── jdk-21.0.1
├── jsch-0.1.55.jar
└── NewExpectj.jar
```
** __Make sure the jdk directory, that can be downloaded from official Oracle, is placed on the working tree and that is correctly pointed in the checkServersAuto.sh script__

---
## Reading log file:
To read the log file created when running the program, use the getDetailsOutput.sh which allows to display 3 levels of information as follows:
```
-l 0 : Shows only the N target hosname servers where the commands have been executed. Useful to know how many servers have finished running all commands.
-1 1 : Shows everything
-l 2 : Shows everything pretty, showing a header of the server name like *************Server:<hostname>

./getDetailsOutput.sh -f output_12-Nov-23_21_40_02.out -l 2
```
### Execution

![screenshot-4e159cbc](https://github.com/Cesar642/AutomateCommandTerminal/assets/44422221/448c792f-8dc9-466b-91b9-c48e9f51f720)
![screenshot-a66164fe](https://github.com/Cesar642/AutomateCommandTerminal/assets/44422221/a5eb8de5-1d0b-4292-b0f1-091372481bb3)


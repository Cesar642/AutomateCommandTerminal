#!/bin/bash

TARGETFILE=""
serverList(){
 #echo "$TARGETFILE"
 cat $TARGETFILE|awk '/Server:/{n=split($0,a,":");print a[2]}'
}

filteredDetail(){
 #echo "$TARGETFILE"
 cat $TARGETFILE|awk '/^\*\* /'
}

noFiltered(){
 #echo "$TARGETFILE"
 cat $TARGETFILE|awk '/^\*\*/'
}

showHelp() {
   # `cat << EOF` This means that cat should stop reading when EOF is detected
   cat << EOF
   Usage: $0 <--help|-h|-l> 
               $0 <--level|-l> <0,1,2>
EOF
# EOF is found above and hence cat command stops reading. This is equivalent to echo but much neater when printing out.
exit 0
}

options=$(getopt -l "help,file:,level:" -o "hf:l:" -- "$@" )
# set --:
eval set -- "$options"
#MAIN RUN
while true
do
   case "$1" in
      -h|--help)
         showHelp
         ;;
      -f|--file)
         shift
           TARGETFILE=$1
           ;;
      -l|--level) 
       shift
            LEVEL=$1
                  ;;
      --)
         shift
         break
                 ;;
   esac
   shift
done

cat $TARGETFILE| sed 's/\x1b\[[?]\{0,1\}[0-9;]*[a-zA-Z]//g' | tr -d '\r' > $TARGETFILE"_2" 
sed -i 's/^e\[?2004l//g' $TARGETFILE"_2"
sed -i 's/\$//g' $TARGETFILE"_2"
mv $TARGETFILE"_2" $TARGETFILE

case $LEVEL in
    0)
     serverList
      ;;
    1)
     filteredDetail
      ;;
    2)
     noFiltered
      ;;
esac
exit 0


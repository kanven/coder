#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
HOME_DIR=`pwd`
LIB_DIR=$HOME_DIR/lib
CONF_DIR=$HOME_DIR/conf
TMPL_DIR=$HOME_DIR/tmpl
LOG_DIR=$HOME_DIR/log
PID=`ps -f|grep java|grep $CONF_DIR|awk '{print $2}'`
if [ -n "$PID" ]
then
   echo '服务已经启动！'
   exit 1
fi
if [ ! -d $LOG_DIR ]
then
   mkdir $LOG_DIR
fi
STD_OUT_FILE=$LOG_DIR/stdout.log
LIB_JARS=`ls $LIB_DIR| grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
echo $LIB_JARS
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2g -Xms2g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi
echo "starting server....."
nohup java $JAVA_MEM_OPTS -classpath $CONF_DIR:$TMPL_DIR:LOG_DIR:LIB_JARS com.kanven.tools.code.Coder > $STD_OUT_FILE 2>&1 &
COUNT=0
while [ $COUNT -lt 1 ]
do
   echo -e ".\c"
   sleep 1
   COUNT=`ps -e|grep java|grep $CONF_DIR|awk '{print $2}'|wc -l`
   if [ $COUNT -gt 0 ]
     then 
        break
   fi
done
echo "Server started"
exit 0

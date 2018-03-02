#!/bin/bash
export JAVA_HOME=/media/mint/72B972CB80CF3A87/jre1.7.0_80
export PATH=$JAVA_HOME/bin:$PATH
java -jar /home/mint/IdeaProjects/ogm/build/libs/ogm-1.0-SNAPSHOT.jar

export JAVA_HOME=/media/mint/72B972CB80CF3A87/jdk-9.0.4
export PATH=$JAVA_HOME/bin:$PATH
java -jar /home/mint/IdeaProjects/ogm/build/libs/ogm-1.0-SNAPSHOT.jar

#export JAVA_HOME=/media/mint/72B972CB80CF3A87/jdk1.7.0_80
#export PATH=$JAVA_HOME/bin:$PATH

#Для gradle из командной строки
export JAVA_HOME=/media/mint/72B972CB80CF3A87/jdk1.7.0_80
export PATH=$JAVA_HOME/bin:$PATH
export PATH=/media/mint/72B972CB80CF3A87/gradle-4.5.1/bin:$PATH


java -cp $JAVA_HOME/jre/lib/*:./* gui.mainwindow.MainWindow

export JAVA_HOME=/media/mint/72B972CB80CF3A87/jre1.7.0_80
export PATH=$JAVA_HOME/bin:$PATH
java -cp $JAVA_HOME/lib/*:./* gui.mainwindow.MainWindow
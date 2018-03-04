chcp 1251
echo "%~dp0"
echo %~dp0
echo %cd%
SET JAVA_HOME="W:\jre-7u80-windows-i586\jre1.7.0_80"
SET PATH="%JAVA_HOME%\bin";%PATH%
java -cp "W:\ogmJava\*";"%JAVA_HOME%\lib\*" gui.mainwindow.MainWindow

REM для Нытвы Gradle
SET JAVA_HOME="C:\Program Files (x86)\Java\jdk1.7.0_51"
SET PATH=%JAVA_HOME%\bin;%PATH%
set PATH=D:\Distr\Programming\Java\gradle-4.5.1\bin;%PATH%
chcp 1251
echo "%~dp0"
echo %~dp0
echo %cd%
SET JAVA_HOME="W:\jre-7u80-windows-i586\jre1.7.0_80"
SET PATH="%JAVA_HOME%\bin";%PATH%
java -cp "W:\ogmJava\*";"%JAVA_HOME%\lib\*" gui.mainwindow.MainWindow
@ECHO OFF
ECHO Install the required modules. Then make package for web module.
cd ..
call mvn clean install
cd web
mvn clean package
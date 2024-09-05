@ECHO OFF
SET JAVA_OPTS='-server -Xms512m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=512m -XX:NewSize=192m -XX:MaxNewSize=384m -Djava.awt.headless=true -Dhttp.agent=Sakai -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dsun.lang.ClassLoader.allowArraySyntax=true' 

REM Should use the absolute path for folder release

REM Fill your home folder of Sakai Tomcat here
REM SET TOMCAT_HOME=

IF "%TOMCAT_HOME%"=="" ( 
    set /p TOMCAT_HOME="Input the path of the TOMCAT: "
) 

REM Copy resources from web module to sakai module
copy .\web\src\main\webapp\WEB-INF\app-config.properties .\tool\src\main\webapp\WEB-INF
copy .\web\src\main\webapp\WEB-INF\springapp-servlet.xml .\tool\src\main\webapp\WEB-INF
copy .\web\src\main\webapp\WEB-INF\applicationContext.xml .\tool\src\main\webapp\WEB-INF

mkdir .\tool\src\main\webapp\WEB-INF\templates\fragments
xcopy .\web\src\main\webapp\WEB-INF\templates\fragments .\tool\src\main\webapp\WEB-INF\templates\fragments /S /Y
xcopy .\web\src\main\webapp\WEB-INF\templates\*.* .\tool\src\main\webapp\WEB-INF\templates\ /Y

REM mkdir .\tool\src\main\webapp\resources
REM xcopy .\web\src\main\webapp\resources .\tool\src\main\webapp\resources /S /Y

CALL mvn clean package sakai:deploy -Dmaven.tomcat.home=%TOMCAT_HOME% -Dmaven.test.skip=true -f pom-sakai.xml

@PAUSE
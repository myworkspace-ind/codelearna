@ECHO OFF
REM # Replay "A" by "B" from file source.txt into 
REM sed -e 's/A/B/g' source.txt > dest.txt
REM // <parent> => <!--parent>
REM // </parent> => </parent-->

sed -i -e "s/<parent>/<!--parent>/g" -e "s/<\/parent>/<\/parent-->/g" web\pom.xml
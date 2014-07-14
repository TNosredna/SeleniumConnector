erase /Q tmp
mkdir tmp
mkdir tmp\img
mkdir tmp\img\failed
mkdir tmp\xml
mkdir tmp\html

java -jar selenese-runner.jar --driver firefox --screenshot-dir tmp\img --screenshot-all tmp\img\failed --xml-result tmp\xml --html-result tmp\html %1
REM java -cp selenese-runner.jar jp.vmi.selenium.selenese.Main --driver firefox --screenshot-dir tmp\img --screenshot-all tmp\img\failed --xml-result tmp\xml --html-result tmp\html %1
rm -r /opt/tomcat/webapps/crud /opt/tomcat/webapps/crud.war
cd ~/dev/crud/
mvn package
mv ~/dev/crud/target/crud.war /opt/tomcat/webapps/

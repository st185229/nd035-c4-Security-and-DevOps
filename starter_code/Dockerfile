# we are extending everything from tomcat:8.0 image ...
FROM tomcat:9.0.43-jdk11-openjdk
MAINTAINER Suresh Thomas
EXPOSE 8080
# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
ADD target/${project.artifactId}.war /usr/local/tomcat/webapps/udacity.war
CMD ["catalina.sh","run"]
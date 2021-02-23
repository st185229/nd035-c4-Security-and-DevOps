# we are extending everything from tomcat:8.0 image ...
FROM tomcat:8.0
MAINTAINER Suresh Thomas
EXPOSE 8080
# Remove any existing
RUN rm -rf /usr/local/tomcat/webapps/ROOT
# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
ADD target/${project.artifactId}-${project.version}.war /usr/local/tomcat/webapps/
CMD ["catalina.sh","run"]
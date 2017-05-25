#Docker file for running tests
FROM maven:3.5.0-openjdk8u111

WORKDIR /usr/local/workspace/enterprise

RUN mkdir -p redhawk-driver; mkdir -p redhawk-connector; mkdir -p redhawk-camel; mkdir -p redhawk-websocket; mkdir -p redhawk-rest

#Add full root to workspace
#ADD . . Figure efficient way to do this. The node_modules dir has a crap ton of data and need to ignore target
COPY pom.xml .
COPY redhawk-driver redhawk-driver/
COPY redhawk-connector redhawk-connector/
COPY redhawk-rest redhawk-rest/
COPY redhawk-websocket redhawk-websocket/
COPY redhawk-camel redhawk-camel/
COPY settings.xml /root/.m2

CMD ["/bin/bash"]

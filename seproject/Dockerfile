FROM ubuntu:16.04
LABEL maintainer = "dormarble dormarble <gudfla0814@gmail.com>"
RUN apt-get update
RUN apt-get install default-jre -y

RUN mkdir -p /var/www
ADD ./build/libs/server.jar /var/www

EXPOSE 80
CMD java -jar /var/www/server.jar

FROM openjdk:11.0-jre-buster
LABEL maintainer="研究院研发组 <research-maint@itcast.cn>"
ENV JAVA_OPTS=""
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app
ADD app.jar /app/app.jar

ENTRYPOINT ["sh","-c","java  -jar $JAVA_OPTS /app/app.jar"]
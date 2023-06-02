FROM maven:3.9.2-eclipse-temurin-17-alpine AS BUILDER
#RUN apt-get update && apt-get install maven -y
RUN ls -la

RUN mkdir /build
COPY . /build
WORKDIR /build

RUN cd /build && echo "--------AQQUI ESTAMOS-----" && pwd && ls -la

RUN mvn install
RUN  ls -la
RUN cd target/ && pwd && ls -la

FROM openjdk:17-alpine
COPY --from=BUILDER /build/target/eureka-server-0.0.1-SNAPSHOT.jar .
RUN echo "-----------aqui estoy en openjdk alpine-----" && pwd && ls -la
RUN chmod +x eureka-server-0.0.1-SNAPSHOT.jar
RUN echo "-----------observando el artifact-----" && pwd && ls -la 
EXPOSE 8761
CMD ["java", "-jar", "eureka-server-0.0.1-SNAPSHOT.jar"]  

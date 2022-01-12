FROM openjdk:17-oracle
EXPOSE 8080
COPY /target/adminService.jar adminService.jar
ENTRYPOINT ["java","-jar","/adminService.jar"]


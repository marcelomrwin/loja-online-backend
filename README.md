# runtime-springboot

Created by the Cloud App Generator

## Init Postgres
docker run --rm -d -p 5432:5432 -e POSTGRESQL\_USER=lojaonline -e POSTGRESQL\_PASSWORD=lojaonline -e POSTGRESQL\_DATABASE=lojaonline -e POSTGRESQL\_ADMIN_PASSWORD=lojaonline -v ~/data/postgres:/var/lib/postgresql/data registry.access.redhat.com/rhscl/postgresql-10-rhel7

_Remove <b>-v ~/data/postgres:/var/lib/postgresql/data</b> if you don't need persistent volume_ 

## Execute application
mvn spring-boot:run

## Access swagger API
http://localhost:8090/swagger-ui.html

## Access the application
http://localhost:8090/index.xhtml

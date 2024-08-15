<p align="center">
  <a href="https://spring.io/projects/spring-boot" target="blank"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/2560px-Spring_Framework_Logo_2018.svg.png" width="320" alt="Spring Boot Logo" /></a>
</p>

# **Librería Base**

## **Descripción**
Librería desarrollada en Java con el framework Spring Boot 3 y base de datos MongoDB. Librería base de la fábrica de software FullSpectrum Tech.

## **Pre-requisitos**
Para clonar y ejecutar esta aplicación, necesitará [Git](https://git-scm.com), [Java 17.0.11](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) y [Maven](https://maven.apache.org/download.cgi) instalados en su computadora. 

Desde su línea de comando:

```bash
# Clonar repositorio
$ git clone https://github.com/fstech-co/lib-base-spring-webflux

# Entrar al repositorio local
$ cd lib-base-spring-webflux

# Instalar dependencias
$ mvn install
```

## **Ejecutar pruebas unitarias y SonarQube**

Si no cuenta con un servidor SonarQube remoto, tiene la posibilidad de ejecutarlo mediante un contendedor Docker con el siguiente comando:

```bash
$ docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
```

Desde su línea de comando:

```bash
# Comando para ejecutar pruebas unitarias con cobertura y generar informe herramienta SonarQube
$ mvn clean verify sonar:sonar -Dsonar.login=<SONARQUBE_LOGIN> -Dsonar.password=<SONARQUBE_PASSWORD>
-Dsonar.projectKey=lib-base-spring-webflux -Dsonar.projectName='lib-base-spring-webflux' -Dsonar.host.url=<SONARQUBE_URL>
```
Una vez finalizado el proceso, podrá acceder desde el [navegador](http://localhost:9000/projects?sort=name) para validar que se visualice correctamente el informe de SonarQube.

## **Módulos del proyecto**

- **Common**:
  Módulo transversal en el cual se define la configuración, librerías, enumeradores, útilidades.

- **Application**:
  Modulo en el cual se definen los paths o funcionalidades que expone el servicio

- **Core**:
  Módulo en el cual se implementa la lógica de negocio (use cases)

- **Provider**:
  Módulo que controla la conexión a legados, base de datos y servicios con los cuales se tiene comunicación

## **Autores**
Los diferentes autores y encargados de cada operación de la aplicación para inquietudes son:

| Operación             | Autor                  | Correo                    |
| --------------------- |------------------------|---------------------------|
| General               | David Corredor Ramírez | dgcorredorr@gmail.com |
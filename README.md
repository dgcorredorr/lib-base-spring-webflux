<p align="center">
  <a href="https://spring.io/projects/spring-boot" target="blank"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/2560px-Spring_Framework_Logo_2018.svg.png" width="320" alt="Spring Boot Logo" /></a>
</p>

# **Librería Base**

## **Descripción**
Librería desarrollada en Java con el framework Spring Boot 3 y base de datos MongoDB. Librería base.

## **Pre-requisitos**
Para clonar y ejecutar esta aplicación, necesitará [Git](https://git-scm.com), [Java 17.0.11](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) y [Maven](https://maven.apache.org/download.cgi) instalados en su computadora. 

Desde su línea de comando:

```bash
# Clonar repositorio
$ git clone https://github.com/dgcorredorr/lib-base-spring-webflux

# Entrar al repositorio local
$ cd lib-base-spring-webflux

# Instalar dependencias
$ mvn install
```

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
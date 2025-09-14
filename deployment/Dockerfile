# Funciona también si usan Java 17, como yo
FROM eclipse-temurin:21-jdk-alpine

# Crear usuario no root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Crear directorio de logs / opcional si tienes un archivo de configuracion de logs
# que guarda en una ruta específica. example: logback-spring.xml
RUN mkdir -p /logs && chown -R appuser:appgroup /logs

VOLUME /tmp

# Copiar JAR generado previamente con: ./gradlew :app-service:clean :app-service:bootJar
COPY ../applications/app-service/build/libs/AuthenticationMS.jar AuthenticationMS.jar

# Configuración de rendimiento
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Usar usuario no-root
USER appuser

# Ejecutar la app
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar AuthenticationMS.jar" ]

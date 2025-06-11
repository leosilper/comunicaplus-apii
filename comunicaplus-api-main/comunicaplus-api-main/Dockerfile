# Usa uma imagem base do Java 17
FROM eclipse-temurin:17-jdk

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . .

# Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Compila o projeto sem rodar os testes
RUN ./mvnw clean package -DskipTests

# Expõe a porta usada pela aplicaçãoo
EXPOSE 8080

# Executa o arquivo JAR gerado
CMD ["java", "-jar", "target/comunicaplus-api-main-0.0.1-SNAPSHOT.jar"]

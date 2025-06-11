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

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Executa o arquivo JAR gerado (nome correto com base no pom.xml)
CMD ["java", "-jar", "target/comunica_api-0.0.1-SNAPSHOT.jar"]

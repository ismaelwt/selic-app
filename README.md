# API spring boot para calcular a de juros baseada na taxa selic.

mvn clean install -U

mvn clean test

mvn jacoco:report - code coverage

java -jar target/selic.jar

http://localhost:8080/swagger-ui.html

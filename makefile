run:
	./mvnw clean package && java -jar app/target/app-1.0.0.jar

test:
	./mvnw test

test-report:
	./mvnw clean package

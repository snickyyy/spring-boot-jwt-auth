
app-build:
	@echo "Building the application..."
	gradlew jibDockerBuild

app-run: app-build
	@echo "Running the application..."
	docker run --env-file .env -p 8080:8080 spring-boot-jwt-auth

app-stop:
	@echo "Stopping the application..."
	docker stop spring-boot-jwt-auth

app-exec:
	@echo "Accessing the application container..."
	docker exec -it spring-boot-jwt-auth /bin/sh

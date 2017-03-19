# Building main project

1) Go to your local project path, for example:
~~~~
C:\IdeaProjects\token-auth
~~~~
2) Run command:
~~~~
gradlew :spring-token-auth:build
~~~~

# Running project

1) Run built application:
~~~~
java -jar spring-token-auth\build\libs\spring-token-auth-1.0-SNAPSHOT.jar
~~~~
2) Debug mode:
~~~~
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n -jar spring-token-auth\build\libs\spring-token-auth-1.0-SNAPSHOT.jar
~~~~
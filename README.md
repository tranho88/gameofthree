
# READ ME  
  
### Techstack  
JAVA 9  
Spring boot  
Junit  
Mockito  
Maven  
  
### Guides  
  
1. Step 1: Navigate to bin directory and modify the properties files if needed.
 2. Step 2:
 In the terminal, run the two following commands to start two game players:    

        java -jar game.of.three-0.0.1-SNAPSHOT.jar --spring.config.name=application-pl1
        java -jar game.of.three-0.0.1-SNAPSHOT.jar --spring.config.name=application-pl2

 3. Step 3:
In the terminal, run the below command to start the game:
 -  Auto mode: (configured in the config file)
		
		    curl --location --request POST 'http://localhost:8080/game/init' \
			--header 'Content-Type: application/json' \
			--data-raw '{}'


 
  - Manual mode:
 

	    curl --location --request POST 'http://localhost:8081/game/init' \
		--header 'Content-Type: application/json' \
		--data-raw '{
				"number": 100
		}'  


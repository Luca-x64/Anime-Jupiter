# Anime Jupiter

## todolist:
# front:
    - add register.fxml
    - fix login.fxml

# back:
    - TODO registerController
    - spostare la connessione del socket (client) al file `main.App.java`
    + Login con password hash B CRYPT [OK]

# database:
    - add anime table
    - add favourite table (N-N tra users e anime)


# bug:
    - [BACK] doppio click sul tasto login (loginController->login()) con dati sbagliati crea un crash


# Users: [name,email,pw]
- Admin: `admin, admin@gmail.com,admin`
- others: `luca,luca@gmail.com,luca`,
         `manuel,manuel@gmail.com,manuel`


== How to kill the process with the xx port used:
- Windows: 
`netstat -ano | findstr :<PORT> `
`taskkill /PID <PID> /F`
- Linux: Prossimamente

== Sql tips:
ALTER TABLE tablename AUTO_INCREMENT = <N>


For run the program: 
```
mvn clean javafx:run@client 
mvn clean javafx:run@server
```


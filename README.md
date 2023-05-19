# Anime Jupiter

## todolist:
# front:
    - change register.fxml
    - change login.fxml

# back:
   - after register process OK (What to do???)

# database:
    - add anime table
    - add favourite table (N-N tra users e anime)


# bug:
    - [BACK] doppio click sul tasto login (loginController->login()) con dati sbagliati crea un crash
    - login, back to login, re-attempt login crash


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
ALTER TABLE tablename AUTO_INCREMENT = `<N>`


For run the program: 
```
mvn clean javafx:run@client 
mvn clean javafx:run@server
```

# Credits:
- BCrypt: https://www.mindrot.org/projects/jBCrypt/


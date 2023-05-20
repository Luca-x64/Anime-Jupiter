# Anime Jupiter

## todolist:
# front:
    - change register.fxml
    - change login.fxml
    - RegisterController gotoLogin() not working
    - gestione messaggio di uscita addAnime
    - windows size on Linux is bugged, on windows seems ok

# back:
    - finish updating AdminController
    - check update AddAnimeController
    - update EditAnimeController

    - after register process OK (What to do???)
    

# database:
    


# bug:
    - [BACK] doppio click sul tasto login (loginController->login()) con dati sbagliati crea un crash
    - [BACK] login, back to login, re-attempt login crash
    - OpenLink (Engine) non funziona => crash 


# Users: [name,email,pw]
- Admin: `admin, admin@gmail.com,admin`
- others: `luca,luca@gmail.com,luca`,
         `manuel,manuel@gmail.com,manuel`


== How to kill the process with the xx port used:
- Windows: 
`netstat -ano | findstr 20006 `
`taskkill /PID <PID> /F`
- Linux: (non serve perchè è un sistema fatto bene)

== Sql tips:
ALTER TABLE tablename AUTO_INCREMENT = `<N>`


For run the program: 
```
mvn javafx:run@server / mvn clean javafx:run@server
mvn javafx:run@client / mvn clean javafx:run@client 
```

# Credits:
- BCrypt: https://www.mindrot.org/projects/jBCrypt/


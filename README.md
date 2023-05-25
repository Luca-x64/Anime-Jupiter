# Anime Jupiter

## todolist:
# front:
    - make switch for login and register
    - windows size on Linux is bugged, on windows seems ok
    - ri aggiungere  (in AdminController) .setOnCloseRequest di editAnime e di addAnime (con il messaggio rosso di errore)
    - set all stages .onHiding (gestione dell'uscita del pannello (es: chiusura socket, etc...))
    CHECK scroll messagge addAnime e editAnime working !?

# back:
    - CHECK (not working) register (serverThread , autoLogin ) 
    - alert add e edit, cambiare Color class (not serializable), con stringa

# database:
    


# bug:
    
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


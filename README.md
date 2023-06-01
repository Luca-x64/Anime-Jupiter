# Anime Jupiter

## todolist:
# front:
    - set all stages .onHiding (gestione dell'uscita del pannello (es: chiusura socket, etc...))
    CHECK correttezza funzionamento dei messaggi alert di add e edit anime
    - posizione dei pannelli add e edit anime, 
    - dimensione dei pannelli admin/user/login/register
    - caratteri giapponesi su linux

# back:
    - CHECK (not working) register (serverThread , autoLogin )
    - TODO close client, create a server exception (stampa null in console, exit = 6) 
    - CHECK server close 

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


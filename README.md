# Anime Jupiter
developing second release

# TODO-List:
## Front-End:
    - set all stages .onHiding (gestione dell'uscita del pannello (es: chiusura socket, etc...))
    - CHECK correttezza funzionamento dei messaggi alert di add e edit anime
    - CHECK colori e simboli dei messaggi di ALERT, rosso = errore, verde = successo, e giallo = avviso
    - TODO in user controller, per l'anime selezionato, visualizzare dinamicamente lo stato del preferito ( Object Anime.getFavourite() )
    
## Back-End:
    - CHECK query favourite colonna non trovata quando si filtra per i favoriti
    - TODO CHECK gestione errori
    - TODO button filter favourite
## DataBase:
    


# Bug:


# Users: [name,email,pw]
- Admin: `admin, admin@gmail.com,admin`
- others: `luca,luca@gmail.com,luca`,
         `manuel,manuel@gmail.com,manuel`


# For run the program: 

Required Japanese character: `noto-fonts-cjk` on endevour OS (Arch)

```
mvn javafx:run@server / mvn clean javafx:run@server
mvn javafx:run@client / mvn clean javafx:run@client 
```

# Credits:
- BCrypt: https://www.mindrot.org/projects/jBCrypt/  `(Blowfish password hashing algorithm)`


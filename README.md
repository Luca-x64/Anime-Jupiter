# Anime Jupiter
developing second release

# TODO-List:
## Front-End:
    - TODO [USER] per l'anime selezionato, visualizzare dinamicamente lo stato del preferito userController.setChosenAnime [riga 264]
    - TODO [USER] bottone per visualizzare gli anime preferiti, fare in modo che si capisca quando è attivo e quando no, tipo un interruttore ON/OFF
    
## Back-End:
    - TODO CHECK gestione errori
## DataBase:
    


# Bug:


# Users: [name,email,pw]
- Admin: `admin, admin@gmail.com,admin`
- others: `luca,luca@gmail.com,luca`,
         `manuel,manuel@gmail.com,manuel`

# Requirements:
    - jdk-20 ( https://www.oracle.com/java/technologies/downloads/ ) 
    - javafx-sdk-20.0.2 ( https://gluonhq.com/products/javafx/ )
    - maven ( https://maven.apache.org/download.cgi?.)  => add to PATH C:\Program Files\apache-maven-3.9.4\bin
    
    Required Japanese character: `noto-fonts-cjk` on endevour OS (Arch)

# For run the program: 

```
mvn javafx:run@server / mvn clean javafx:run@server
mvn javafx:run@client / mvn clean javafx:run@client 
```

# Credits:
- BCrypt: https://www.mindrot.org/projects/jBCrypt/  `(Blowfish password hashing algorithm)`


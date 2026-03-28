# Anime Jupiter
developing second release

## DataBase:
Specs: MySql > 8.0
File in src/sql

# Bug:


# Users: [name,email,pw]
- Admin: `admin, admin@gmail.com,admin`
- others: `luca,luca@gmail.com,luca`,
         `manuel,manuel@gmail.com,manuel`

# Requirements:
    - JDK-21 ( https://www.oracle.com/java/technologies/downloads/ ) 
    
    Optional for Japanese characters: `noto-fonts-cjk` on endevour OS (Arch)

# For run the program: 

```
./gradlew runServer
./gradlew runClient
```

# Credits:
- BCrypt: https://www.mindrot.org/projects/jBCrypt/  `(Blowfish password hashing algorithm)`


# TODO-List: //TODO check if still present
## Front-End:
    - TODO [USER] sorting favourite
    - TODO [USER] fix reload time for sorting
## Back-End:
    - TODO CHECK gestione errori

package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public record User (@Nullable String username, @NotNull String email, @NotNull String password) implements Serializable {

    public User(@NotNull String email,@NotNull String pw) {
        this(null, email, pw);
//        this(email.substring(0, email.indexOf('@')), email, pw); // TODO, serverThread depency on username=null
    }
    
}

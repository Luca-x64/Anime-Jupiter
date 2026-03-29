package interfaces;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Anime;
import org.jetbrains.annotations.NotNull;

public interface SetDataEdit {
    void setData(@NotNull Anime anime,@NotNull  ObjectOutputStream os,@NotNull  ObjectInputStream is);
}

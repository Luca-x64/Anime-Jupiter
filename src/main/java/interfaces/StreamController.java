package interfaces;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public interface StreamController {
    void setStream(@NotNull ObjectOutputStream os,@NotNull ObjectInputStream is);

}

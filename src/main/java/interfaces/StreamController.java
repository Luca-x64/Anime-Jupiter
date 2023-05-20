package interfaces;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public interface StreamController {
    public void setStream(ObjectOutputStream os,ObjectInputStream is);
}

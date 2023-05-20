package interfaces;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Anime;

public interface SetDataEdit {
    public void setData(Anime anime, ObjectOutputStream os, ObjectInputStream is);
}

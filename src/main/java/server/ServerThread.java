package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.Config;
import database.Database;
import jbcrypt.BCrypt;

public class ServerThread implements Runnable {

    private Socket socket;
    private Database DB;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    public ServerThread(Socket s,Database db) throws IOException{
        this.DB=db;
        this.socket=s;
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        login();
    }

    private void login() {
        String email = (String) receive();
        String password = (String) receive();
        
        

        String queryLogin = "SELECT * FROM users WHERE email=?";

        //TEMP [codice per aggiungere un utente, da aggiungere il prepared statement] 
        // password = BCrypt.hashpw(password, BCrypt.gensalt());
        // String queryRegister = "INSERT INTO users (nome,email,password) values('manuel','manuel@gmail.com','"+password+"')";
        // try {
        //     DB.getConn().createStatement().execute(queryRegister);
        //     System.exit(0);
        // } catch (SQLException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        //FINE TEMP

        ResultSet rs=null;
        Boolean verified = false;
        try (PreparedStatement pst = DB.getConn().prepareStatement(queryLogin)){
            pst.setString(1, email);
            rs = pst.executeQuery();
            boolean checkEmail = rs.next();
            send(checkEmail);
            if(checkEmail){
                String hashedPassword = rs.getString("password");
                verified = BCrypt.checkpw(password,hashedPassword);
                send(verified);
                if(verified){
                    send(rs.getBoolean("isAdmin"));
                }
            }
            
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


    private void send(Object o) {
        try {
        os.writeObject(o);
        os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private Object receive(){
        Object received = null;
        try {
            received = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return received;
    }

}

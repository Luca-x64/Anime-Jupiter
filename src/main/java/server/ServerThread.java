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
import model.User;

public class ServerThread implements Runnable {

    private Socket socket;
    private Database DB;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private User user;

    public ServerThread(Socket s, Database db) throws IOException {
        this.DB = db;
        this.socket = s;
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        account();
    }

    private void account() {
        user = (User) receive();
        if (user.getUsername() == null) {
            login();
        } else {
            register();
        }
    }

    private void login() {
        String queryLogin = "SELECT * FROM users WHERE email=?";
        ResultSet rs = null;
        Boolean verified = false;
        try (PreparedStatement pst = DB.getConn().prepareStatement(queryLogin)) {
            pst.setString(1, user.getEmail());
            rs = pst.executeQuery();
            boolean checkEmail = rs.next();
            send(checkEmail);
            if (checkEmail) {
                String hashedPassword = rs.getString("password");
                verified = BCrypt.checkpw(user.getPassword(), hashedPassword);
                send(verified);
                if (verified) {
                    send(rs.getBoolean("isAdmin"));
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void register() {
        String queryRegister = "INSERT INTO users (nome,email,password) VALUES (?,?,?)";
        try (PreparedStatement pst = DB.getConn().prepareStatement(queryRegister)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            int response = pst.executeUpdate();
            if (response == 1) {
                send(true);
            } else {
                send(false);
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

    private Object receive() {
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

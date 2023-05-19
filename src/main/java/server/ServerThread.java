package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import config.Config;
import database.Database;
import jbcrypt.BCrypt;
import model.Anime;
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
        while (socket.isConnected()) {
            functions();
        }
    }

    private void functions() {
        Object o = receive();
        System.out.println((Integer) o);
        int receive = (Integer) o;
        switch (receive) {
            case 1: { 
                selectAnime("SELECT * FROM anime");
                break;
            }
            case 2: { 
                int user_id = (Integer) receive();
                selectAnime("SELECT a.* FROM anime as a INNER JOIN favourite as f on a.id=f.anime_id where id=\""+user_id+"\"");
                break;
            }
            case 3: { // add anime
                Anime anime = (Anime) receive();
                insertAnime(anime);
                break;
            }
            case 4: { 
                String search = (String) receive();
                searchAnime("SELECT * FROM anime WHERE title LIKE = ?",search);
                break;
            }

            default: {

            }

        }
    }

    private void searchAnime(String query,String search) {
        List<Anime> animeList = new ArrayList<>();
        try {
            PreparedStatement pst =  DB.getConn().prepareStatement(search);
            pst.setString(1, "%\""+search+"\"%");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                animeList.add(new Anime(rs.getString("title"),rs.getString("author"),rs.getString("publisher"),rs.getInt("episodes"),rs.getInt("year"),rs.getString("plot"),rs.getString("imagePath"),rs.getString("link")));
            }
            send(animeList);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void insertAnime(Anime a) {
        String query = "INSERT INTO anime (title,author,publisher,plot,link,imagePath,episodes,year) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst;
        try {
            pst = DB.getConn().prepareStatement(query);
            pst.setString(1,a.getTitle());
            pst.setString(2,a.getAuthor());
            pst.setString(3,a.getPublisher());
            pst.setString(4,a.getPlot());
            pst.setString(5,a.getLink());
            pst.setString(6,a.getImagePath());
            pst.setInt(7,a.getEpisodes());
            pst.setInt(8,a.getYear());
            int response = pst.executeUpdate();
            send(response);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void selectAnime(String query) { // TODO CHECK correttezza runtime
        List<Anime> animeList = new ArrayList<>();
        try {
            ResultSet rs =  DB.getConn().createStatement().executeQuery(query);
            while(rs.next()){
                animeList.add(new Anime(rs.getString("title"),rs.getString("author"),rs.getString("publisher"),rs.getInt("episodes"),rs.getInt("year"),rs.getString("plot"),rs.getString("imagePath"),rs.getString("link")));
            }
            send(animeList);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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
            System.err.println(e.getMessage());
        }
        return received;
    }

}

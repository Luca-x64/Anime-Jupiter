package server;

import java.beans.Statement;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

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
    private boolean isAdmin;
    private boolean verified = false;
    private ArrayList<Object> exitMsg = null;
    private int user_id;

    public ServerThread(Socket s, Database db) throws IOException {
        this.DB = db;
        this.socket = s;
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (!verified) {
            account();
        }
        while (!socket.isClosed()) {
            functions();
        }
    }

    private void functions() {
        try {
            int receive = (Integer) receive();
            switch (receive) {
                case 1: {
                    selectAnime("SELECT * FROM anime");
                    break;
                }
                case 2: {
                    selectAnime("SELECT a.* FROM anime as a INNER JOIN favourite as f on a.id=f.anime_id where id=\""
                            + user_id + "\"");
                    break;
                }
                case 3: { // add anime (only admin)
                    Anime anime = (Anime) receive();
                    insertAnime(anime);
                    break;
                }
                case 4: { // search filter
                    String search = (String) receive();
                    searchAnime("SELECT * FROM anime WHERE title LIKE ?", search);
                    break;
                }
                case 5: { // delete anime (only admin)
                    Integer toDeleteId = (Integer) receive();
                    deleteAnime(toDeleteId);
                    break;
                }
                case 6: { // update anime (only admin)
                    Anime updated = (Anime) receive();
                    updateAnime(updated);
                    break;
                }
                case 7: { // logout
                    verified = false;
                    user = null;
                    while (!verified) {
                        account();
                    }
                    break;
                }
                case 8: { // setExitMessage
                    exitMsg = (ArrayList<Object>) receive();
                    break;
                }
                case 9: { // getExitMessage
                    send(exitMsg);
                    exitMsg = null;
                    break;
                }
                case 10: { // favourite
                    favourite();

                }
                default: {
                    // null
                }
            }
        } catch (NullPointerException ne) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void favourite() {
        boolean result = false;
        int anime_id = (Integer) receive();

        String selectQuery = "SELECT COUNT(*) FROM favourite WHERE user_id=? AND anime_id=?";
        String deleteQuery = "DELETE FROM favourite WHERE user_id=? AND anime_id=?";
        String insertQuery = "INSERT INTO favourite (user_id,anime_id) VALUES (?, ?)";

        try {

            // Verifica se esiste una riga con anime_id=1 e user_id=1
            PreparedStatement selectStatement = DB.getConn().prepareStatement(selectQuery);
            selectStatement.setInt(1, user_id);
            selectStatement.setInt(2, anime_id);

            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Se esiste una riga, elimina la riga
                PreparedStatement deleteStatement = DB.getConn().prepareStatement(deleteQuery);
                deleteStatement.setInt(1, user_id);
                deleteStatement.setInt(2, anime_id);
                deleteStatement.executeUpdate();
            } else {
                // Se non esiste una riga, inserisci una nuova riga
                PreparedStatement insertStatement = DB.getConn().prepareStatement(insertQuery);
                insertStatement.setInt(1, user_id);
                insertStatement.setInt(2, anime_id);
                insertStatement.executeUpdate();
            }

            result = true;
        } catch (SQLException e) {
            System.out.println(e);
            // TODO remove sout, exception ignored
        }

        send(result);

    }

    private void updateAnime(Anime updated) {
        if (isAdmin) {
            String query = "UPDATE anime SET title= ?, author= ?, publisher= ? , plot= ? , link= ? , imagePath= ? , episodes= "
                    + updated.getEpisodes() + " , year=" + updated.getYear() + " WHERE id= " + updated.getID();
            try {
                PreparedStatement pst = DB.getConn().prepareStatement(query);
                pst.setString(1, updated.getTitle());
                pst.setString(2, updated.getAuthor());
                pst.setString(3, updated.getPublisher());
                pst.setString(4, updated.getPlot());
                pst.setString(5, updated.getLink());
                pst.setString(6, updated.getImagePath());
                send(pst.executeUpdate() > 0);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            send(isAdmin);
        }
    }

    private void deleteAnime(Integer id) {
        if (isAdmin) {
            String query = "DELETE FROM anime WHERE id =" + id + "";
            try {
                send(DB.getConn().createStatement().executeUpdate(query) == 1);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            send(isAdmin);
        }
    }

    private void searchAnime(String query, String search) {
        List<Anime> animeList = new ArrayList<>();
        try {
            PreparedStatement pst = DB.getConn().prepareStatement(query);
            pst.setString(1, "%" + search + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Anime anime = new Anime(rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
                        rs.getInt("episodes"), rs.getInt("year"), rs.getString("plot"), rs.getString("imagePath"),
                        rs.getString("link"));
                anime.setID(rs.getInt("id"));
                animeList.add(anime);
            }
            send(animeList);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void insertAnime(Anime a) {
        if (isAdmin) {
            String query = "INSERT INTO anime (title,author,publisher,plot,link,imagePath,episodes,year) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement pst;
            try {
                pst = DB.getConn().prepareStatement(query);
                pst.setString(1, a.getTitle());
                pst.setString(2, a.getAuthor());
                pst.setString(3, a.getPublisher());
                pst.setString(4, a.getPlot());
                pst.setString(5, a.getLink());
                pst.setString(6, a.getImagePath());
                pst.setInt(7, a.getEpisodes());
                pst.setInt(8, a.getYear());
                Boolean response = pst.executeUpdate() == 1;
                send(response);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            send(isAdmin);
        }

    }

    private void selectAnime(String query) { // TODO CHECK correttezza runtime
        List<Anime> animeList = new ArrayList<>();
        try {
            ResultSet rs = DB.getConn().createStatement().executeQuery(query);
            while (rs.next()) {
                Anime anime = new Anime(rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
                        rs.getInt("episodes"), rs.getInt("year"), rs.getString("plot"), rs.getString("imagePath"),
                        rs.getString("link"));
                anime.setID(rs.getInt("id"));
                animeList.add(anime);
            }
            send(animeList);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void account() {
        user = (User) receive();
        if (user != null) {
            if (user.getUsername() == null) {
                login();
            } else {
                register();
            }
        } else {
            System.exit(6);
        }
    }

    private void login() {
        String queryLogin = "SELECT * FROM users WHERE email=?";
        ResultSet rs = null;
        try (PreparedStatement pst = DB.getConn().prepareStatement(queryLogin)) {
            pst.setString(1, user.getEmail());
            rs = pst.executeQuery();
            boolean checkEmail = rs.next(); // CHECK
            send(checkEmail);
            if (checkEmail) {
                String hashedPassword = rs.getString("password");
                verified = BCrypt.checkpw(user.getPassword(), hashedPassword);
                send(verified);
                if (verified) {
                    isAdmin = rs.getBoolean("isAdmin");
                    user_id = rs.getInt("id");
                    send(isAdmin);
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void register() {
        String queryRegister = "INSERT INTO users (nome,email,password) VALUES (?,?,?)";
        int response = 0;
        try (PreparedStatement pst = DB.getConn().prepareStatement(queryRegister)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            response = pst.executeUpdate();
            try {
                verified = response == 1;
                send(verified);
                if (verified) {
                    String queryGetId = "SELECT id FROM users WHERE email=" + user.getEmail();
                    ResultSet rs;
                    rs = DB.getConn().createStatement().executeQuery(queryGetId);
    
                    rs.next();
                    user_id = rs.getInt("id");
    
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (SQLException e) {
            send(false);
            send("Email already registered!");
        }

        

    }

    private synchronized void send(Object o) {
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private synchronized Object receive() {
        Object received = null;
        try {
            received = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println(e.getMessage());
        }
        return received;
    }

}

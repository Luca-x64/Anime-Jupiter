package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

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
                    selectAnime(user_id);
                    break;
                }
                case 2: {
                    selectFavourite(user_id);
                    break;
                }
                case 3: { // add anime (only admin)
                    Anime anime = (Anime) receive();
                    insertAnime(anime);
                    break;
                }
                case 4: { // search filter
                    String search = (String) receive();
                    searchAnime(search);
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

    private void selectFavourite(int user_id) {
        List<Anime> animeList = new ArrayList<>();
        try {
            ResultSet rs = DB.ExecutePreparedQuery("SELECT a.* FROM anime as a INNER JOIN favourite as f on a.id=f.anime_id where user_id = ?", user_id);

            while (rs.next()) {

                Anime anime = new Anime(rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
                        rs.getInt("episodes"),Year.of( rs.getInt("year")), rs.getString("plot"), rs.getString("imagePath"),
                        rs.getString("link"));
                anime.setID(rs.getInt("id"));
                anime.favourite();
                animeList.add(anime);
            }
            send(animeList);
        } catch (SQLException e) {
            System.err.println(e);
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
            ResultSet resultSet = DB.ExecutePreparedQuery(selectQuery, user_id, anime_id);
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Se esiste una riga, elimina la riga
                DB.executeQuery(deleteQuery, user_id, anime_id);
            } else {
                // Se non esiste una riga, inserisci una nuova riga
                DB.executeQuery(insertQuery, user_id, anime_id);
            }

            result = true;
        } catch (SQLException e) {
            //System.err.println(e);
        }

        send(result);

    }

    private void updateAnime(Anime updated) {
        if (isAdmin) {
            String query = "UPDATE anime SET title= ?, author= ?, publisher= ?, plot= ?, link= ?, imagePath= ?, episodes= ?, year= ? WHERE id= ?";
            int result = DB.executeQuery(query, updated.getTitle(), updated.getAuthor(), updated.getPublisher(), updated.getPlot(),
                    updated.getLink(), updated.getImagePath(), updated.getEpisodes(), updated.getYear().getValue(), updated.getID());

            send(result > 0);

        } else {
            send(isAdmin);
        }
    }

    private void deleteAnime(Integer id) {
        if (isAdmin) {
            String query = "DELETE FROM anime WHERE id =?";
            int result = DB.executeQuery(query, id);
            send(result == 1);
        } else {
            send(isAdmin);
        }
    }

    private void searchAnime(String search) {
        List<Anime> animeList = new ArrayList<>();
        try {
            ResultSet rs = DB.ExecutePreparedQuery("SELECT * FROM anime WHERE title LIKE ?", "%" + search + "%");
            while (rs.next()) {
                Anime anime = new Anime(rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
                        rs.getInt("episodes"), Year.of( rs.getInt("year")), rs.getString("plot"), rs.getString("imagePath"),
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
            int result = DB.executeQuery(query, a.getTitle(), a.getAuthor(), a.getPublisher(), a.getPlot(), a.getLink(),
                    a.getImagePath(), a.getEpisodes(), a.getYear().getValue());

            send(result == 1);

        } else {
            send(isAdmin);
        }

    }

    private void selectAnime(int user_id) {
        List<Anime> animeList = new ArrayList<>();
        try {
            ResultSet rs = DB.ExecutePreparedQuery("SELECT anime.*,  CASE WHEN EXISTS (SELECT * FROM favourite WHERE favourite.anime_id = anime.id AND favourite.user_id =? ) THEN 'true' ELSE 'false' END AS favourite FROM anime;", user_id);

            while (rs.next()) {
                Anime anime = new Anime(rs.getString("title"), rs.getString("author"), rs.getString("publisher"),
                        rs.getInt("episodes"), Year.of( rs.getInt("year")), rs.getString("plot"), rs.getString("imagePath"),
                        rs.getString("link"));
                anime.setID(rs.getInt("id"));
                System.out.println(rs.getString("favourite"));
                if (Boolean.parseBoolean(rs.getString("favourite"))) {
                    anime.favourite();
                    System.out.println(anime.getFavourite()); // TODO

                }

                animeList.add(anime);
            }
            send(animeList);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private void account() {
        user = (User) receive();
        if (user != null) {
            if (user.username() == null) {
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
        try {
            ResultSet rs = DB.ExecutePreparedQuery(queryLogin, user.email());

            boolean checkEmail = rs.next(); // CHECK
            send(checkEmail);
            if (checkEmail) {
                String hashedPassword = rs.getString("password");
                verified = BCrypt.checkpw(user.password(), hashedPassword);
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

        String hashpw = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        int response = 0;
        try {
            response = DB.executeQuery(queryRegister, user.username(), user.email(), hashpw);
        } catch (RuntimeException e) {

            send(false);
            send("Email already registered!");
        }


        try {
            verified = response == 1;
            send(verified);
            if (verified) {
                String queryGetId = "SELECT id FROM users WHERE email=?";
                ResultSet rs = DB.ExecutePreparedQuery(queryGetId, user.email());

                rs.next();
                user_id = rs.getInt("id");

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

package bp1;

import javax.xml.transform.Result;
import java.sql.*;

public class SQlite_java {
    private Connection conn = null;

    public void connect(){
        disconnect();
        try {
            Class.forName("org.sqlite.JDBC");
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:Banka_autoincrement.db");
                System.out.println("Successfully connected to database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void disconnect(){
        if(conn!=null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void printAllUsers(){
        String sql = "select Idkom, Naziv, Adresa from Komitent";
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ALL users:");
            while(rs.next()){
                System.out.println(rs.getInt(1) + "\t"
                        + rs.getString(2) + "\t"
                        + rs.getString("Adresa"));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void printAllUsersWithName (String name){
        String sql = "select Idkom, Naziv, Adresa from Komitent where Naziv = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery();) {
                System.out.println("ALL users with name:" + name);
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "\t"
                            + rs.getString(2) + "\t"
                            + rs.getString("Adresa"));
                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUserAddress(int idKom, String address){
        String sql = "update Komitent set Adresa = ? where Idkom = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(2, idKom);
            ps.setString(1, address);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int insertUser (String name, String address){
        String sql = "insert into Komitent (Naziv,Adresa) VALUES(?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql);){
           ps.setString(1, name);
           ps.setString(2, address);
           if(ps.executeUpdate() == 1){
               try(ResultSet keysRs = ps.getGeneratedKeys();){
                    if(keysRs.next()){
                        int id = keysRs.getInt(1);
                        System.out.println("User with id " + id + " has been inserted");
                        return id;
                    }
               } catch (SQLException e) {
                   e.printStackTrace();
                   return -1;
               }
           }
           else return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
    public void deleteUser(int idKom){
        String sql = "delete from Komitent where idKom = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, idKom);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


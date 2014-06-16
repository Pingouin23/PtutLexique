package lexique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



/**
 *
 * @author Théo
 */
public class Notation {
    String userid;
    String password;
    String URL;
    java.sql.Connection conn;

    Notation() throws SQLException {
        userid = "p1105653";
        password = "154996";
        URL = "jdbc:oracle:thin:@iuta.univ-lyon1.fr:1521:orcl";
        conn = java.sql.DriverManager.getConnection(URL, userid, password);
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        if (conn != null) {
            System.out.println("Connexion établie");
        } else {
            System.out.println("Connexion échouée");
        }
    }
    
    void donneNote(ResultSet r) throws SQLException{
        Statement lanceRequete1;
        lanceRequete1 = conn.createStatement();
        ResultSet requeteSet;
        int note;
        Scanner s;
        s = new Scanner(System.in);
        while (r.next()){
            System.out.print("Adverbe : " + r.getString("mot") + " ");
            System.out.print("Donner un note : ");
            note = s.nextInt();
            if(note == 0 || note == 1){
                requeteSet = lanceRequete1.executeQuery("insert into lexiqueLemme values('"
                        + r.getInt("id_mot") 
                        + "','" + r.getString("mot")
                        + "','" + r.getString("lemme")
                        + "','" + r.getString("cgram")
                        + "','" + r.getInt("classe")
                        + "','" + r.getString("genre")
                        + "','" + r.getString("nombre")
                        + "','" + r.getInt("freqlemfilm")
                        + "','" + r.getInt("freqlemlivre")
                        + "','" + r.getInt("freqfilm")
                        + "','" + r.getInt("freqlivre")
                        + "','" + note
                        + "')");
                requeteSet.close();
            }
            else{
                if (note == 2) requeteSet = lanceRequete1.executeQuery("insert into lexiqueLemme values('"
                        + r.getInt("id_mot") 
                        + "','" + r.getString("mot")
                        + "','" + r.getString("lemme")
                        + "','" + r.getString("cgram")
                        + "','" + r.getInt("classe")
                        + "','" + r.getString("genre")
                        + "','" + r.getString("nombre")
                        + "','" + r.getInt("freqlemfilm")
                        + "','" + r.getInt("freqlemlivre")
                        + "','" + r.getInt("freqfilm")
                        + "','" + r.getInt("freqlivre")
                        + "',' -1"
                        + "')");
                else requeteSet = lanceRequete1.executeQuery("insert into lexiqueLemme values('"
                        + r.getInt("id_mot") 
                        + "','" + r.getString("mot")
                        + "','" + r.getString("lemme")
                        + "','" + r.getString("cgram")
                        + "','" + r.getInt("classe")
                        + "','" + r.getString("genre")
                        + "','" + r.getString("nombre")
                        + "','" + r.getInt("freqlemfilm")
                        + "','" + r.getInt("freqlemlivre")
                        + "','" + r.getInt("freqfilm")
                        + "','" + r.getInt("freqlivre")
                        + "',' 2"
                        + "')");
                requeteSet.close();
            }
        } 
        System.out.print("\n");
    }
    
    public static void main(String[] args) throws SQLException {
        Notation note;
        note = new Notation();
        Statement lanceRequete1;
        lanceRequete1 = note.conn.createStatement();
        ResultSet requete1;
        requete1 = lanceRequete1.executeQuery("select * from Lexique where CGRAM = 'ADV'"
                + "and mot not in (Select mot from lexiqueLemme)");
        while(requete1.next()){
            note.donneNote(requete1);
        }
        lanceRequete1.close();
        requete1.close();
        
        //System.out.println(requete1.getInt("opinion"));
    }
}

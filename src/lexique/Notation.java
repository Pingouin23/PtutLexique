/*Permet de noter des mots du lexique*/
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
        userid = "admin"; //votre id
        password = "admin"; //votre mdp
        URL = "jdbc:oracle:thin:@iuta.univ-lyon1.fr:1521:orcl"; //votre serveur
        conn = java.sql.DriverManager.getConnection(URL, userid, password);
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
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
            //Lorsque la fonction est lancée, taper 0 pour noter l'opinion à 0
            //taper 1 pour noter à 1
            //taper 2 pour noter à -1
            note = s.nextInt();
            if(note == 0 || note == 1){
                //mise à jour du lexique noté
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
                //mise à jour du lexique noté
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
                //mise à jour du lexique noté
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
    }
    
    public static void main(String[] args) throws SQLException {
        Notation note;
        note = new Notation();
        Statement lanceRequete1;
        lanceRequete1 = note.conn.createStatement();
        ResultSet requete1;
        //choix des mots à ajouter au lexique en partant du lexique complet non noté
        requete1 = lanceRequete1.executeQuery("select * from Lexique where CGRAM = 'ADV'"
                + "and mot not in (Select mot from lexiqueLemme)");
        while(requete1.next()){
            note.donneNote(requete1);
        }
        lanceRequete1.close();
        requete1.close();
    }
}

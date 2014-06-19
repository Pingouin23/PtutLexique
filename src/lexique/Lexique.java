/*Classe principale*/
package lexique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Lexique {

    Connection connG;
    Phrase phrase;

    Lexique() throws SQLException {
        connG = new Connection();
        phrase = new Phrase("");
    }
    
     /*Extrait tous les adjectifs d'une Phrase à l'aide d'une requête SQL*/
    ArrayList<String> extraitAdjAdv(Phrase comm) throws SQLException {
        Statement lanceRequete;
        lanceRequete = connG.conn.createStatement();
        ResultSet requete;
        ArrayList<String> liste;
        liste = new ArrayList<>();
        comm = comm.lemmatise();
        System.out.println(comm);
        for (String s : comm.mots) {
            requete = lanceRequete.executeQuery("Select * from basenotee where lemme = '"
                    + s + "'");
            if (requete.next()) {
                liste.add(s);
            }
            requete.close();
        }
        lanceRequete.close();
        return liste;
    }

    /*Somme la note de chacun des mots extraits dans la fonction précédente*/
    public void notePhrase(String s, int id_phrase) throws SQLException {
        Phrase comm;
        comm = new Phrase(s);
        Statement lanceRequete;
        lanceRequete = connG.conn.createStatement();
        ResultSet requete;
        ArrayList<String> liste;
        liste = new ArrayList<>();
        liste = extraitAdjAdv(comm);//récupère la liste de mots du lexique
        int note = 0;
        for (int i = 0; i < liste.size(); i++) {
            //requete pour récupérer les opinions
            requete = lanceRequete.executeQuery("Select opinion from basenotee where lemme = '" + liste.get(i) + "'");
            if (requete.next()) {
                //si note == 0 pas de calcul
                if (requete.getInt("opinion") != 0) {
                    if (requete.getInt("opinion") == -1) {
                        //inversion de l'opinion si Negation présente
                        if (comm.detectNegation()) {
                            note++;
                        } else {
                            note--;
                        }
                    } else {
                        //inversion de l'opinion si Negation présente
                        if (requete.getInt("opinion") == 1) {
                            if (comm.detectNegation()) {
                                note--;
                            } else {
                                note++;
                            }
                        }
                    }
                }
            }
            requete.close();
        }
        Statement updateR;
        updateR = connG.conn.createStatement();
        //mise à jour de la table
        updateR.executeUpdate("update phrase_demo set note = '" + note + "' where id_phrase = '"
                + id_phrase + "'");
        updateR.close();
        lanceRequete.close();
    }
    /*Main*/
    public static void main(String[] args) throws SQLException {
        Lexique l;
        l = new Lexique();
        Phrase lower;
        Statement lanceRequete;
        lanceRequete = l.connG.conn.createStatement();
        ResultSet requete;
        //choix de la table dans la base
        requete = lanceRequete.executeQuery("select * from phrase_demo");
        while (requete.next()) {
            lower = new Phrase(requete.getString("phrase"));
            lower.phrase = lower.phrase.toLowerCase();
            l.notePhrase(lower.phrase, requete.getInt("id_phrase"));
        }
        requete.close();
        lanceRequete.close();
    }
}

/*Permet de récupérer les tableaux de Fscore*/
package lexique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author yougo
 */
public class CompareAlgo {

    Connection connexion;

    public CompareAlgo() throws SQLException {
        connexion = new Connection();

    }

    public void compareAlgoAvecHumain() throws SQLException {
        float noteRefPositif = 0;
        float noteATestPositifPositif = 0;
        float noteATestNeutrePositif = 0;
        float noteATestNegatifPositif = 0;
        float noteRefNeutre = 0;
        float noteATestPositifNeutre = 0;
        float noteATestNeutreNeutre = 0;
        float noteATestNegatifNeutre = 0;
        float noteRefNegatif = 0;
        float noteATestPositifNegatif = 0;
        float noteATestNeutreNegatif = 0;
        float noteATestNegatifNegatif = 0;
        Statement lanceRequete;
        lanceRequete = connexion.conn.createStatement();
        ResultSet requete;
        //choix de la table à tester
        requete = lanceRequete.executeQuery("Select * from phrase_demo");
        while (requete.next()) {
            int note = requete.getInt("note");
            int classe = requete.getInt("classe");
            switch (classe) {
                case 1:
                    noteRefPositif++;
                    if (note > 0) {
                        noteATestPositifPositif++;
                    } else {
                        if (note == 0) {
                            noteATestNeutrePositif++;
                        } else {
                            noteATestNegatifPositif++;
                        }
                    }
                    break;
                case 0:
                    noteRefNeutre++;
                    if (classe == note) {
                        noteATestNeutreNeutre++;
                    } else {
                        if (note > 0) {
                            noteATestPositifNeutre++;
                        } else {
                            noteATestNegatifNeutre++;
                        }
                    }
                    break;
                case -1:
                    noteRefNegatif++;
                    if (note < 0) {
                        noteATestNegatifNegatif++;
                    } else {
                        if (note > 0) {
                            noteATestPositifNegatif++;
                        } else {
                            noteATestNeutreNegatif++;
                        }
                    }
                    break;
                default:
                    System.out.println("Problème ! ! ");
                    break;
            }
        }

        System.out.println("\n\n\n\t\t\t\t\tResultat de l'algo");
        System.out.println("\t\t\t Positif\t|\tNeutre\t|\tNegatif\n");
        System.out.format("Fscore Positif  :\t %7.2f \t|%7.2f\t|%7.2f \n", (noteATestPositifPositif / noteRefPositif), (noteATestNeutrePositif / noteRefPositif), (noteATestNegatifPositif / noteRefPositif));
        System.out.println("-------------------------------------------------------------------------------\n");
        System.out.format("Fscore Neutre : \t %7.2f \t|%7.2f\t|%7.2f \n", (noteATestPositifNeutre / noteRefNeutre), (noteATestNeutreNeutre / noteRefNeutre), (noteATestNegatifNeutre / noteRefNeutre));
        System.out.println("-------------------------------------------------------------------------------\n");
        System.out.format("Fscore Negatif : \t %7.2f \t|%7.2f\t|%7.2f \n", (noteATestPositifNegatif / noteRefNegatif), (noteATestNeutreNegatif / noteRefNegatif), (noteATestNegatifNegatif / noteRefNegatif));
        System.out.println("-------------------------------------------------------------------------------\n");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        CompareAlgo nettoyage;
        nettoyage = new CompareAlgo();
        nettoyage.compareAlgoAvecHumain();
    }

}

package lexique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern; // contient Pattern

public class Phrase {

    public String phrase; /* le commentaire dans son ensemble*/
    String[] elements; /* On ne peut pas faire un split sur un AL, ensemble de mots*/
    ArrayList<String> mots;

    Phrase(String s) {
        phrase = s;
        elements = phrase.split("[ .\",\'=!/():;_?]");
        mots = new ArrayList((Arrays.asList(elements)));
        phrase = join(mots);
    }

    @Override
    public String toString() {
        return phrase;
    }

    boolean detectNegation() {
        for (int i = 0; i < elements.length; i++) {
            if (Pattern.matches("ne|pas|jamais|rien|aucun|aucune|n|impossible|moins", elements[i])) { // expression regulière
                System.out.println("pouet");   
                return true;
            }
            else {
                if (i < elements.length - 2) {
                    if (Pattern.matches("loin", elements[i])
                            && Pattern.matches("d", elements[i + 1])
                            && Pattern.matches("être", elements[i + 2])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* Fais le contraire de split, met le contenu d'un AL dans un même string */
    String join(ArrayList<String> tab) {
        String joinstring = "";
        for (String s : tab) {
            joinstring += s + " ";
        }
        return joinstring;
    }

    /* change les mots du commentaire par le lemme correspondant le plus frequent */
    Phrase lemmatise() throws SQLException {
        Connection l = new Connection();
        Statement lanceRequete;
        lanceRequete = l.conn.createStatement();
        ResultSet liste_pneus;
        ResultSet lexique;
        Phrase finalp = new Phrase("");
        String test;
        test = "";
        for (String mot : mots) {
            mot = mot.toLowerCase();
            try {
                if (test.equals("")) {
                    liste_pneus = lanceRequete.executeQuery("Select * from PNEUS where lower(DESIGNATION) = '" + mot + "'");
                    if (liste_pneus.next()) {
                        test = mot;
                        finalp.mots.add(mot);
                    } else {
                        lexique = lanceRequete.executeQuery("Select * from basemot where lower(MOT) = '" + mot
                                + "' and (CGRAM = 'NOM' "
                                + "or CGRAM = 'VER' "
                                + "or CGRAM = 'ADV' or CGRAM = 'ADJ')"
                                + "order by cgram asc,"
                                + "FREQLEMFILM desc");
                        if (lexique.next()) {
                            //System.out.println("lemme " + lexique.getString("LEMME"));
                            finalp.mots.add(lexique.getString("LEMME"));
                        }
                        lexique.close();
                    }
                } else {
                    liste_pneus = lanceRequete.executeQuery("Select * from PNEUS where lower(DESIGNATION)"
                            + " = '" + test +" "+ mot + "'");
                    if (liste_pneus.next()) {
                        test = mot;
                        finalp.mots.add(mot);
                    } else {
                        test = "";
                        lexique = lanceRequete.executeQuery("Select * from basemot where lower(MOT) = '" + mot
                                + "' and (CGRAM = 'NOM' "
                                + "or CGRAM = 'VER' "
                                + "or CGRAM = 'ADV' or CGRAM = 'ADJ')"
                                + "order by  cgram asc,"
                                + "FREQLEMFILM desc");
                        if (lexique.next()) {
                            //System.out.println("lemme " + lexique.getString("LEMME"));
                            finalp.mots.add(lexique.getString("LEMME"));
                        }
                        lexique.close();
                    }
                }
                liste_pneus.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        lanceRequete.close();
        finalp.phrase = finalp.join(finalp.mots);
        return finalp; // renvoie la phrase lémmatisée
    }

    public static void main(String[] args) throws SQLException {
        Phrase s;
        s = new Phrase("Pas cher cela dit");
        s.detectNegation();
        s = s.lemmatise();
        System.out.println(s);
    }
}

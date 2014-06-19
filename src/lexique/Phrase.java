/*Classe Phrase permettant d'ensuite diviser en mots*/
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
        elements = phrase.split("[ .\",\'=!/():;_?]"); //split ponctuation
        mots = new ArrayList((Arrays.asList(elements))); //création de la liste de mots
        phrase = join(mots);
    }

    @Override
    public String toString() {
        return phrase;
    }

    boolean detectNegation() {
        for (int i = 0; i < elements.length; i++) {
            //termes permettant de valider une négation
            if (Pattern.matches("ne|pas|jamais|rien|aucun|aucune|n|impossible|moins|peu", elements[i])) { 
                // expression regulière   
                return true;
            }
            else {
                //particularité pour "loin d'être"
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
                    //ajout des noms de produits
                    liste_pneus = lanceRequete.executeQuery("Select * from PNEUS where lower(DESIGNATION) = '" + mot + "'");
                    if (liste_pneus.next()) {
                        test = mot;
                        finalp.mots.add(mot);
                    } else {
                        //ajout des mots du lexique correspondant aux classes grammaticales voulues
                        lexique = lanceRequete.executeQuery("Select * from basemot where lower(MOT) = '" + mot
                                + "' and (CGRAM = 'NOM' "
                                + "or CGRAM = 'VER' "
                                + "or CGRAM = 'ADV' or CGRAM = 'ADJ')"
                                + "order by cgram asc,"
                                + "FREQLEMFILM desc");
                        if (lexique.next()) {
                            finalp.mots.add(lexique.getString("LEMME"));
                        }
                        lexique.close();
                    }
                } else {
                    /*Dans ce else se trouve un début de travail sur un bigramme
                    Cela s'effectue sur la désignation d'un produit lorsqu'il contient plusieurs termes différents
                    ex : "Michelin X85 hiver"*/
                    //ajout des noms de produits
                    liste_pneus = lanceRequete.executeQuery("Select * from PNEUS where lower(DESIGNATION)"
                            + " = '" + test +" "+ mot + "'");
                    if (liste_pneus.next()) {
                        test = mot;
                        finalp.mots.add(mot);
                    } else {
                        test = "";
                        //ajout des mots du lexique correspondant aux classes grammaticales voulues
                        lexique = lanceRequete.executeQuery("Select * from basemot where lower(MOT) = '" + mot
                                + "' and (CGRAM = 'NOM' "
                                + "or CGRAM = 'VER' "
                                + "or CGRAM = 'ADV' or CGRAM = 'ADJ')"
                                + "order by  cgram asc,"
                                + "FREQLEMFILM desc");
                        if (lexique.next()) {
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
        //test
        s = new Phrase("Pas cher cela dit");
        s.detectNegation();
        s = s.lemmatise();
        System.out.println(s);
    }
}

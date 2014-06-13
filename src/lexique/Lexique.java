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
    /*
     /*Permet de savoir si un mot est un produit
     boolean estUnProduit(String s) throws SQLException{
     boolean bool = false;
     PreparedStatement lanceRequete;
     String select = "select * from PNEUS where lower(DESIGNATION) = ?";
     lanceRequete = connG.conn.prepareStatement(select);
        
     ResultSet requete;
     lanceRequete.setString(1,s);
     requete = lanceRequete.executeQuery();
     //System.out.println(s);
     if (requete.next()) {
     bool = true;
     }
     requete.close();
     lanceRequete.close();
     return bool;
     }
  
     /*Permet de vérifier que deux mots sont proches dans une phrase
     // !!!!!! A MODIFIER !!!!!!!
     boolean estProche(String mot1, String mot2, Phrase phrase){
     boolean bool = false;
     int i = 0;
     int j = 0;
     for (String s : phrase.mots){
     if (s.equalsIgnoreCase(mot1)) i = 1;
     if (s.equalsIgnoreCase(mot2)) j = 1;
     i++;
     j++;
     }
     if ((j - i <= 3 && j - i >= -3) || (i - j <= 3 && i - j >= -3)) bool = true;
     return bool;
     }
    
     /*Renvoi l'adjectif proche du string mot dans la Phrase phrase
     String adjectifProche(Phrase phrase, String mot) throws SQLException{
     Statement lanceRequete;
     lanceRequete = connG.conn.createStatement();
     ResultSet requete;
     String adj;
     adj = "";
     for (String s : phrase.mots){
            
     // System.out.println(s);
     requete = lanceRequete.executeQuery("Select * from lexiqueLemme "
     + "where lower(mot) = '" + s + "'");
           
     if(requete.next()){
     if (estProche(s,mot,phrase)){
     adj = s;
     return adj;
     }
     }
     }
     return adj;
     }
    
     int cptAdjectifProche(Phrase phrase, String mot) throws SQLException{
     Statement lanceRequete;
     lanceRequete = connG.conn.createStatement();
     ResultSet requete;
     int cpt;
     cpt = 0;
     for (String s : phrase.mots){
     if(s.equals("")) cpt = cpt + 1;
     else cpt = cpt + s.length() + 1;
     // System.out.println(s);
     requete = lanceRequete.executeQuery("Select * from lexiqueLemme "
     + "where lower(mot) = '" + s + "'");
           
     if(requete.next()){
     if (estProche(s,mot,phrase)){
     return cpt;
     }
     }
     }
     cpt = 0;
     return cpt;
     }
    
     /*Permet de savoir si un commentaire contient un nom de produit
     ArrayList<String> extraitProduit(Phrase comm) throws SQLException{
     ArrayList<String> liste;
     liste = new ArrayList<>();
     //System.out.println(comm.mots);
     for (String m : comm.mots) {
     //System.out.println(m);
     if (estUnProduit(m) == true) {
     if(!liste.contains(m)){
     liste.add(m);
     }
     }
     }
     return liste;
     }
    
     /*Lie un produit à un adjectif 
     ArrayList<Nom> associeNomAdj(Phrase comm) throws SQLException {
     ArrayList<Nom> liste;
     liste = new ArrayList<>();
     ArrayList<String> listenom;
     listenom = new ArrayList<>();
     listenom = extraitNom(comm);
     int cpt;
     for (int i = 0; i < listenom.size()-1; i++) {
     Nom p;
     p = new Nom();
     p.setNom(listenom.get(i));
     liste.add(p);
     }
     for (Nom pr : liste){
     //System.out.println("testing " + comm.phrase);
     cpt = cptAdjectifProche(comm,pr.getNom());
     pr.setAdjectif(adjectifProche(comm,pr.getNom()));
     comm = new Phrase(comm.phrase.substring(cpt));
            
     }
     return liste;
     }
    
     /*Extrait les noms présents dans un commentaire 
     //ERREUR A VERIFIER ICI !!!!
     public ArrayList<String> extraitNom(Phrase comm) throws SQLException {
     ArrayList<String> liste;
     liste = new ArrayList<>();
     Statement lanceRequete;
     lanceRequete = connG.conn.createStatement();
     ResultSet requete;
     for (String s : comm.mots) {
     requete = lanceRequete.executeQuery("Select * from lexique where lower(lemme) ='"
     + s + "'");
     if (requete.next()) {
     if (requete.getString("CGRAM").equalsIgnoreCase("NOM")) {
     liste.add(s);
     }
     }
     requete.close();
     }
     lanceRequete.close();
     return liste;
     }
    
     /*Extrait tous les adjectifs d'une Phrase*/

    ArrayList<String> extraitAdj(Phrase comm) throws SQLException {
        Statement lanceRequete;
        lanceRequete = connG.conn.createStatement();
        ResultSet requete;
        ArrayList<String> liste;
        liste = new ArrayList<>();
        for (String s : comm.mots) {
            requete = lanceRequete.executeQuery("Select * from lexiqueLemme where mot = '"
                + s + "'");
            if (requete.next()) {
                    liste.add(s);
            }
        }
        return liste;
    }

    public void notePhrase(String s, int id_phrase) throws SQLException {
        Phrase comm;
        comm = new Phrase(s);
        Statement lanceRequete;
        lanceRequete = connG.conn.createStatement();
        ResultSet requete;
        ArrayList<String> liste;
        liste = new ArrayList<>();
        liste = extraitAdj(comm);
        int note = 0;
        for (int i = 0; i < liste.size(); i++) {
            requete = lanceRequete.executeQuery("Select opinion from lexiqueLemme where lemme = '" + liste.get(i) + "'");
            if (requete.next()) {
                if (requete.getInt("opinion") == -1) {
                    if (comm.detectNegation()) {
                        note++;
                    } else {
                        note--;
                    }
                } else {
                    if (requete.getInt("opinion") == 1) {
                        if (comm.detectNegation()) {
                            note--;
                        } else {
                            note++;
                        }
                    }
                }

            }
            requete.close();
        }
        Statement updateR;
        updateR = connG.conn.createStatement();
        updateR.executeUpdate("update phrase set note = '" + note + "' where id_phrase = '"
            + id_phrase + "'");
        updateR.close();
        lanceRequete.close();
    }
    /*
     //Associe un produit aux adjectifs et noms relatifs dans la phrase
     public ArrayList<Produit> associeProduit(Phrase comm) throws SQLException {
     //Initialisation de la liste retournée
     ArrayList<Produit> listefinale;
     listefinale = new ArrayList<>();
     //Liste des produits :
     ArrayList<String> listeproduit;
     listeproduit = new ArrayList<>();
     listeproduit = extraitProduit(comm); 
     if (listeproduit.isEmpty()) {
     System.out.println("Aucun produit dans la phrase");
     } else {
     //Liste des noms associés :
     ArrayList<Nom> listenom;
     listenom = new ArrayList<>();
     listenom = associeNomAdj(comm);
     Nom[] n;
     n = new Nom[listenom.size()];
     noteNom(listenom, comm);
     for (int i = 0; i < listenom.size(); i++) {
     n[i] = listenom.get(i);
     System.out.println("Nom : " + n[i]);
     }
     for (int i = 0; i < listeproduit.size(); i++) {
     Produit p;
     p = new Produit(n, listeproduit.get(i));
     p.listenom = n;
     listefinale.add(p);
     System.out.println(p);
     }

     }
     return listefinale;
     }
     */

    public static void main(String[] args) throws SQLException {
        Lexique l;
        l = new Lexique();
        Statement lanceRequete;
        lanceRequete = l.connG.conn.createStatement();
        ResultSet requete;
        requete = lanceRequete.executeQuery("select * from phrase");
        for (int i = 0; i < 50; i++) {
            if(requete.next()){
                l.notePhrase(requete.getString("phrase"),requete.getInt("id_phrase"));
            }
        }

        /*
         while(requete.next()){
         Phrase commentaire;
         commentaire = new Phrase(requete.getString("poste"));
         listefinale = l.associeProduit(commentaire);
         }*/
        requete.close();
        lanceRequete.close();
    }
}

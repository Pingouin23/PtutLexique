/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexique;

/**
 *
 * @author p1105653
 */
public class Produit {
    Nom[] listenom;
    String produit;
    int opinionfinale;
    
    Produit(){
        listenom = null;
        produit = "";
        opinionfinale = 0;
    }
    
    Produit(Nom[] n, String p){
        listenom = n;
        produit = p;
        opinionfinale = 0;
        for (int i = 0; i < listenom.length; i++){
            opinionfinale = opinionfinale + n[i].opinion;
        }
    }
    
    @Override
    public String toString(){
        String s;
        s = "Produit : " + produit + "\n";
        for (int i = 0; i < listenom.length; i++){
            if (!listenom[i].adjectif.equals("")){
                s = s + listenom[i].toString() + "\n";
            }
        }
        return s;
    }
}

package lexique;


public class Nom {
    
    String nom;
    String adjectif;
    int opinion;
    
    Nom(){
        nom = "";
        adjectif = "";
        opinion = 0;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdjectif(String adjectif) {
        this.adjectif = adjectif;
    }

    public String getNom() {
        return nom;
    }

    public String getAdjectif() {
        return adjectif;
    }
    
    @Override
    public String toString(){
        String s;
        s = "\t-- Nom : " + nom;
        s = s + "\n\t-- Adjectif associé : " + adjectif;
        s = s + "\n\t-- Note : " + opinion;
        return s;  
    }
    
}

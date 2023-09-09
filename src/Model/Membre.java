package Model;


import java.util.List;

public class Membre {
    private int Numero_membre;
    private String nom;

    public int getNumero_membre() {
        return Numero_membre;
    }

    public void setNumero_membre(int numero_membre) {
        Numero_membre = numero_membre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public Membre(int Numero_membre,String nom){
        this.Numero_membre=Numero_membre;
        this.nom=nom;
    }
    public List<Livre> getLivresEmpruntes() {
        return null;
    }
}


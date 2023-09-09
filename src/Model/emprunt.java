package Model;

import java.util.Date;

public class emprunt {
    private Livre livre;
    private Membre member;
    private Date Date_emprunt;
    private Date Date_retour;

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMember() {
        return member;
    }

    public void setMember(Membre member) {
        this.member = member;
    }

    public Date getDate_emprunt() {
        return Date_emprunt;
    }

    public void setDate_emprunt(Date date_emprunt) {
        Date_emprunt = date_emprunt;
    }

    public Date getDate_retour() {
        return Date_retour;
    }

    public void setDate_retour(Date date_retour) {
        Date_retour = date_retour;
    }
    public emprunt(Livre livre, Membre membre, Date Date_emprunt, Date Date_retour) {
        this.livre = livre;
        this.member = membre;
        this.Date_emprunt = Date_emprunt;
        this.Date_retour = Date_retour;
    }

}




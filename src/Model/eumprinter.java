package Model;

import java.util.Date;

public class eumprinter {
    private int ISBN;
    private int Member;
    private Date Date_emprunt;
    private Date Date_retour;

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public int getMember() {
        return Member;
    }

    public void setMember(int member) {
        Member = member;
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
    public eumprinter(int ISBN, int Membre, Date Date_emprunt, Date Date_retour) {
        this.ISBN = ISBN;
        this.Member = Membre;
        this.Date_emprunt = Date_emprunt;
        this.Date_retour = Date_retour;
    }

}




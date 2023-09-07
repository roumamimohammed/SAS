package DAO;

import Model.Membre;
import Model.eumprinter;
import Model.Livre;
import Model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EumprinterDAO {
    private Connection connection;
    private List<eumprinter> eumprinterList;

    public EumprinterDAO(Connection connection) {
        this.connection = connection;
        eumprinterList = new ArrayList<>();
    }

    public void addEumprinter(eumprinter eumprinter) {
        eumprinterList.add(eumprinter);
    }

    public void displayAllEumprinters() {
        for (eumprinter eumprinter : eumprinterList) {
            System.out.println("ISBN: " + eumprinter.getISBN());
            System.out.println("Member: " + eumprinter.getMember());
            System.out.println("Date_emprunt: " + eumprinter.getDate_emprunt());
            System.out.println("Date_retour: " + eumprinter.getDate_retour());
            System.out.println();
        }
    }

    public void eumprintLivre(String ISBN, int memberNumero, Date date_emprunt, Date date_retour) {
        MembreDAO memberDAO = new MembreDAO(connection);
        Membre member = memberDAO.getMemberByNumero(memberNumero);

        if (member == null) {
            System.out.println("Member with Numero_membre " + memberNumero + " does not exist.");
            return;
        }

        LivreDAO livreDAO = new LivreDAO(connection);
        Livre livre = livreDAO.getLivreByISBN(ISBN);

        if (livre == null || livre.getStatus() != Status.disponible) {
            System.out.println("Book with ISBN " + ISBN + " is not available for eumprinte.");
            return;
        }

        eumprinter eumprinter = new eumprinter(ISBN, memberNumero, date_emprunt, date_retour);
        addEumprinter(eumprinter);

        insertEumprinterRecord(eumprinter);

        System.out.println("Book with ISBN " + ISBN + " has been eumprinted to member " + memberNumero);
    }

    public void returnLivre(String ISBN) {
        eumprinter returnedEumprinter = null;
        for (eumprinter eumprinter : eumprinterList) {
            if (eumprinter.getISBN().equals(ISBN)) {
                returnedEumprinter = eumprinter;
                break;
            }
        }

        if (returnedEumprinter == null) {
            System.out.println("No eumprinter record found for ISBN " + ISBN + ".");
            return;
        }

        eumprinterList.remove(returnedEumprinter);

        System.out.println("Book with ISBN " + ISBN + " has been returned and is now available.");
    }

    private void insertEumprinterRecord(eumprinter eumprinter) {
        try {
            String insertQuery = "INSERT INTO eumprinter (ISBN, Numero_membre, Date_emprunt, Date_retour) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, eumprinter.getISBN());
            preparedStatement.setInt(2, eumprinter.getMember());
            preparedStatement.setTimestamp(3, new Timestamp(eumprinter.getDate_emprunt().getTime()));
            preparedStatement.setTimestamp(4, new Timestamp(eumprinter.getDate_retour().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Eumprinter record added successfully.");
            } else {
                System.out.println("Failed to add eumprinter record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

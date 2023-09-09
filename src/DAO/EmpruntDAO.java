package DAO;

import Exceptions.DAOException;
import Model.Membre;
import Model.emprunt;
import Model.Livre;
import Model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmpruntDAO {
    private final Connection connection;
    private final List<emprunt> empruntList;

    public EmpruntDAO(Connection connection) {
        this.connection = connection;
        empruntList = new ArrayList<>();
    }

    public void addemprunt(emprunt emprunt) {
        empruntList.add(emprunt);
    }
    
    public void empruntLivre(String ISBN, int memberNumero, Date date_emprunt, Date date_retour) throws DAOException {
        MembreDAO memberDAO = new MembreDAO(connection);
        Membre member = memberDAO.getMemberByNumero(memberNumero);

        if (member == null) {
            System.out.println("Member with Numero_membre " + memberNumero + " does not exist.");
            return;
        }

        LivreDAO livreDAO = new LivreDAO(connection);
        Livre livre = livreDAO.getLivreByISBN(ISBN);

        if (livre == null || livre.getStatus() != Status.disponible) {
            System.out.println("Book with ISBN " + ISBN + " is not available for emprunte.");
            return;
        }

        emprunt emprunt = new emprunt(livre, member, date_emprunt, date_retour);
        addemprunt(emprunt);

        insertempruntRecord(emprunt);

        System.out.println("Book with ISBN " + ISBN + " has been emprunted to member " + memberNumero);
    }

    public void returnLivre(String ISBN) {
        emprunt returnedEmprunt = null;
        for (emprunt emprunt : empruntList) {
            if (emprunt.getLivre().getIsbn().equals(ISBN)) {
                returnedEmprunt = emprunt;
                break;
            }
        }

        if (returnedEmprunt == null) {
            System.out.println("No emprunt record found for ISBN " + ISBN + ".");
            return;
        }

        empruntList.remove(returnedEmprunt);

        System.out.println("Book with ISBN " + ISBN + " has been returned and is now available.");
    }

    private void insertempruntRecord(emprunt emprunt) throws DAOException {
        try {
            String insertQuery = "INSERT INTO emprunt (ISBN, Numero_membre, Date_emprunt, Date_retour) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, emprunt.getLivre().getIsbn());
            preparedStatement.setInt(2, emprunt.getMember().getNumero_membre());
            preparedStatement.setTimestamp(3, new Timestamp(emprunt.getDate_emprunt().getTime()));
            preparedStatement.setTimestamp(4, new Timestamp(emprunt.getDate_retour().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("emprunt record added successfully.");
            } else {
                System.out.println("Failed to add emprunt record.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error while inserting emprunt record.", e);
        }
    }
}

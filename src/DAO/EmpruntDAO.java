package DAO;

import Exceptions.DAOException;
import Model.Membre;
import Model.emprunt;
import Model.Livre;
import Model.Status;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmpruntDAO {
    private final Connection connection;
    private  List<emprunt> empruntList  = new ArrayList<>();

    public EmpruntDAO(Connection connection) {
        this.connection = connection;

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

        insertempruntRecord(emprunt);

        System.out.println("Book with ISBN " + ISBN + " has been emprunted to member " + memberNumero);
    }

    public void returnLivre(String ISBN) throws DAOException {
        emprunt returnedEmprunt = null;
        empruntList = getEmpruntList(); // Call the getEmpruntList method to retrieve emprunt data from the database
        LivreDAO livreDAO = new LivreDAO(connection);

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

        System.out.println("Book with ISBN " + ISBN + " has been returned.");

        empruntList.remove(returnedEmprunt);
        Livre livre = livreDAO.getLivreByISBN(ISBN);

        if (livre != null) {
            livre.setStatus(Status.disponible);
            livreDAO.updateLivre(livre);
            System.out.println("Book with ISBN " + ISBN + " is now available.");
        } else {
            System.out.println("No Livre found for ISBN " + ISBN + ".");
        }
    }

    private List<emprunt> getEmpruntList() throws DAOException {
        List<emprunt> empruntList = new ArrayList<>();

        try {
            String query = "SELECT l.*, m.*, e.* FROM emprunt AS e INNER JOIN membre AS m INNER JOIN livre AS l ON e.ISBN = l.ISBN AND e.Numero_membre = m.Numero_membre;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                int Numero_membre = resultSet.getInt("Numero_membre");
                Date date_emprunt = resultSet.getTimestamp("Date_emprunt");
                Date date_retour = resultSet.getTimestamp("Date_retour");

                Livre livre=new Livre();
               livre.setIsbn(ISBN);

                Membre member = new Membre();
                member.setNumero_membre(Numero_membre);

                emprunt emprunt = new emprunt(livre, member, date_emprunt, date_retour);
                empruntList.add(emprunt);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieving emprunt list.");
        }
        return empruntList;
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
            throw new DAOException("Error while inserting emprunt record.");
        }
    }


}

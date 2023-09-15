package DAO;

import Exceptions.DAOException;
import Model.Membre;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembreDAO {
    private final Connection connection;

    public MembreDAO(Connection connection) {
        this.connection = connection;
    }

    public void createMember(Membre member) throws DAOException {
        if (member.getNom() == null || member.getNom().isEmpty())
                {
            System.out.println("One or more input fields are empty or null. member not added.");
            return;
        }
        try {
            String insertQuery = "INSERT INTO membre (Numero_membre, nom) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, member.getNumero_membre());
            preparedStatement.setString(2, member.getNom());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Member"+member+"added successfully.");
            } else {
                System.out.println("Failed to add the member.");
            }
        } catch (SQLException e) {
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException && e.getMessage().contains("Duplicate entry")) {
                throw new DAOException.DuplicateException("member already exists.", e);
            } else {
                throw new DAOException("Error while creating a book.");
            }
        }
    }

    public void updateMember(Membre member) throws DAOException {
        try {
            String updateQuery = "UPDATE membre SET Numero_membre = ?, nom = ? WHERE Numero_membre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, member.getNumero_membre());
            preparedStatement.setString(2, member.getNom());
            preparedStatement.setInt(3, member.getNumero_membre());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Member updated successfully.");
            } else {
                System.out.println("Failed to update the member.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error while updating the member.");
        }
    }

    public void deleteMember(int numero_membre) throws DAOException {
        try {
            String deleteQuery = "DELETE FROM membre WHERE Numero_membre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, numero_membre);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Member deleted successfully.");
            } else {
                System.out.println("Failed to delete the member.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error while deleting the member.");
        }
    }

    public Membre getMemberByNumero(int numero_membre) throws DAOException {
        try {
            String selectQuery = "SELECT * FROM membre WHERE Numero_membre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, numero_membre);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numMembre = resultSet.getInt("Numero_membre");
                String nom = resultSet.getString("nom");
                return new Membre(numMembre, nom);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching member by Numero_membre.");
        }
        return null;
    }
}

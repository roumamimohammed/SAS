package DAO;

import Model.Membre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    private Connection connection;

    public MembreDAO(Connection connection) {
        this.connection = connection;
    }

    public Membre createMember(Membre member) {
        try {
            String insertQuery = "INSERT INTO membre (Numero_membre, nom) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, member.getNumero_membre());
            preparedStatement.setString(2, member.getNom());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Member added successfully.");
            } else {
                System.out.println("Failed to add the member.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public void updateMember(Membre member) {
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
            e.printStackTrace();
        }
    }

    public void deleteMember(int numero_membre) {
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
            e.printStackTrace();
        }
    }

    public Membre getMemberByNumero(int numero_membre) {
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
            e.printStackTrace();
        }
        return null;
    }
}

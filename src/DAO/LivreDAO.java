package DAO;

import Model.Livre;
import Model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    private Connection connection;

    public LivreDAO(Connection connection) {
        this.connection = connection;
    }

    public void createLivre(Livre livre) {
        try {
            String insertQuery = "INSERT INTO livre (title, author, isbn, status) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, livre.getTitle());
            preparedStatement.setString(2, livre.getAuthor());
            preparedStatement.setString(3, livre.getIsbn());
            preparedStatement.setString(4, livre.getStatus().toString());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book added successfully.");
            } else {
                System.out.println("Failed to add the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM livre";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String statusStr = resultSet.getString("status");
                Status status = Status.valueOf(statusStr);

                Livre livre = new Livre(title, author, isbn, status);
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }

    public void updateLivre(Livre livre) {
        try {
            String updateQuery = "UPDATE livre SET title = ?, author = ?, status = ? WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, livre.getTitle());
            preparedStatement.setString(2, livre.getAuthor());
            preparedStatement.setString(3, livre.getStatus().toString());
            preparedStatement.setString(4, livre.getIsbn());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to update the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLivre(String isbn) {
        try {
            String deleteQuery = "DELETE FROM livre WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, isbn);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Failed to delete the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

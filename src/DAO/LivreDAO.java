package DAO;

import Exceptions.DAOException;
import Model.Livre;
import Model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    private final Connection connection;

    public LivreDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Livre> getBorrowedLivres() throws DAOException {
        List<Livre> borrowedLivres = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM livre WHERE status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, Status.emprunte.toString());
            execute(borrowedLivres, preparedStatement);
        } catch (SQLException e) {
            throw new DAOException("Error while getting borrowed books.", e);
        }

        return borrowedLivres;
    }

    public List<Livre> getLostLivres() throws DAOException {
        List<Livre> lostLivres = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM livre WHERE status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, Status.perdu.toString());
            execute(lostLivres, preparedStatement);
        } catch (SQLException e) {
            throw new DAOException("Error while getting lost books.", e);
        }

        return lostLivres;
    }

    private void execute(List<Livre> lostLivres, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            String isbn = resultSet.getString("isbn");
            String statusStr = resultSet.getString("status");
            Status status = Status.valueOf(statusStr);

            Livre livre = new Livre(title, author, isbn, status);
            lostLivres.add(livre);
        }
    }

    public void displayStatistics() throws DAOException {
        int totalBooks = 0;
        int lostBooks = 0;
        int borrowedBooks = 0;
        int availableBooks = 0;

        try {
            String selectQuery = "SELECT * FROM livre";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String statusStr = resultSet.getString("status");
                Status status = Status.valueOf(statusStr);

                switch (status) {
                    case disponible -> availableBooks++;
                    case emprunte -> borrowedBooks++;
                    case perdu -> lostBooks++;
                }
                totalBooks++;
            }

            System.out.println("Total Books: " + totalBooks);
            System.out.println("Lost Books: " + lostBooks);
            System.out.println("Borrowed Books: " + borrowedBooks);
            System.out.println("Available Books: " + availableBooks);
        } catch (SQLException e) {
            throw new DAOException("Error while displaying statistics.", e);
        }
    }


    public void createLivre(Livre livre) throws DAOException {
        if (livre.getTitle() == null || livre.getTitle().isEmpty() ||
                livre.getAuthor() == null || livre.getAuthor().isEmpty() ||
                livre.getIsbn() == null || livre.getIsbn().isEmpty()) {
            System.out.println("One or more input fields are empty or null. Book not added.");
            return;
        }
        try {
            String insertQuery = "INSERT INTO livre (title, author, isbn, status) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, livre.getTitle());
            preparedStatement.setString(2, livre.getAuthor());
            preparedStatement.setString(3, livre.getIsbn());
            preparedStatement.setString(4, livre.getStatus().toString());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
            } else {
                System.out.println("Failed to add the book.");
            }
        } catch (SQLException e) {
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException && e.getMessage().contains("Duplicate entry")) {
                throw new DAOException.DuplicateException("ISBN already exists.", e);
            } else {
                throw new DAOException("Error while creating a book.", e);
            }
        }
    }

    public void updateLivre(Livre livre) throws DAOException {
        if (livre.getTitle() == null || livre.getTitle().isEmpty() ||
                livre.getAuthor() == null || livre.getAuthor().isEmpty() ||
                livre.getIsbn() == null || livre.getIsbn().isEmpty()) {
            System.out.println("One or more input fields are empty or null. Book not added.");
            return;
        }
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
            throw new DAOException("Error while updating the book.", e);
        }
    }

    public Livre getLivreByISBN(String isbn) throws DAOException {
        try {
            String selectQuery = "SELECT * FROM livre WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbnStr = resultSet.getString("isbn");
                String statusStr = resultSet.getString("status");
                Status status = Status.valueOf(statusStr);

                return new Livre(title, author, isbnStr, status);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching book by ISBN.", e);
        }

        return null;
    }

    public List<Livre> getAllAvailableLivres() throws DAOException {
        List<Livre> availableLivres = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM livre WHERE status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, Status.disponible.toString());
            execute(availableLivres, preparedStatement);
        } catch (SQLException e) {
            throw new DAOException("Error while fetching available books.", e);
        }

        return availableLivres;
    }

    public List<Livre> searchLivresByTitleOrAuthor(String keyword) throws DAOException {
        List<Livre> matchingLivres = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM livre WHERE title LIKE ? OR author LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");
            execute(matchingLivres, preparedStatement);
        } catch (SQLException e) {
            throw new DAOException("Error while searching for books.", e);
        }

        return matchingLivres;
    }

    public void deleteLivre(String isbn) throws DAOException {
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
            throw new DAOException("Error while deleting the book.", e);
        }
    }

    public void markLivreAsLost(String isbn) throws DAOException {
        try {
            String updateQuery = "UPDATE livre SET status = ? WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, Status.perdu.toString());
            preparedStatement.setString(2, isbn);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book marked as lost successfully.");
            } else {
                System.out.println("Failed to mark the book as lost.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error while marking the book as lost.", e);
        }
    }
}

package Model;

public class Livre {
    private Status newStatus;
    private String title;
    private String author;
    private String isbn;

    public Livre(String title, String author, String isbn, Status newStatus) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.newStatus = newStatus;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Status getStatus() {
        return newStatus;
    }


    public void setStatus(Status status) {
        this.newStatus = status;
    }
}

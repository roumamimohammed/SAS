package Model;

public enum Status {
    disponible,
    eumprinter,
    perdu;

    public static Status getDefaultStatus() {
        return disponible;
    }
}

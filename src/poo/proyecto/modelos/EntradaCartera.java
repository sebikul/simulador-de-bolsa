package poo.proyecto.modelos;

public final class EntradaCartera {

    private final Titulo titulo;
    private int amount;

    public EntradaCartera(int amount, Titulo titulo) {

        this.amount = amount;
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return titulo.getSimbolo() + "*" + amount + " (" + titulo.getValor() + ")";
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public String name() {
        return titulo.getSimbolo();
    }

}

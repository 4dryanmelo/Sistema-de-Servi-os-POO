package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Pedido {
    private final int id;
    private final Cliente cliente;
    private final Servico servico;
    private String recado;
    private String data;
    private boolean concluido;

    public Pedido(int id, Cliente cliente, Servico servico, String recado) {
        if (id < 1 || cliente == null || servico == null) {
            throw new IllegalArgumentException("O pedido precisa de um ID positivo, de um cliente, de um serviço e agendar para uma data após a atual.");
        }
        this.id = id;
        this.cliente = cliente;
        this.servico = servico;
        editarRecado(recado);
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Servico getServico() { return servico; }
    public String getRecado() { return recado; }
    public String getData() { return data; }
    public boolean isConcluido() { return concluido; }

    public void editarRecado(String recado) {
        if (concluido) {
            throw new IllegalStateException("Este pedido já foi concluído.");
        }
        if (recado == null || recado.trim().isEmpty()) {
            throw new IllegalArgumentException("Escreva um recado para o prestador.");
        }
        this.recado = recado.trim();
    }

    public void concluir() {
        if (concluido) {
            throw new IllegalStateException("Este pedido já foi concluído.");
        }
        concluido = true;
    }

    

    @Override
    public String toString() {
        String estado = concluido ? "Concluído" : "Pendente";
        return "#" + id + " " + servico.getNome() + " | " + cliente.getNome() + " | " + recado + " | " + data + " | " + estado;
    }
}

package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public final class Pedido {
    private final int id;
    private final Cliente cliente;
    private final Servico servico;
    private String recado;
    private LocalDate data;
    private boolean concluido;

    public Pedido(int id, Cliente cliente, Servico servico, String recado, String data) {
        if (id < 1 || cliente == null || servico == null) {
            throw new IllegalArgumentException("O pedido precisa de um ID positivo, de um cliente, de um serviço e agendar uma data correta para o serviço.");
        }
        this.id = id;
        this.cliente = cliente;
        this.servico = servico;
        this.data = validarData(data);
        editarRecado(recado);
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Servico getServico() { return servico; }
    public String getRecado() { return recado; }
    public LocalDate getData() { return data; }
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
    
    public LocalDate validarData(String texto) {
    	
    	DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    	
    	if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a data do pedido (dd/MM/aaaa).");
        }
        LocalDate data;
        try {
            data = LocalDate.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException erro) {
            throw new IllegalArgumentException("Data inválida. Use o formato dd/MM/aaaa, com uma data existente.");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data do pedido não pode ser anterior à data atual.");
        }
        return data; 
    }
   
    @Override
    public String toString() {
        String estado = concluido ? "Concluído" : "Pendente";
        return "#" + id + " " + servico.getNome() + " | " + cliente.getNome() + " | " + recado + " | " + getData() + " | " + estado;
    }
}

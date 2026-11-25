package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Servico;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {
    private static final String CAMINHO_ARQUIVO = "src/dados/dadosservicos.json";
    private static final Type TIPO_LISTA = new TypeToken<List<Servico>>() {}.getType();
    private final Gson gson;

    public ServicoDAO() {
    	gson = new GsonBuilder().create();
    }

    public boolean servicoExiste(int id) {
        return servicoJaCadastrado(lerJson(), id);
    }

    private boolean servicoJaCadastrado(List<Servico> lista, int id) {
        if (id < 1) return false;
        for (Servico s : lista) {
            if (s.getId() == id) return true;
        }
        return false;
    }

    public boolean cadastrar(Servico novoServico) {
        List<Servico> lista = lerJson();
        if (servicoJaCadastrado(lista, novoServico.getId())) return false;
        lista.add(novoServico);
        salvarJson(lista);
        return true;
    }
    
    public void atualizar(Servico servicoEditado) {
        List<Servico> lista = lerJson();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == servicoEditado.getId()) {
                lista.set(i, servicoEditado);
                salvarJson(lista);
                return;
            }
        }
    }

    public List<Servico> lerJson() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists() || arquivo.length() == 0) return new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(arquivo.toPath(), StandardCharsets.UTF_8)) {
            List<Servico> lista = gson.fromJson(reader, TIPO_LISTA);
            validarLista(lista);
            return lista;
        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException("Erro ao ler " + CAMINHO_ARQUIVO + ". " + e.getMessage(), e);
        }
    }

    private void validarLista(List<Servico> lista) {
        if (lista == null) throw new JsonParseException("Era esperada uma lista de serviços.");
        for (Servico s : lista) {
            if (s == null || s.getNome() == null || s.getNome().trim().isEmpty()) {
                throw new JsonParseException("O cadastro está incompleto. Confira o id do serviço.");
            }
        }
    }

    public void salvarJson(List<Servico> lista) {
        validarLista(lista);
        File arquivo = new File(CAMINHO_ARQUIVO);
        try {
            Files.createDirectories(arquivo.getParentFile().toPath());
            try (Writer writer = Files.newBufferedWriter(arquivo.toPath(), StandardCharsets.UTF_8)) {
                gson.toJson(lista, TIPO_LISTA, writer);
            }
        } catch (IOException | JsonIOException e) {
            throw new IllegalStateException("Erro ao salvar " + CAMINHO_ARQUIVO + ". " + e.getMessage(), e);
        }
    }
   
}
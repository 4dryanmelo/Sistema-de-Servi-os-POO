package dao;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Prestador;
import model.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String CAMINHO_ARQUIVO = "src/dados/dados.json";
    private static final Type TIPO_LISTA = new TypeToken<List<Usuario>>() {}.getType();
    private final Gson gson;

    public UsuarioDAO() {
        gson = new GsonBuilder().registerTypeAdapter(Usuario.class, new UsuarioAdapter()).create();
    }

    public boolean cadastroExiste(String login) {
        return loginJaCadastrado(lerJson(), login);
    }

    private boolean loginJaCadastrado(List<Usuario> lista, String login) {
        if (login == null) return false;
        for (Usuario u : lista) {
            if (u.getLogin().equalsIgnoreCase(login.trim())) return true;
        }
        return false;
    }

    public boolean cadastrar(Usuario novoUsuario) {
        List<Usuario> lista = lerJson();
        if (loginJaCadastrado(lista, novoUsuario.getLogin())) return false;
        lista.add(novoUsuario);
        salvarJson(lista);
        return true;
    }

    public List<Usuario> lerJson() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists() || arquivo.length() == 0) return new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(arquivo.toPath(), StandardCharsets.UTF_8)) {
            List<Usuario> lista = gson.fromJson(reader, TIPO_LISTA);
            validarLista(lista);
            return lista;
        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException("Erro ao ler " + CAMINHO_ARQUIVO + ". " + e.getMessage(), e);
        }
    }

    private void validarLista(List<Usuario> lista) {
        if (lista == null) throw new JsonParseException("Era esperada uma lista de usuários.");
        for (Usuario u : lista) {
            if (u == null || !u.acessoValido() || u.getNome() == null || u.getNome().trim().isEmpty()) {
                throw new JsonParseException("Um cadastro está incompleto. Confira o nome, o login e a senha.");
            }
        }
    }

    public void salvarJson(List<Usuario> lista) {
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

    public Usuario logar(String login, String senha) {
        if (login == null || senha == null) return null;
        for (Usuario u : lerJson()) {
            if (u.getLogin().equalsIgnoreCase(login.trim()) && u.autenticar(senha)) return u;
        }
        return null;
    }

    private static class UsuarioAdapter implements JsonSerializer<Usuario>, JsonDeserializer<Usuario> {
        @Override
        public JsonElement serialize(Usuario src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty("tipo", src.getPerfil());
            return obj;
        }

        @Override
        public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            if (!json.isJsonObject()) throw new JsonParseException("Era esperado um cadastro de usuário.");
            JsonObject obj = json.getAsJsonObject();
            if (!obj.has("tipo") || !obj.get("tipo").isJsonPrimitive()) {
                throw new JsonParseException("Falta o campo tipo no cadastro. Informe Cliente ou Prestador no arquivo.");
            }
            String tipo = obj.get("tipo").getAsString();
            if ("Cliente".equalsIgnoreCase(tipo)) return context.deserialize(obj, Cliente.class);
            if ("Prestador".equalsIgnoreCase(tipo)) return context.deserialize(obj, Prestador.class);
            throw new JsonParseException("Tipo de usuário desconhecido: " + tipo);
        }
    }
}

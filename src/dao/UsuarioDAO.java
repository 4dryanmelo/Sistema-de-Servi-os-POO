package dao; // ou package dao; se você criar a pasta dao

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Prestador;
import model.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String CAMINHO_ARQUIVO = "src/dados/dados.json";
    private final Gson gson;

    public UsuarioDAO() {
        this.gson = new Gson();
    }

    public boolean cadastroExiste(String login) {
        List<Usuario> lista = lerJson();
        return loginJaCadastrado(lista, login);
    }

    private boolean loginJaCadastrado(List<Usuario> lista, String login) {
        if (login == null) return false;
        for (Usuario u : lista) {
            if (u.getLogin() != null && u.getLogin().equalsIgnoreCase(login.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean cadastrar(Usuario novoUsuario) {
        List<Usuario> lista = lerJson();

        if (loginJaCadastrado(lista, novoUsuario.getLogin())) {
            return false;
        }

        lista.add(novoUsuario);
        salvarJson(lista);
        return true;
    }

    public List<Usuario> lerJson() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(arquivo)) {
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            List<Usuario> lista = gson.fromJson(reader, tipoLista);
            if(lista!=null) {
            	return lista;
            }else {
            	return new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void salvarJson(List<Usuario> lista) {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }

        try (Writer writer = new FileWriter(arquivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar no arquivo JSON: " + e.getMessage());
        }
    }

    public Usuario logar(String login, String senha) {
        if (login == null || senha == null) {
            return null;
        }

        List<Usuario> lista = lerJson();

        for (Usuario u : lista) {
            boolean loginCerto = u.getLogin() != null && u.getLogin().equalsIgnoreCase(login.trim());
            boolean senhaCerta = u.autenticar(senha);

            if (loginCerto && senhaCerta) {
                return u;
            }
        }
        return null;
    }

    // Adaptador interno para o Gson conseguir instanciar Cliente ou Prestador
    private static class UsuarioAdapter implements JsonSerializer<Usuario>, JsonDeserializer<Usuario> {
        @Override
        public JsonElement serialize(Usuario src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src).getAsJsonObject();
            obj.addProperty("tipo", src.getPerfil());
            return obj;
        }

        @Override
        public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String tipo = obj.get("tipo").getAsString();
            if ("Cliente".equalsIgnoreCase(tipo)) {
                return context.deserialize(obj, Cliente.class);
            } else if ("Prestador".equalsIgnoreCase(tipo)) {
                return context.deserialize(obj, Prestador.class);
            }
            throw new JsonParseException("Tipo de usuário desconhecido: " + tipo);
        }
    }
}
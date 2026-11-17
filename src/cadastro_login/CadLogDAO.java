package cadastro_login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CadLogDAO {

    private static final String CAMINHO_ARQUIVO = "src/dados/dados.json";
    private final Gson gson;

    public CadLogDAO() {
        this.gson = new Gson();
    }

    public boolean cadastroExiste(String email) {
        ArrayList<Cadastro> lista = lerJson();
        return emailJaCadastrado(lista, email);
    }
    
    private boolean emailJaCadastrado(ArrayList<Cadastro> lista, String email) {
        for (Cadastro c : lista) {
            if (c.getEmail() != null && c.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean cadastrar(Cadastro novoCadastro) {
        ArrayList<Cadastro> lista = lerJson();

        if (emailJaCadastrado(lista, novoCadastro.getEmail())) {
            return false; 
        }

        lista.add(novoCadastro);
        salvarJson(lista);
        return true; 
    }

    public ArrayList<Cadastro> lerJson() {
        try (Reader reader = new FileReader(CAMINHO_ARQUIVO)) {
            Type tipoLista = new TypeToken<ArrayList<Cadastro>>() {}.getType();
            ArrayList<Cadastro> lista = gson.fromJson(reader, tipoLista);

            if (lista == null) {
                return new ArrayList<>();
            }
            return lista;

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void salvarJson(ArrayList<Cadastro> lista) {
        try (Writer writer = new FileWriter(CAMINHO_ARQUIVO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar no arquivo JSON: " + e.getMessage());
        }
    }
    
    public Cadastro logar(String email,String senha) {
    	ArrayList<Cadastro> lista= lerJson();
    	
    	for(Cadastro c : lista) {
    		boolean emailCerto= c.getEmail().equals(email)&& c.getEmail()!=null;
    		boolean senhaCerta= c.getSenha().equals(senha)&& c.getSenha()!=null;
    		
    		if(emailCerto&&senhaCerta) {
    			return c;
    		}
    	}return null;
    	
    	
    }
}
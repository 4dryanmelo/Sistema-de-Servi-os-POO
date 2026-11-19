package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exception.RegraNegocioException;

public class Projeto {
	public static final String[] CATEGORIAS_DISPONIVEIS= {
				    "Desenvolvimento Web",
				    "Desenvolvimento Mobile",
				    "Desenvolvimento de Software e Jogos",
				    "Segurança da Informação",
				    "Banco de Dados e Cloud",
				    "QA e Testes",
				    "Engenharia e Integração de IA",
				    "Ciência e Análise de Dados",
				    "Treinamento e Avaliação de Modelos",
				    "Identidade Visual e Branding",
				    "UI/UX Design",
				    "Design Gráfico e Mídias Sociais",
				    "Ilustração e Modelagem 3D",
				    "Copywriting",
				    "Redação SEO e Blogs",
				    "Tradução e Localização",
				    "Revisão e Edição",
				    "Gestão de Tráfego Pago",
				    "Social Media",
				    "SEO (Otimização para Buscadores)",
				    "Prospecção e Vendas",
				    "Edição de Vídeo",
				    "Motion Graphics e Animação",
				    "Produção Musical e Áudio",
				    "Suporte Administrativo (Assistente Virtual)",
				    "Contabilidade e Finanças",
				    "Consultoria Jurídica",
				    "Gestão de Projetos e Produtos",
				    "Arquitetura e Engenharia",
				    "Educação e Mentoria",
				    "Serviços Locais e Eventos"	
	};
	
	private String id;
	private String titulo;
	private String categoria;
	private double orcamento;
	private String status;
	
	private Cliente clienteAutor;
	private Freela freelaContratado;
	
	private List<Proposta> propostas;
	
	public Projeto(String titulo,String categoria, double orcamento, Cliente clienteAutor) throws RegraNegocioException{
		if(orcamento<=0) {
			throw new RegraNegocioException("O orcçamento deve ser maior que zero");
		}
		this.id=UUID.randomUUID().toString();
		this.titulo=titulo;
		this.categoria=categoria;
		this.orcamento=orcamento;
		this.clienteAutor=clienteAutor;
		this.status="ABERTO";
		this.propostas= new ArrayList<>();
		
	}
}

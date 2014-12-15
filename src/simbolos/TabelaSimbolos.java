package simbolos;

import java.util.ArrayList;

public class TabelaSimbolos {

	private int id;
	private String nome;
	private String classe;

	private int position;
	private int linha;
	private String tipo;
	private Object valor;

	private ArrayList<String> parametros;
	private String retorno;
	private boolean global;

	public TabelaSimbolos(int id, String conteudo, int position, int linha) {

		this.id = id;

		this.nome = conteudo;

		this.position = position;

		this.linha = linha;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public ArrayList<String> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<String> parametros) {
		this.parametros = parametros;
	}

	public String getRetorno() {
		return retorno;
	}

	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

}

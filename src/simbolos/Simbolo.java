package simbolos;

import java.util.List;

import tokens.Palavra;

public class Simbolo {

	/*
	 * Escopo do identificador.
	 */
	private int escopo;

	/*
	 * Tipo da variável, INTEIRO ou BOOLEANO
	 */
	private Palavra tipo;
	
	/*
	 * Classe variável, FUNCAO ou PROCED
	 */
	private String classe;
	
	/*
	 * Nome do identificador.
	 */
	private String lexema;

	/*
	 * Lista de parametros
	 */
	private List<Parametro> listaParametros;

	public Simbolo(int escopo, Palavra tipo, String lexema,
			List<Parametro> listaParametros, String classe) {
		
		this.escopo = escopo;
		this.tipo = tipo;
		this.lexema = lexema;
		this.classe = classe;
		this.listaParametros = listaParametros;
	}
	
	public Simbolo(){
		
	}

	public int getEscopo() {
		return escopo;
	}

	public void setEscopo(int escopo) {
		this.escopo = escopo;
	}

	public Palavra getTipo() {
		return tipo;
	}

	public void setTipo(Palavra tipo) {
		this.tipo = tipo;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public List<Parametro> getListaParametros() {
		return listaParametros;
	}

	public void setListaParametros(List<Parametro> listaParametros) {
		this.listaParametros = listaParametros;
	}
	
	public String getTipoLexema() {

		String s = "";

		if (tipo == null) {

			s = "null";

		} else {

			s = tipo.lex;

		}

		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classe == null) ? 0 : classe.hashCode());
		result = prime * result + escopo;
		result = prime * result + ((lexema == null) ? 0 : lexema.hashCode());
		result = prime * result
				+ ((listaParametros == null) ? 0 : listaParametros.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Simbolo other = (Simbolo) obj;
		if (classe == null) {
			if (other.classe != null)
				return false;
		} else if (!classe.equals(other.classe))
			return false;
		if (escopo != other.escopo)
			return false;
		if (lexema == null) {
			if (other.lexema != null)
				return false;
		} else if (!lexema.equals(other.lexema))
			return false;
		if (listaParametros == null) {
			if (other.listaParametros != null)
				return false;
		} else if (!listaParametros.equals(other.listaParametros))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		
		return this.lexema;
	}

}

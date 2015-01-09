package analisador_sintatico;

import java.util.List;
import java.util.Stack;

import tokens.Token;
import excecao.Excecao;

public abstract class ASintatico {

	/*
	 * Metodo para passagem dos erros
	 */
	protected Excecao ex = new Excecao();

	/*
	 * Pilha que gerencia o escopo das variáveis
	 */
	public static Stack<Integer> escopo = new Stack<Integer>();

	/*
	 * Variável que conta o número de escopos
	 */
	protected int contaEscopo = 0;

	/*
	 * Variável que mostra o escopo atualizado
	 */
	protected int escopoAtual = 0;

	/*
	 * Indice que percorre a list de tokens
	 */
	protected int indiceLista = 0;

	/*
	 * Lista que recebe a lista de tokens da análise léxica
	 */
	protected List<Token> listaTokens;

	/*
	 * Contagem de erros
	 */
	protected int numeroErro = 0;

	/*
	 * Verifica se é uma função ou procedimento, se for um procedimento não pode
	 * ser atribuida
	 */
	protected int isProced = 0;

	/*
	 * Contrutor
	 */
	public ASintatico(List<Token> l) {

		listaTokens = l;

	}// public Sintatico(List<Token> l) {}
	
	/*
	 * Contrutor
	 */
	public ASintatico() {

	}// public Sintatico(List<Token> l) {}

	/*
	 * Metodo que verifica se o token existe na lista de tokes. Se existir é
	 * retornado true senão false
	 */
	public boolean comparaLexema(int t) {

		if (!listaTokens.isEmpty()) {

			if (listaTokens.get(indiceLista).tag == t) {

				return true;

			} else {

				return false;

			}

		} else {

			return false;

		}
	}

	public void consomeToken() {

		if (listaTokens.get(indiceLista).toString().equals("{")
				|| listaTokens.get(indiceLista).toString().equals("INICIO")) {

			this.empilhaLexema();

		} else if (listaTokens.get(indiceLista).toString().equals("}")
				|| listaTokens.get(indiceLista).toString().equals("FIM")) {

			this.desempilhaLexema();

		} else {

		}

		indiceLista++;

	}

	/*
	 * Metodo finaliza o programa caso a análise léxica seja aceita
	 */
	protected void finalizarPrograma(String s) {

		ex.excecao("Análise " + s + " aceita.");

		return;

	}// private void finalizarPrograma(){}

	private void desempilhaLexema() {

		escopo.pop();

		try {

			escopoAtual = escopo.peek();

		} catch (Exception e) {

		}

	}

	private void empilhaLexema() {

		contaEscopo++;

		escopo.push(contaEscopo);

		try {

			escopoAtual = escopo.peek();

		} catch (Exception e) {

		}

	}

}

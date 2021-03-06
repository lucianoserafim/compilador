package analisador_lexico;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

import excecao.Excecao;
import simbolos.TabelaDeSimbolos;
import tokens.Numerico;
import tokens.Palavra;
import tokens.Tag;
import tokens.Token;
import tokens.Type;

/**
 * Classe para análise lexica
 * 
 * @author luciano
 *
 */
public class Lexico {

	/*
	 * Classe que modela as exceções
	 */
	Excecao ex = new Excecao();

	int e = 0;

	/*
	 * Código passado para a anáĺise
	 */
	String codigoFonte = "";

	/*
	 * Palavra reconhecida no código
	 */
	String lexema = "";

	/*
	 * Linha onde está sendo feita a análise
	 */
	int linha = 1;

	/*
	 * Número do caractere lido
	 */
	int numeroCaractereAtual = 0;

	/*
	 * Simbolo lido
	 */
	char caractereAtualLido;

	/*
	 * Caractere lido a frente do caractere atual
	 */
	char lookAhead;

	/*
	 * Lista de todos os lexemas que sera passado para o sintático
	 */
	List<Token> listaTokens = new ArrayList<Token>();

	/*
	 * Tabela de palavras reservadas
	 */
	Hashtable<String, Palavra> palavrasReservadas = new Hashtable<String, Palavra>();

	/*
	 * Construtor
	 */
	public Lexico(String codigo) {

		listaTokens.clear();

		/*
		 * Código que será análisado
		 */
		this.codigoFonte = codigo;

		/*
		 * Palavras reservadas
		 */
		this.reservaPalavras(new Palavra("INICIO", Tag.INICIO, -1));
		this.reservaPalavras(new Palavra("FIM", Tag.FIM, -1));

		this.reservaPalavras(new Palavra("SE", Tag.SE, -1));
		this.reservaPalavras(new Palavra("SENAO", Tag.SENAO, -1));
		this.reservaPalavras(new Palavra("ENQUANTO", Tag.ENQUANTO, -1));
		this.reservaPalavras(new Palavra("PARE", Tag.PARE, -1));
		this.reservaPalavras(new Palavra("CONTINUE", Tag.CONTINUE, -1));
		this.reservaPalavras(new Palavra("RETORNA", Tag.RETORNA, -1));
		this.reservaPalavras(new Palavra("FUNCAO", Tag.FUNCAO, -1));
		this.reservaPalavras(new Palavra("IDENTIFICADOR", Tag.IDENTIFICADOR, -1));
		this.reservaPalavras(new Palavra("$", Tag.FINAL, -1));

		this.reservaPalavras(Palavra.VERDADEIRO);
		this.reservaPalavras(Palavra.FALSO);
		this.reservaPalavras(Palavra.VOID);

		this.reservaPalavras(Palavra.ATRIBUICAO);

		this.reservaPalavras(Type.INTEIRO);
		this.reservaPalavras(Type.BOOLEANO);

		this.reservaPalavras(Palavra.IGUAL);
		this.reservaPalavras(Palavra.DIFERENTE);
		this.reservaPalavras(Palavra.MAIOR_Q);
		this.reservaPalavras(Palavra.MENOR_Q);
		this.reservaPalavras(Palavra.MAIOR_IGUAL);
		this.reservaPalavras(Palavra.MENOR_IGUAL);
		this.reservaPalavras(Palavra.IMPRIME);

	}// Lexico(String codigo) {}

	/*
	 * Metodo que preenche a tabela de palavras reservadas no construtor
	 */
	public void reservaPalavras(Palavra p) {

		palavrasReservadas.put(p.lex, p);

	}// reservaPalavras(Palavra p) {}

	/*
	 * Metodo responsável por retornar a palavra reservada
	 */
	private Palavra getPalavraReservada(String l) {

		try {

			return palavrasReservadas.get(l);

		} catch (Exception e) {

			return null;

		}

	}// getPalavraReservada(String l) {}

	/*
	 * Metodo para verificar se a palavra é maiuscula.
	 */
	public boolean isMaiuscula(char caractere) {

		if (caractere > 64 && caractere < 91) {

			return true;

		}

		return false;

	}// isMaiuscula(char caractere) {}

	/*
	 * Metodo para verificar se a palavra é um identificador. As palavras
	 * reservadas são somente com letras maiúsculas
	 */
	public boolean isMinuscula(char caractere) {

		if (caractere > 96 && caractere < 123) {

			return true;

		} else {

			return false;

		}

	}// isMinuscula(char caractere) {}

	/*
	 * Metodo verifica se a palavra é um numero
	 */
	private boolean isNumerico(char caractere) {

		// Segundo a tabela ASCII 0-9
		if (caractere > 47 && caractere < 58) {

			return true;

		}

		return false;

	}// isNumerico(char caractere) {}

	/*
	 * Metodo responsável por verificar se a palavra reservada existe
	 */
	private boolean isPalavraReservada(String l) {

		boolean p = palavrasReservadas.containsKey(l);

		return p;

	}// isPalavraReservada(String l) {}

	/*
	 * Metodo que le o simbolo atual
	 */
	private char lerCaractereAtual() {

		caractereAtualLido = codigoFonte.charAt(numeroCaractereAtual);

		this.lerProximoCaractere();

		numeroCaractereAtual++;

		return caractereAtualLido;

	}// lerCaractereAtual() {}

	/*
	 * Metodo que lê o lookahead
	 */
	private char lerProximoCaractere() {

		try {

			this.lookAhead = codigoFonte.charAt(numeroCaractereAtual + 1);

		} catch (Exception e) {

			lookAhead = '$';

			return '$';
		}

		return lookAhead;

	}// lerProximoCaractere() {}

	/*
	 * Metodo responsável por ler todo código e devolver uma lista para o
	 * análisador sintático
	 */
	public List<Token> scanear() {

		TabelaDeSimbolos.getTabelaDeSimbolos().getListaDeSimbolos().clear();

		ex.erro = "";

		if (codigoFonte.equals("")) {

			e++;

			ex.excecao("Análise léxica aceita. Palavra vazia.");

			return null;

		} else {

			// Percorre enquanto houver código

			while (numeroCaractereAtual < codigoFonte.length()) {

				lerCaractereAtual();

				if (caractereAtualLido == ' ' || caractereAtualLido == '\t') {

					// Retira espaço em branco e tabulações

				} else if (caractereAtualLido == '\n') {

					// Conta o número de linhas

					linha++;

				} else if (isMaiuscula(caractereAtualLido)) {

					// Verifica se todas são maiusculas

					lexema = caractereAtualLido + "";

					while (isMaiuscula(lookAhead)) {

						lexema = lexema + lookAhead;

						lerCaractereAtual();

					}

					if (isMinuscula(lookAhead) || isNumerico(lookAhead)) {

						e++;

						ex.excecao(
								"Erro léxico: A palavra esperada não é reservada : ",
								lexema, linha);

						return null;

					} else if (isPalavraReservada(lexema)) {

						Token t = new Token(getPalavraReservada(lexema).tag,
								linha);

						t.setNomeDoToken(lexema);

						listaTokens.add(t);

					} else {

						e++;

						ex.excecao("Erro léxico: A palavra não é reservada : ",
								lexema, linha);

						return null;

					}

				} else if (isMinuscula(caractereAtualLido)) {

					// verifica se é um identificador

					lexema = caractereAtualLido + "";

					while (isMinuscula(lookAhead)) {

						lexema = lexema + lookAhead;

						lerCaractereAtual();

					}

					if (isMaiuscula(lookAhead) || isNumerico(lookAhead)) {

						e++;

						ex.excecao("Erro léxico: Era esperado um: ", lexema
								+ " : Palavra não é um identificador", linha);

						return null;

					} else {

						Token t = new Token(Tag.IDENTIFICADOR, linha);

						t.setNomeDoToken(lexema);

						listaTokens.add(t);

					}

				} else if (isNumerico(caractereAtualLido)) {

					// Verifica se é um númerico

					lexema = caractereAtualLido + "";

					while (isNumerico(lookAhead)) {

						lexema = lexema + lookAhead;

						lerCaractereAtual();

					}

					if (isMaiuscula(lookAhead) || isMinuscula(lookAhead)) {

						e++;

						ex.excecao(
								"Erro léxico: A palavra não é um númerico : ",
								lexema, linha);

						return null;

					} else {

						int v = Integer.parseInt(lexema);

						Numerico n = new Numerico(v, linha);

						n.setNomeDoToken("NUMERICO");

						listaTokens.add(n);

					}

				} else if (caractereAtualLido == '(') {

					listaTokens.add(new Token('(', linha));

				} else if (caractereAtualLido == ')') {

					listaTokens.add(new Token(')', linha));

				} else if (caractereAtualLido == '{') {

					listaTokens.add(new Token('{', linha));

				} else if (caractereAtualLido == '}') {

					listaTokens.add(new Token('}', linha));

				} else if (caractereAtualLido == ';') {

					listaTokens.add(new Token(';', linha));

				} else if (caractereAtualLido == ',') {

					listaTokens.add(new Token(',', linha));

				} else if (caractereAtualLido == '+') {

					listaTokens.add(new Token('+', linha));

				} else if (caractereAtualLido == '-') {

					listaTokens.add(new Token('-', linha));

				} else if (caractereAtualLido == '*') {

					listaTokens.add(new Token('*', linha));

				} else if (caractereAtualLido == '/') {

					listaTokens.add(new Token('/', linha));

				} else if (caractereAtualLido == '!') {

					if (lookAhead == '=') {

						numeroCaractereAtual++;

						Token t = getPalavraReservada("!=");

						t.setLinhaLocalizada(linha);

						t.setNomeDoToken("!=");

						listaTokens.add(t);

					} else {

						e++;

						ex.excecao("Erro léxico: Era esperado o simbolo : ",
								"=", linha);

						return null;

					}

				} else if (caractereAtualLido == '<') {

					if (lookAhead == ' ') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada("<").tag, linha);

						t.setNomeDoToken("<");

						listaTokens.add(t);

					} else if (lookAhead == '=') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada("<=").tag,
								linha);

						t.setNomeDoToken("<=");

						listaTokens.add(t);

					} else {

						e++;

						ex.excecao(
								"Erro léxico: Era esperado o simbolo ou um espaço em branco : ",
								"=", linha);

						return null;

					}

				} else if (caractereAtualLido == '>') {

					if (lookAhead == ' ') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada(">").tag, linha);

						t.setNomeDoToken(">");

						listaTokens.add(t);

					} else if (lookAhead == '=') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada(">=").tag,
								linha);

						t.setNomeDoToken("=>");

						listaTokens.add(t);

					} else {

						e++;

						ex.excecao(
								"Erro léxico: Era esperado o simbolo ou espaço em branco : ",
								"=", linha);

						return null;

					}

				} else if (caractereAtualLido == '=') {

					if (lookAhead == ' ') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada("=").tag, linha);

						t.setNomeDoToken("=");

						listaTokens.add(t);

					} else if (lookAhead == '=') {

						numeroCaractereAtual++;

						Token t = new Token(getPalavraReservada("==").tag,
								linha);

						t.setNomeDoToken("==");

						listaTokens.add(t);

					} else {

						e++;

						ex.excecao(
								"Erro léxico: Era esperado o simbolo ou espaço em branco : ",
								"=", linha);

						return null;

					}

				}

			}// while (numeroCaractereAtual < codigoFonte.length()){}

		}

		Token t = new Token(getPalavraReservada("$").tag, linha);

		listaTokens.add(t);

		ex.excecao("Análise léxica aceita.");

		return listaTokens;

	}// scanear() {}

}// Lexico {}

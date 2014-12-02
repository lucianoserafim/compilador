package analisador_lexico;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import excessao.Excecao;
import simbolos.Type;

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

		/*
		 * Código que será análisado
		 */
		this.codigoFonte = codigo + '$';

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
		this.reservaPalavras(new Palavra("IDENTIFICADOR", Tag.IDENTIFICADOR, -1));
		this.reservaPalavras(new Palavra("FUNCAO", Tag.FUNCAO, -1));
		this.reservaPalavras(new Palavra("PROCED", Tag.PROCED, -1));
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

	}

	/*
	 * Metodo que preenche a tabela de palavras reservadas no construtor
	 */
	public void reservaPalavras(Palavra p) {

		palavrasReservadas.put(p.lexema, p);

	}

	/*
	 * Metodo responsável por retornar a palavra reservada
	 */
	private Palavra getPalavraReservada(String l) {

		try {

			return palavrasReservadas.get(l);

		} catch (Exception e) {

			System.out.println("Erro ao retornar palavra.");

			return null;

		}

	}

	/*
	 * Metodo para verificar se a palavra é maiuscula.
	 */
	public boolean isMaiuscula(char caractere) {

		if (caractere > 64 && caractere < 91) {

			return true;

		}

		return false;

	}

	/*
	 * Metodo para verificar se a palavra é um identificador. As palavras
	 * reservadas são somente com letras maiúsculas
	 */
	public boolean isMinuscula(char caractere) {

		if (caractere > 96 && caractere < 123) {

			return true;

		}

		return false;

	}

	/*
	 * Metodo verifica se a palavra é um numero
	 */
	private boolean isNumerico(char caractere) {

		// Segundo a tabela ASCII 0-9
		if (caractere > 47 && caractere < 58) {

			return true;

		}

		return false;

	}

	/*
	 * Metodo responsável por verificar se a palavra reservada existe
	 */
	private boolean isPalavraReservada(String l) {

		boolean p = palavrasReservadas.containsKey(l);

		return p;

	}

	/*
	 * Metodo que le o simbolo atual
	 */
	private char lerCaractereAtual() {

		caractereAtualLido = codigoFonte.charAt(numeroCaractereAtual);

		this.lerProximoCaractere();

		numeroCaractereAtual++;

		return caractereAtualLido;

	}

	/*
	 * Metodo que lê o lookahead
	 */
	private char lerProximoCaractere() {

		try {

			this.lookAhead = codigoFonte.charAt(numeroCaractereAtual + 1);

		} catch (Exception e) {

			return '$';
		}

		return lookAhead;

	}

	/*
	 * Metodo responsável por ler todo código e devolver uma lista para o parser
	 */
	public List<Token> scanear() {

		ex.erro = "";

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

					ex.excecao(" A palavra esperada não é reservada : ",
							lexema, linha);

					return null;

				} else if (isPalavraReservada(lexema)) {

					Token t = getPalavraReservada(lexema);

					t.setLinhaLocalizada(linha);

					t.setNomeDoToken(lexema);

					listaTokens.add(t);

				} else {

					ex.excecao(" A palavra não é reservada : ", lexema, linha);

					return null;

				}

			} else if (isMinuscula(caractereAtualLido)) {

				// verifica se é um identificador

				lexema = caractereAtualLido + "";

				while (isMinuscula(lookAhead)) {

					lexema = lexema + lookAhead;

					lerCaractereAtual();

				}

				if (!(isMaiuscula(lookAhead)) || !(isNumerico(lookAhead))) {

					Token t = getPalavraReservada("IDENTIFICADOR");

					t.setLinhaLocalizada(linha);

					t.setNomeDoToken(lexema);

					listaTokens.add(t);

				} else {

					ex.excecao("Erro léxico: Era esperado um: ", lexema,
							linha);

					return null;

				}

			} else if (isNumerico(caractereAtualLido)) {

				// Verifica se é um númerico

				lexema = caractereAtualLido + "";

				while (isNumerico(lookAhead)) {

					lexema = lexema + lookAhead;

					lerCaractereAtual();

				}

				if (isMaiuscula(lookAhead) || isMinuscula(lookAhead)) {

					ex.excecao(" A palavra não é um númerico : ", lexema, linha);

					return null;

				} else {

					int v = Integer.parseInt(lexema);

					Numerico n = new Numerico(v, linha);

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

					listaTokens.add(t);

				} else {

					ex.excecao("Era esperado o simbolo : ", "=", linha);

					return null;

				}

			} else if (caractereAtualLido == '<') {

				Token t;

				if (lookAhead == ' ') {

					numeroCaractereAtual++;

					t = getPalavraReservada("<");

					listaTokens.add(t);

				} else if (lookAhead == '=') {

					numeroCaractereAtual++;

					t = getPalavraReservada("<=");
					
					t.setLinhaLocalizada(linha);

					listaTokens.add(t);

				} else {

					ex.excecao(
							" Era esperado o simbolo ou um espaço em branco : ",
							"=", linha);

					return null;

				}

			} else if (caractereAtualLido == '>') {

				Token t;

				if (lookAhead == ' ') {

					numeroCaractereAtual++;

					t = getPalavraReservada(">");
					
					t.setLinhaLocalizada(linha);

					listaTokens.add(t);

				} else if (lookAhead == '=') {

					numeroCaractereAtual++;

					t = getPalavraReservada(">=");
					
					t.setLinhaLocalizada(linha);

					listaTokens.add(t);

				} else {

					ex.excecao(
							" Era esperado o simbolo ou espaço em branco : ",
							"=", linha);

					return null;

				}

			} else if (caractereAtualLido == '=') {

				Token t;

				if (lookAhead == ' ') {

					numeroCaractereAtual++;

					t = getPalavraReservada("=");
					
					t.setLinhaLocalizada(linha);

					listaTokens.add(t);

				} else if (lookAhead == '=') {

					numeroCaractereAtual++;

					t = getPalavraReservada("==");
					
					t.setLinhaLocalizada(linha);

					listaTokens.add(t);

				} else {

					ex.excecao(
							" Era esperado o simbolo ou espaço em branco : ",
							"=", linha);

					return null;

				}

			} else {

				Token t = getPalavraReservada("$");

				listaTokens.add(t);

			}

		}/* while (numeroCaractereAtual < codigoFonte.length()) */

		ex.excecao("Análise léxica aceita.");

		return listaTokens;
	}
}

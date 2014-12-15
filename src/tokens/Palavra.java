package tokens;


/**
 * Classe que gerencia lexemas para palavras reservadas, identificadores e
 * tokens.
 * 
 * @author luciano
 */
public class Palavra extends Token {
	
	public String lex = "";

	public Palavra(String l, int tag, int linha) {

		super(tag, linha);
		
		this.lex = l;

	}
	
	private Palavra(String l, int tag) {

		super(tag, -1);
		
		this.lex = l;

	}
	
	public static final Palavra

	VERDADEIRO = new Palavra("VERDADEIRO", Tag.VERDADEIRO),

	FALSO = new Palavra("FALSO", Tag.FALSO),

	VOID = new Palavra("VOID", Tag.VOID),

	IMPRIME = new Palavra("IMPRIME", Tag.IMPRIME),

	MAIOR_Q = new Palavra(">", Tag.MAIOR_Q),

	MENOR_Q = new Palavra("<", Tag.MENOR_Q),

	DIFERENTE = new Palavra("!=", Tag.DIFERENTE),

	IGUAL = new Palavra("==", Tag.IGUAL),

	MENOR_IGUAL = new Palavra("<=", Tag.MENOR_IGUAL),

	MAIOR_IGUAL = new Palavra(">=", Tag.MAIOR_IGUAL),
	
	ATRIBUICAO = new Palavra("=", Tag.ATRIBUICAO);

	public String toString() {

		return lex;

	}

}

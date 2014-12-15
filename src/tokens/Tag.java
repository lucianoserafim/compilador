package tokens;

/**
 * Classe que define contantes inteiras para os tokens. Valores maior que 255
 * para não haver conflito com valores da tabela ASCII.
 * 
 * @author luciano
 */
public class Tag {

	/**
	 * Constantes definidas para tokens
	 */

	public final static int INICIO = 256, FIM = 257;

	public final static int INTEIRO = 258, BOOLEANO = 259, VOID = 260,
			VERDADEIRO = 261, FALSO = 262;

	public final static int SE = 263, SENAO = 264, PARE = 265, CONTINUE = 266,
			RETORNA = 267, ENQUANTO = 268;

	public final static int IGUAL = 269, DIFERENTE = 270, MENOR_Q = 271,
			MAIOR_Q = 272, MENOR_IGUAL = 273, MAIOR_IGUAL = 274;

	public final static int IMPRIME = 275;

	public final static int BASICO = 276;
	
	public final static int NUMERICO = 277;
	
	public final static int IDENTIFICADOR = 278;
	
	public final static int ATRIBUICAO = 279;
	
	public final static int FINAL = 280;
	
	public final static int FUNCAO = 281;

}

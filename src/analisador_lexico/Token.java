package analisador_lexico;

/**
 * Classe que define os Tokens
 * 
 * @author luciano
 */
public class Token {

	/**
	 * Será utilizado na análise sintática
	 */
	public final int tag;
	
	/**
	 * Linha que indica onde ocorreu erro
	 */
	protected int linhaLocalizada;
	
	/**
	 * Nome do token
	 */
	protected String nomeDoToken;

	public Token(int t, int l) {

		this.tag = t;
		setLinhaLocalizada(l);
		
		if(t < 256){
			
			this.nomeDoToken = (char)t + "";
			
		}

	}

	public int getLinhaLocalizada() {
		return linhaLocalizada;
	}

	public void setLinhaLocalizada(int linhaLocalizada) {
		this.linhaLocalizada = linhaLocalizada;
	}

	public String getNomeDoToken() {
		return nomeDoToken;
	}

	public void setNomeDoToken(String nomeDoToken) {
		this.nomeDoToken = nomeDoToken;
	}

	public int getTag() {
		return tag;
	}

	public String toString() {

		return "" + (char) tag;

	}

}

package tokens;


/**
 * Classe que define tokens numericos
 * 
 * @author luciano
 */
public class Numerico extends Token {

	public final int value;
	
	public Numerico(int v, int l) {

		super(Tag.NUMERICO, l);

		this.value = v;
		
		nomeDoToken = "NUMERICO";

	}

	public String toString() {

		return "" + value;

	}

}

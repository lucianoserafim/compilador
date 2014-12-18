package tokens;


public class Type extends Palavra {
	
	public static final Type INTEIRO = new Type("INTEIRO", -1);
	public static final Type BOOLEANO = new Type("BOOLEANO", -1);
	
	public Type(String palavra, int l){
		
		super(palavra, Tag.BASICO, l);
		
		nomeDoToken = palavra;
		
	}

}

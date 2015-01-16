package excecao;

public class Excecao {

	public static String erro = "";
	
	public static String codigoIntermediario = "";

	public Excecao() {

	}

	public void excecao(String s, String lx, int l) {

		erro = erro + s + lx + " : linha " + l + "\n";

	}

	public void excecao(String s) {

		erro = erro + s + "\n";

	}
	
	public void codigoInt(String s) {

		codigoIntermediario = codigoIntermediario + s + "\n\n";

	}

}

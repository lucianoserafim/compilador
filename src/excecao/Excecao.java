package excecao;

public class Excecao {

	public Excecao() {

	}

	public static String erro = "";

	public void excecao(String s, String lx, int l) {

		erro = erro + s + lx + " : linha " + l + "\n";

		System.out.println(erro);

	}
	
	public void excecao(String s) {

		erro = erro + s + "\n";

		System.out.println(erro);

	}

}

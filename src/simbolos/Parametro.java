package simbolos;

public class Parametro extends Simbolo {

	private String nomeDoTipo;

	public Parametro(int escopo, String tipo, String lexema) {

		super(escopo, null, lexema, null, "PARAMETRO");

		boolean i = tipo.contains("INTEIRO");

		if (i) {

			nomeDoTipo = "INTEIRO";

		} else {

			nomeDoTipo = "BOOLEANO";

		}

	}

	public void setNomeTipo(String n) {

		nomeDoTipo = n;
	}

	@Override
	public String toString() {

		return this.nomeDoTipo + " " + super.getLexema();
	}

}

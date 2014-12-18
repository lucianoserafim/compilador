package simbolos;

public class Parametro extends Simbolo {

	private String nomeDoTipo;

	public Parametro(int e, String t, String l) {

		super(e, null, l, null, "PARAMETRO");

		boolean i = t.contains("INTEIRO");

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

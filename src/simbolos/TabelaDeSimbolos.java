package simbolos;

import java.util.ArrayList;
import java.util.List;

import excecao.Excecao;

public class TabelaDeSimbolos {

	Excecao ex;

	private static TabelaDeSimbolos instance = null;

	private List<Simbolo> listaDeSimbolos;

	public TabelaDeSimbolos() {

		listaDeSimbolos = new ArrayList<Simbolo>();

	}

	public static TabelaDeSimbolos getTabelaDeSimbolos() {

		if (instance == null) {

			instance = new TabelaDeSimbolos();
		}

		return instance;

	}

	public void inserirSimbolo(Simbolo s) {
		
		Simbolo a = retornaVariavel(s);

		listaDeSimbolos.add(s);

	}

	public void atualizarSimbolo(Simbolo s) {
		
		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.getLexema().equals(listaDeSimbolos.get(i).getLexema())
					&& ((listaDeSimbolos.get(i).getEscopo() == (s.getEscopo()) || listaDeSimbolos
							.get(i).getEscopo() == 0))
					&& (s.getClasse().equals("VARIAVEL") || s.getClasse().equals("PARAMETRO"))) {
				
				Simbolo a = retornaVariavel(s);
				
				listaDeSimbolos.remove(i);
				
				a.setValor(s.getValor());
				
				listaDeSimbolos.add(i, a);
				
				break;
				
			}
		}

	}

	public boolean verificarDeclaracaoVariavel(Simbolo s) {

		boolean r = false;

		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.getLexema().equals(listaDeSimbolos.get(i).getLexema())
					&& ((listaDeSimbolos.get(i).getEscopo() == (s.getEscopo()) || listaDeSimbolos
							.get(i).getEscopo() == 0))
					&& s.getClasse().equals("VARIAVEL")) {

				return true;

			}
		}

		return r;
	}

	public Simbolo retornaVariavel(Simbolo s) {

		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.getLexema().equals(listaDeSimbolos.get(i).getLexema())
					&& ((listaDeSimbolos.get(i).getEscopo() == (s.getEscopo()) || listaDeSimbolos
							.get(i).getEscopo() == 0))
					&& (s.getClasse().equals("VARIAVEL") || s.getClasse().equals("PARAMETRO"))) {

				return listaDeSimbolos.get(i);
			}
		}

		return null;
	}

	public Simbolo verificarDeclFuncProced(Simbolo s, int quantParametro) {

		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.getLexema().equals(listaDeSimbolos.get(i).getLexema())
					&& (listaDeSimbolos.get(i).getListaParametros().size() == quantParametro)
					&& (s.getClasse().equals("FUNCAO") || s.getClasse().equals(
							"PROCEDIMENTO"))) {
				return listaDeSimbolos.get(i);
			}
		}

		return null;
	}

	public Simbolo retornaFuncProced(Simbolo s) {

		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.getLexema().equals(listaDeSimbolos.get(i).getLexema())) {
				return listaDeSimbolos.get(i);
			}
		}

		return null;
	}

	public boolean verificarSimbolo(Simbolo s) {

		boolean retorno = false;

		for (int i = 0; i < listaDeSimbolos.size(); i++) {

			if (s.equals(listaDeSimbolos.get(i))) {

				retorno = true;

			}

		}

		return retorno;

	}

	public List<Simbolo> getListaDeSimbolos() {

		return listaDeSimbolos;

	}

}

package codigo_intermediario;

import java.util.ArrayList;
import java.util.List;

import simbolos.Parametro;
import simbolos.Simbolo;
import simbolos.TabelaDeSimbolos;
import tokens.Tag;
import tokens.Token;
import analisador_sintatico.ASintatico;
import excecao.Excecao;

/**
 * Classe responsável pela análise sintática
 * 
 * @author luciano
 *
 */
public class Gerador extends ASintatico {

	public Gerador(List<Token> l) {

		super(l);

	}

	/*
	 * Metodo para passagem dos erros
	 */
	Excecao ex = new Excecao();

	/*
	 * Produção <principal>::=
	 */
	public void principalGerador() {

		ex.erro = "";

		/*
		 * Limpa contadores
		 */
		escopo.clear();
		escopoAtual = 0;
		contaEscopo = 0;

		/*
		 * Verifica se a lista está vazia. Se a lista estiver vazia então o
		 * programa é uma palavra vazia
		 */

		if (listaTokens == null) {

			finalizarPrograma("geração");

			return;

		}

		//TabelaDeSimbolos.getTabelaDeSimbolos().getListaDeSimbolos().clear();

		ex.codigoInt("_Principal:");

		boolean dpf = true;

		while (dpf) {

			dpf = declaracaoProcedFunc();

		}

		if (numeroErro == 0) {

			if (comparaLexema(Tag.INICIO)) {

				ex.codigoInt("_INICIO:");

				consomeToken();

				boolean c = true;

				while (c) {

					c = comeco();

				}

				if (numeroErro == 0) {

					if (comparaLexema(Tag.FIM)) {

						ex.codigoInt("_FIM:");

						consomeToken();

						finalizarPrograma("e geração de código");

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado a palavra reservada FIM depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro de síntese: Análise não aceita.");

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: Era esperado a palavra reservada ",
						"INICIO", listaTokens.get(indiceLista)
								.getLinhaLocalizada());

				return;

			}

		} else {

			ex.excecao("Erro sintático: Análise não aceita.");

			return;

		}

	}// public void principal(){}

	/*
	 * Produção <declara_proced> || <declara_func>
	 */
	private boolean declaracaoProcedFunc() {

		boolean dpf = true;

		if (comparaLexema(Tag.VOID)) {

			declaraProced();

		} else if (comparaLexema(Tag.FUNCAO)) {

			declaraFunc();

		} else if (comparaLexema(Tag.BASICO)) {

			numeroErro++;

			ex.excecao(
					"Erro sintático:",
					" A declaração de variável deve ser feita antes da declaração de funções e procedimentos ",
					listaTokens.get(indiceLista).getLinhaLocalizada());

			dpf = false;

		} else {

			dpf = false;

		}

		return dpf;

	}

	/*
	 * Produção <declara_proced>::=
	 */
	private void declaraProced() {

		String dP = "";

		Simbolo simboloProced = null;

		if (comparaLexema(Tag.VOID)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				simboloProced = new Simbolo(escopoAtual + 1, null, listaTokens
						.get(indiceLista).getNomeDoToken(), null,
						"PROCEDIMENTO");

				String id = listaTokens.get(indiceLista).getNomeDoToken();

				consomeToken();

				if (comparaLexema('(')) {

					consomeToken();

					int i = indiceLista;

					int quantParametros = 0;

					String param = "";

					while (listaTokens.get(i).getTag() != ')') {

						if (listaTokens.get(i).getTag() != ','
								&& listaTokens.get(i).getTag() != Tag.BASICO) {

							param = param + " param "
									+ listaTokens.get(i).getNomeDoToken()
									+ "\n";

							quantParametros++;

						}

						i++;

					}

					ex.codigoInt(param + " " + id + ", " + quantParametros);

					simboloProced.setListaParametros(listaDeParametros());

					if (numeroErro == 0) {

						if (comparaLexema(')')) {

							consomeToken();

							if (comparaLexema('{')) {

								consomeToken();

								boolean c = true;

								while (c) {

									c = comeco();

								}

								if (numeroErro == 0) {

									if (comparaLexema('}')) {

										consomeToken();

									} else {

										numeroErro++;

										ex.excecao(
												"Erro sintático: ",
												"Era esperado um } depois do token "
														+ listaTokens
																.get(indiceLista - 1)
																.getNomeDoToken(),
												(listaTokens
														.get(indiceLista - 1)
														.getLinhaLocalizada()));

										return;

									}

								} else {

									return;

								}

							} else {

								numeroErro++;

								ex.excecao(
										"Erro sintático: ",
										"Era esperado um { depois do token "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								return;

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado um ) depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return;

						}

					} else {

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ( depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

		//TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloProced);

	}

	/*
	 * Produção <declara_func>::=
	 */
	private void declaraFunc() {

		Simbolo simboloFunc = null;

		if (comparaLexema(Tag.FUNCAO)) {

			consomeToken();

			if (comparaLexema(Tag.BASICO)) {

				consomeToken();

				if (comparaLexema(Tag.IDENTIFICADOR)) {

					simboloFunc = new Simbolo(escopoAtual + 1, listaTokens.get(
							indiceLista - 1).getNomeDoToken(), listaTokens.get(
							indiceLista).getNomeDoToken(), null, "FUNCAO");

					String id = listaTokens.get(indiceLista).getNomeDoToken();

					consomeToken();

					if (comparaLexema('(')) {

						consomeToken();

						int i = indiceLista;

						int quantParametros = 0;

						String param = "";

						while (listaTokens.get(i).getTag() != ')') {

							if (listaTokens.get(i).getTag() != ','
									&& listaTokens.get(i).getTag() != Tag.BASICO) {

								param = param + " param "
										+ listaTokens.get(i).getNomeDoToken()
										+ "\n";

								quantParametros++;

							}

							i++;

						}

						ex.codigoInt(param + " " + id + ", " + quantParametros);

						simboloFunc.setListaParametros(listaDeParametros());

						if (numeroErro == 0) {

							if (comparaLexema(')')) {

								consomeToken();

								if (comparaLexema('{')) {

									consomeToken();

									boolean c = true;

									while (c) {

										c = comeco();

									}

									if (numeroErro == 0) {

										if (comparaLexema(Tag.RETORNA)) {

											consomeToken();

											if (comparaLexema('(')) {

												consomeToken();

												retorno();

												if (numeroErro == 0) {

													if (comparaLexema(')')) {

														consomeToken();

														if (comparaLexema(';')) {

															consomeToken();

														} else {

															numeroErro++;

															ex.excecao(
																	"Erro sintático: ",
																	"Era esperado um ; depois do token "
																			+ listaTokens
																					.get(indiceLista - 1)
																					.getNomeDoToken(),
																	(listaTokens
																			.get(indiceLista - 1)
																			.getLinhaLocalizada()));

															return;

														}

													} else {

														numeroErro++;

														ex.excecao(
																"Erro sintático: ",
																"Era esperado um ) depois do token "
																		+ listaTokens
																				.get(indiceLista - 1)
																				.getNomeDoToken(),
																(listaTokens
																		.get(indiceLista - 1)
																		.getLinhaLocalizada()));

														return;

													}

												} else {

													return;

												}

											} else {

												numeroErro++;

												ex.excecao(
														"Erro sintático: ",
														"Era esperado um ( depois do token "
																+ listaTokens
																		.get(indiceLista - 1)
																		.getNomeDoToken(),
														(listaTokens
																.get(indiceLista - 1)
																.getLinhaLocalizada()));

												return;

											}

										} else {

											numeroErro++;

											ex.excecao(
													"Erro sintático: ",
													"Era esperado um retorno depois do token "
															+ listaTokens
																	.get(indiceLista - 1)
																	.getNomeDoToken(),
													(listaTokens
															.get(indiceLista - 1)
															.getLinhaLocalizada()));

											return;

										}

									} else {

										return;

									}

									if (comparaLexema('}')) {

										consomeToken();

									} else {

										numeroErro++;

										ex.excecao(
												"Erro sintático: ",
												"Era esperado um } depois do token "
														+ listaTokens
																.get(indiceLista - 1)
																.getNomeDoToken(),
												(listaTokens
														.get(indiceLista - 1)
														.getLinhaLocalizada()));

										return;

									}

								} else {

									numeroErro++;

									ex.excecao(
											"Erro sintático: ",
											"Era esperado um { depois do token "
													+ listaTokens.get(
															indiceLista - 1)
															.getNomeDoToken(),
											(listaTokens.get(indiceLista - 1)
													.getLinhaLocalizada()));

									return;
								}

							} else {

								numeroErro++;

								ex.excecao(
										"Erro sintático: ",
										"Era esperado um ) depois do token "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								return;

							}

						} else {

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ( depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado a declaração do identificador da função depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado a declaração do tipo de retorno da função depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

		//TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloFunc);

	}

	private void retorno() {

		if (comparaLexema(Tag.FALSO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.NUMERICO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor de retorno depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <lista_param>::=
	 */
	private List<Parametro> listaDeParametros() {

		List<Parametro> listaParametros = new ArrayList<Parametro>();

		if (comparaLexema(Tag.BASICO)) {

			listaParametros.add(parametro());

			if (comparaLexema(',')) {

				listaParametros.add(listaDeParametrosAuxiliar());

			} else {

			}

		} else if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(',')) {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado a declaração de um tipo depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return null;

		} else {

		}

		return listaParametros;

	}

	/*
	 * Produção <lista_param_aux>::=
	 */
	private Parametro listaDeParametrosAuxiliar() {

		Parametro p = null;

		if (comparaLexema(',')) {

			consomeToken();

			p = parametro();

			if (comparaLexema(',')) {

				listaDeParametrosAuxiliar();

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado uma , depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return null;

		}

		return p;

	}

	/*
	 * Produção <parametro>::=
	 */
	private Parametro parametro() {

		Parametro p = null;

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				p = new Parametro(escopoAtual + 1, listaTokens.get(
						indiceLista - 1).getNomeDoToken(), listaTokens.get(
						indiceLista).getNomeDoToken());

				Simbolo v = new Simbolo(p.getEscopo(), listaTokens.get(
						indiceLista - 1).getNomeDoToken(), listaTokens.get(
						indiceLista).getNomeDoToken(), null, "PARAMETRO");

				//TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(v);

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado a declaração de um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return null;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado a declaração de um tipo depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return null;

		}

		return p;

	}

	/*
	 * Produção <valor>::=
	 */
	private void valor(String end) {

		if (comparaLexema(Tag.NUMERICO)) {

			end = end + " " + listaTokens.get(indiceLista).toString();

			consomeToken();

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			end = end + " " + listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				int i = indiceLista + 2;

				int quantParametros = 0;

				String param = "";

				while (listaTokens.get(i).getTag() != ')') {

					if (listaTokens.get(i).getTag() != ',') {

						param = param + " param "
								+ listaTokens.get(i).getNomeDoToken() + "\n";

						quantParametros++;

					}

					i++;

				}

				String ret = chamaFuncaoProced();

				end = param + end + ret;

			} else {

				indiceLista--;

				end = end + " " + listaTokens.get(indiceLista).getNomeDoToken();

				consomeToken();

			}

			// consomeToken();

		} else if (comparaLexema('(')) {

			/*
			 * List<Token> lista1 = new ArrayList<Token>();
			 * 
			 * int i = indiceLista;
			 * 
			 * int quantElementos = 0;
			 * 
			 * while (listaTokens.get(i).getTag() != ';') {
			 * 
			 * if (listaTokens.get(i).getTag() != ')' &&
			 * listaTokens.get(i).getTag() != '(') {
			 * 
			 * lista1.add(listaTokens.get(i));
			 * 
			 * System.out.println(lista1);
			 * 
			 * }
			 * 
			 * i++;
			 * 
			 * }
			 * 
			 * int j = 0;
			 * 
			 * String exp = "";
			 * 
			 * List<String> lista2 = new ArrayList<String>();
			 * 
			 * while (j < lista1.size() - 1) {
			 * 
			 * if (lista1.get(j).getTag() == Tag.IDENTIFICADOR ||
			 * lista1.get(j).getNomeDoToken() == "NUMERICO") {
			 * 
			 * exp = "_t" + variavel + ": " + lista1.get(j).toString();
			 * 
			 * j++;
			 * 
			 * if (lista1.get(j).getTag() == '*' || lista1.get(j).getTag() ==
			 * '+' || lista1.get(j).getTag() == '-' || lista1.get(j).getTag() ==
			 * '/') {
			 * 
			 * exp = exp + " " + lista1.get(j).toString();
			 * 
			 * j++;
			 * 
			 * if (lista1.get(j).getTag() == Tag.IDENTIFICADOR ||
			 * lista1.get(j).getNomeDoToken() == "NUMERICO") {
			 * 
			 * exp = exp + " " + lista1.get(j).toString() + "\n";
			 * 
			 * j++;
			 * 
			 * if (lista1.get(j).getTag() == '*' || lista1.get(j).getTag() ==
			 * '+' || lista1.get(j).getTag() == '-' || lista1.get(j).getTag() ==
			 * '/') {
			 * 
			 * exp = exp + " " + lista1.get(j).toString();
			 * 
			 * j++;
			 * 
			 * }
			 * 
			 * } }
			 * 
			 * }
			 * 
			 * variavel++;
			 * 
			 * lista2.add(exp);
			 * 
			 * end = exp + end;
			 * 
			 * j++;
			 * 
			 * }
			 */

			consomeToken();

			expressaoAritmetica();

			if (numeroErro == 0) {

				if (comparaLexema(')')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

		ex.codigoInt(end);

	}

	/*
	 * Produção <exp_aritmetica>::=
	 */
	private void expressaoAritmetica() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)
				|| comparaLexema('(')) {

			termo();

			if (comparaLexema('+') || comparaLexema('-')) {

				expAritAux();

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor numerico, identificador ou expressão depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <exp_arit_aux>::=
	 */
	private void expAritAux() {

		if (comparaLexema('+') || comparaLexema('-')) {

			consomeToken();

			if (comparaLexema('(') || comparaLexema(Tag.NUMERICO)
					|| comparaLexema(Tag.IDENTIFICADOR)) {

				termo();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor numerico, identificador ou expressão depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

			if (comparaLexema('+') || comparaLexema('-')) {

				expAritAux();

			} else {

			}

		} else {

		}

	}

	/*
	 * Produção <termo>::=
	 */
	private void termo() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)
				|| comparaLexema('(')) {

			fator();

			if (comparaLexema('*') || comparaLexema('/')) {

				termoAux();

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor numerico, identificador ou expressão depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <termoAux>::=
	 */
	private void termoAux() {

		if (comparaLexema('*') || comparaLexema('/')) {

			consomeToken();

			if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

				fator();

				if (comparaLexema('*') || comparaLexema('/')) {

					termoAux();

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor numerico, identificador ou expressão depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um * ou / depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <fator>::=
	 */
	private void fator() {

		if (comparaLexema('(')) {

			consomeToken();

			expressaoAritmetica();

			if (comparaLexema(')')) {

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao(
						"Erro sintático: ",
						"Era esperado um ) depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else if (comparaLexema(Tag.NUMERICO)
				|| comparaLexema(Tag.IDENTIFICADOR)) {

			fatorAux();

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor numerico, identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <fatorAux>::=
	 */
	private void fatorAux() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor numerico, identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <comeco>::=
	 */
	private boolean comeco() {

		boolean com = true;

		if (comparaLexema(Tag.BASICO)) {

			declaraVar();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			atribuirChamarFuncProced();

		} else if (comparaLexema(Tag.SE)) {

			condicional();

		} else if (comparaLexema(Tag.ENQUANTO)) {

			laco();

		} else if (comparaLexema(Tag.IMPRIME)) {

			imprime();

		} else if (comparaLexema('(')) {

			numeroErro++;

			ex.excecao("Erro sintático: ",
					"Era esperado um identificador antes do token "
							+ listaTokens.get(indiceLista).getNomeDoToken(),
					(listaTokens.get(indiceLista).getLinhaLocalizada()));

			com = false;

		} else if (comparaLexema(Tag.ATRIBUICAO)) {

			numeroErro++;

			ex.excecao("Erro sintático: ",
					"Era esperado um identificador antes do token "
							+ listaTokens.get(indiceLista).getNomeDoToken(),
					(listaTokens.get(indiceLista).getLinhaLocalizada()));

			com = false;

		} else if (comparaLexema(Tag.FIM)) {

			com = false;

		} else {

			com = false;

		}

		return com;

	}

	private void imprime() {

		if (comparaLexema(Tag.IMPRIME)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				valorImpressao();

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema(';')) {

							consomeToken();

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado um ; depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao(
						"Erro sintático: ",
						"Era esperado um ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

	private void valorImpressao() {

		if (comparaLexema(Tag.NUMERICO)) {

			consomeToken();

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced();

			} else {

				indiceLista--;

				consomeToken();

			}

		} else if (comparaLexema('(')) {

			consomeToken();

			expressaoAritmetica();

			if (numeroErro == 0) {

				if (comparaLexema(')')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	private void laco() {

		String laco = "";

		if (comparaLexema(Tag.ENQUANTO)) {

			String l1 = label++ + "";

			laco = " _L" + l1 + ": " + "SE";

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				String r1 = condicao();

				String l2 = "";

				l2 = "_L" + label;

				laco = laco + " " + r1 + " goto " + l2;

				++label;

				ex.codigoInt(laco);

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema('{')) {

							consomeToken();

							boolean esc = true;

							while (esc) {

								esc = escopo(l2);

							}

							if (numeroErro == 0) {

								ex.codigoInt("goto _L" + l1);

								ex.codigoInt(l2 + ":");

								++label;

								if (comparaLexema('}')) {

									consomeToken();

								} else {

									numeroErro++;

									ex.excecao(
											"Erro sintático: ",
											"Era esperado o simbolo } depois do token "
													+ listaTokens.get(
															indiceLista - 1)
															.getNomeDoToken(),
											(listaTokens.get(indiceLista - 1)
													.getLinhaLocalizada()));

									return;

								}

							} else {

								return;

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo { depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}
	}

	private boolean escopo(String l2) {

		boolean e = true;

		if (comparaLexema(Tag.SE)) {

			e = condicionalEspecial(l2);

		} else {

			e = comeco();

		}

		return e;
	}

	private boolean condicionalEspecial(String l2) {

		String condEsp = "";

		boolean c = true;

		if (comparaLexema(Tag.SE)) {

			condEsp = "_L" + label++ + ": "
					+ listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				String r1 = condicao();

				condEsp = condEsp + " " + r1 + " goto " + "_L" + label;

				ex.codigoInt(condEsp);

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema('{')) {

							consomeToken();

							boolean esc = true;

							while (esc) {

								esc = escopo("");

							}

							if (numeroErro == 0) {

								if (comparaLexema(Tag.PARE)) {

									ex.codigoInt("goto " + l2);

									consomeToken();

								} else if (comparaLexema(Tag.CONTINUE)) {

									consomeToken();

								} else {

								}

								if (comparaLexema(';')) {

									consomeToken();

								} else {

									numeroErro++;

									ex.excecao(
											"Erro sintático: ",
											"Era esperado o simbolo ; depois do token "
													+ listaTokens.get(
															indiceLista - 1)
															.getNomeDoToken(),
											(listaTokens.get(indiceLista - 1)
													.getLinhaLocalizada()));

									c = false;

								}

							}

							if (comparaLexema('}')) {

								consomeToken();

								senaoEspecial();

							} else {

								numeroErro++;

								ex.excecao(
										"Erro sintático: ",
										"Era esperado o simbolo } depois do token "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								c = false;

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo { depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							c = false;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						c = false;

					}

				} else {

					c = false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				c = false;

			}

		}

		return c;
	}

	private void senaoEspecial() {

		String senaoEsp = "";

		if (comparaLexema(Tag.SENAO)) {

			senaoEsp = "_L" + label + ":";

			ex.codigoInt(senaoEsp);

			consomeToken();

			if (comparaLexema('{')) {

				consomeToken();

				boolean c = true;

				while (c) {

					c = comeco();

				}

				if (numeroErro == 0) {

					if (comparaLexema(Tag.PARE) || comparaLexema(Tag.CONTINUE)) {

						consomeToken();

						if (comparaLexema(';')) {

							consomeToken();

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo ; depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return;

						}

					}

				} else {

					return;

				}

				if (comparaLexema('}')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo } depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else if (comparaLexema(Tag.SE)) {

				condicionalEspecial("");

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo { depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

			return;

		}

	}

	private void declaraVar() {

		Simbolo simboloVariavel = null;

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				simboloVariavel = new Simbolo(escopoAtual, listaTokens.get(
						indiceLista - 1).getNomeDoToken(), listaTokens.get(
						indiceLista).getNomeDoToken(), null, "VARIAVEL");

				//TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloVariavel);

				consomeToken();

				if (comparaLexema(';')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ; depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

			return;

		}

	}

	private void atribuirChamarFuncProced() {

		String cfp = "";

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				int i = indiceLista + 2;

				int quantParametros = 0;

				String param = "";

				while (listaTokens.get(i).getTag() != ')') {

					if (listaTokens.get(i).getTag() != ',') {

						param = param + " param "
								+ listaTokens.get(i).getNomeDoToken() + "\n";

						quantParametros++;

					}

					i++;

				}

				cfp = chamaFuncaoProced();

				ex.codigoInt(param + cfp);

			} else if (comparaLexema(Tag.ATRIBUICAO)) {

				indiceLista--;

				atribuicao();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um = ou ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

	private String chamaFuncaoProced() {

		String cfp = "";

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			String id = listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				int i = indiceLista;

				int quantParametros = 0;

				while (listaTokens.get(i).getTag() != ')') {

					if (listaTokens.get(i).getTag() != ',') {

						quantParametros++;

					}

					i++;

				}

				cfp = " call" + " " + id;

				cfp = cfp + ", " + quantParametros;

				listaArgumentos();

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						/*
						 * if (comparaLexema(';')) {
						 * 
						 * consomeToken();
						 * 
						 * } else {
						 * 
						 * numeroErro++;
						 * 
						 * ex.excecao("Erro sintático: ",
						 * "Era esperado um ; depois do token " +
						 * listaTokens.get(indiceLista - 1) .getNomeDoToken(),
						 * (listaTokens.get(indiceLista - 1)
						 * .getLinhaLocalizada()));
						 * 
						 * return null;
						 * 
						 * }
						 */

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return null;

					}

				} else {

					numeroErro++;

					return null;

				}

			}

		} else {

		}

		return cfp;

	}

	private void listaArgumentos() {

		if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.NUMERICO)
				|| comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			argumentos();

			if (comparaLexema(',')) {

				listaArgumentosAuxiliar();

			} else {

			}

		} else if (comparaLexema(',')) {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um argumento depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		} else {

		}

	}

	private void argumentos() {

		if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.NUMERICO)
				|| comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um argumento depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	private void listaArgumentosAuxiliar() {

		if (comparaLexema(',')) {

			consomeToken();

			argumentos();

			if (comparaLexema(',')) {

				listaArgumentosAuxiliar();

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado uma , depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	private void atribuicao() {

		String end = "";

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			end = end + " " + listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

			if (comparaLexema(Tag.ATRIBUICAO)) {

				end = end + " " + ":=";

				consomeToken();

				valor(end);

				if (numeroErro == 0) {

					if (comparaLexema(';')) {

						consomeToken();

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

	private void condicional() {

		String cond = "";

		if (comparaLexema(Tag.SE)) {

			cond = " _L" + label++ + ": "
					+ listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				String r1 = condicao();

				cond = cond + " " + r1 + " goto " + "_L" + label;

				ex.codigoInt(cond);

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema('{')) {

							consomeToken();

							boolean com = true;

							while (com) {

								com = comeco();

							}

							if (numeroErro == 0) {

								if (comparaLexema('}')) {

									consomeToken();

									senao();

								} else {

									numeroErro++;

									ex.excecao(
											"Erro sintático: ",
											"Era esperado o simbolo } depois do token "
													+ listaTokens.get(
															indiceLista - 1)
															.getNomeDoToken(),
											(listaTokens.get(indiceLista - 1)
													.getLinhaLocalizada()));

									return;

								}

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo { depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

	private String condicao() {

		String cond = "";

		if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			cond = listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			cond = listaTokens.get(indiceLista).getNomeDoToken();

			consomeToken();

		} else if (comparaLexema('(')) {

			cond = expressaoLogica();

			if (numeroErro == 0) {

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma expressão lógica depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return null;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado uma condição depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return null;

		}

		return cond;

	}

	private void senao() {

		String senao = "";

		if (comparaLexema(Tag.SENAO)) {

			consomeToken();

			if (comparaLexema('{')) {

				senao = "_L" + label + ":";

				ex.codigoInt(senao);

				consomeToken();

				boolean c = true;

				while (c) {

					c = comeco();

				}

				if (numeroErro == 0) {

					if (comparaLexema('}')) {

						consomeToken();

						condicional();

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo } depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					return;

				}

			} else if (comparaLexema(Tag.SE)) {

				condicional();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo { depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

	private String expressaoLogica() {

		String cond = "";

		if (comparaLexema('(')) {

			consomeToken();

			if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

				cond = cond + listaTokens.get(indiceLista).getNomeDoToken();

				consomeToken();

				if (comparaLexema(Tag.MENOR_Q)) {

					cond = cond + " > ";

					consomeToken();

				} else if (comparaLexema(Tag.MAIOR_Q)) {

					cond = cond + " < ";

					consomeToken();

				} else if (comparaLexema(Tag.MENOR_IGUAL)) {

					cond = cond + " >= ";

					consomeToken();

				} else if (comparaLexema(Tag.MAIOR_IGUAL)) {

					cond = cond + " <= ";

					consomeToken();

				} else if (comparaLexema(Tag.IGUAL)) {

					cond = cond + " != ";

					consomeToken();

				} else if (comparaLexema(Tag.DIFERENTE)) {

					cond = cond + " == ";

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um operador lógico depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return null;

				}

				if (comparaLexema(Tag.NUMERICO)
						|| comparaLexema(Tag.IDENTIFICADOR)) {

					cond = cond + listaTokens.get(indiceLista).getNomeDoToken();

					consomeToken();

					if (comparaLexema(')')) {

						consomeToken();

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return null;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um valor numerico ou identificador depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return null;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor numerico ou identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return null;

			}

		} else {

		}

		return cond;

	}

}// public class Sintatico{}


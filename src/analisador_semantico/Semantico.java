package analisador_semantico;

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
public class Semantico extends ASintatico {

	public Semantico(List<Token> l) {

		super(l);

	}

	/*
	 * Metodo para passagem dos erros
	 */
	Excecao ex = new Excecao();

	/*
	 * Produção <principal>::=
	 */
	public void principalSemantico() {

		ex.erro = "";

		/*
		 * Verifica se a lista está vazia. Se a lista estiver vazia então o
		 * programa é uma palavra vazia
		 */

		if (listaTokens == null) {

			finalizarPrograma("semântica");

			return;

		}

		TabelaDeSimbolos.getTabelaDeSimbolos().getListaDeSimbolos().clear();

		boolean dpf = true;

		while (dpf) {

			dpf = declaracaoProcedFunc();

		}

		if (numeroErro == 0) {

			if (comparaLexema(Tag.INICIO)) {

				consomeToken();

				boolean c = true;

				while (c) {

					c = comeco();

				}

				if (numeroErro == 0) {

					if (comparaLexema(Tag.FIM)) {

						consomeToken();

						finalizarPrograma("semântica");

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

					ex.excecao("Erro sintático: Análise não aceita.");

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

		} else {

			dpf = false;

		}

		return dpf;

	}

	/*
	 * Produção <declara_proced>::=
	 */
	private void declaraProced() {

		Simbolo simboloProced = null;

		if (comparaLexema(Tag.VOID)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				simboloProced = new Simbolo(escopoAtual + 1, null, listaTokens
						.get(indiceLista).getNomeDoToken(), null,
						"PROCEDIMENTO");

				consomeToken();

				if (comparaLexema('(')) {

					consomeToken();

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

		TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloProced);

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

					consomeToken();

					if (comparaLexema('(')) {

						consomeToken();

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

		TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloFunc);

	}

	private void retorno() {

		if (comparaLexema(Tag.FALSO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.NUMERICO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
					.verificarDeclaracaoVariavel(s);

			if (!b) {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"A variável não foi declarada depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else {

				consomeToken();

			}

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

				p = new Parametro(escopoAtual, listaTokens.get(indiceLista - 1)
						.getNomeDoToken(), listaTokens.get(indiceLista)
						.getNomeDoToken());

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
	private void valor(Simbolo s) {

		if (comparaLexema(Tag.NUMERICO)) {

			if (s.getTipoLexema().equals("INTEIRO")) {

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"Variável do tipo INTEIRO antes do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			if (s.getTipoLexema().equals("INTEIRO")) {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"Variável do tipo INTEIRO antes do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else if (s.getTipoLexema().equals("BOOLEANO")) {

				consomeToken();

			}

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo c = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "");

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced(c);

				if (isProced == 1) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Procedimento não pode ser atribuido depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

				indiceLista--;

				c.setClasse("VARIAVEL");

				boolean d = TabelaDeSimbolos.getTabelaDeSimbolos()
						.verificarDeclaracaoVariavel(c);

				if (!d) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável não declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				String e = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(c).getTipoLexema();

				c.setTipo(e);

				if (s.getTipoLexema().equals(c.getTipoLexema())) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"O tipo da variável "
									+ s.getLexema()
									+ " diferente"
									+ " do tipo da variável "
									+ c.getLexema()
									+ " antes do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

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

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
						indiceLista).getNomeDoToken(), null, "VARIAVEL");

				boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
						.verificarDeclaracaoVariavel(s);

				Simbolo t = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(s);

				if (!b) {

					numeroErro++;

					ex.excecao("Erro semântico: Variável " + s.getLexema(),
							" não declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				} else if (t.getTipoLexema().equals("BOOLEANO")) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável do tipo INTEIRO antes do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			}

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

			Simbolo c = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			boolean v = TabelaDeSimbolos.getTabelaDeSimbolos()
					.verificarDeclaracaoVariavel(c);

			if (!v) {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"Variável não declarada depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else {

				String t = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(c).getTipoLexema();

				c.setTipo(t);

				c.setClasse(null);

				indiceLista++;

			}

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced(c);

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

		if (comparaLexema(Tag.ENQUANTO)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				condicao();

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema('{')) {

							consomeToken();

							boolean esc = true;

							while (esc) {

								esc = escopo();

							}

							if (numeroErro == 0) {

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

	private boolean escopo() {

		boolean e = true;

		if (comparaLexema(Tag.SE)) {

			e = condicionalEspecial();

		} else {

			e = comeco();

		}

		return e;
	}

	private boolean condicionalEspecial() {

		boolean c = true;

		if (comparaLexema(Tag.SE)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				condicao();

				if (numeroErro == 0) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema('{')) {

							consomeToken();

							boolean esc = true;

							while (esc) {

								esc = escopo();

							}

							if (numeroErro == 0) {

								if (comparaLexema(Tag.PARE)
										|| comparaLexema(Tag.CONTINUE)) {

									consomeToken();

									if (comparaLexema(';')) {

										consomeToken();

									} else {

										numeroErro++;

										ex.excecao(
												"Erro sintático: ",
												"Era esperado o simbolo ; depois do token "
														+ listaTokens
																.get(indiceLista - 1)
																.getNomeDoToken(),
												(listaTokens
														.get(indiceLista - 1)
														.getLinhaLocalizada()));

										c = false;

									}

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

		if (comparaLexema(Tag.SENAO)) {

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

				condicionalEspecial();

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

				boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
						.verificarDeclaracaoVariavel(simboloVariavel);

				if (b) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável já declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				} else {

					TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(
							simboloVariavel);

					consomeToken();

				}

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

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "");

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced(s);

			} else if (comparaLexema(Tag.ATRIBUICAO)) {

				indiceLista--;

				atribuicao(s);

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

	private void chamaFuncaoProced(Simbolo s) {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				Simbolo r = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaFuncProced(s);

				if (r != null) {

					s.setTipoRetorno(r.getTipoLexema());

					s.setClasse(r.getClasse());

					if (s.getClasse() == "PROCEDIMENTO") {

						isProced = 1;

					}

					int i = indiceLista;

					int quantParametros = 0;

					while (listaTokens.get(i).getTag() != ')') {

						if (listaTokens.get(i).getTag() != ',') {

							quantParametros++;

						}

						i++;

					}

					Simbolo t = TabelaDeSimbolos.getTabelaDeSimbolos()
							.verificarDeclFuncProced(s, quantParametros);

					if (t == null) {

						numeroErro++;

						ex.excecao("Erro semântico: ",
								"Quantidade de parametros não declarados depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					} else {

						listaArgumentos();

					}

				} else {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Função ou Procedimento não declarada ",
							(listaTokens.get(indiceLista - 1)
									.getLinhaLocalizada()));

					return;

				}

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

					numeroErro++;

					return;

				}

			}

		} else {

		}

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

	private void atribuicao(Simbolo s) {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema(Tag.ATRIBUICAO)) {

				s.setClasse("VARIAVEL");

				boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
						.verificarDeclaracaoVariavel(s);

				if (!b) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável não declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				String t = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(s).getTipoLexema();

				s.setTipo(t);

				consomeToken();

				valor(s);

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

		if (comparaLexema(Tag.SE)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				condicao();

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

	private void condicao() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.FALSO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
					.verificarDeclaracaoVariavel(s);

			if (b) {

				indiceLista++;

			} else {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"Variável não declarada antes do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else if (comparaLexema('(')) {

			expressaoLogica();

			if (numeroErro == 0) {

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma expressão lógica depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado uma condição depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	private void senao() {

		if (comparaLexema(Tag.SENAO)) {

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

	private void expressaoLogica() {

		if (comparaLexema('(')) {

			consomeToken();

			if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

				if (comparaLexema(Tag.MENOR_Q) || comparaLexema(Tag.MAIOR_Q)
						|| comparaLexema(Tag.MENOR_IGUAL)
						|| comparaLexema(Tag.MAIOR_IGUAL)
						|| comparaLexema(Tag.IGUAL)
						|| comparaLexema(Tag.DIFERENTE)) {

					consomeToken();

					if (comparaLexema(Tag.NUMERICO)
							|| comparaLexema(Tag.IDENTIFICADOR)) {
						
						if (comparaLexema(Tag.IDENTIFICADOR)) {

							Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
									indiceLista).getNomeDoToken(), null, "VARIAVEL");

							boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
									.verificarDeclaracaoVariavel(s);

							Simbolo t = TabelaDeSimbolos.getTabelaDeSimbolos()
									.retornaVariavel(s);

							if (!b) {

								numeroErro++;

								ex.excecao("Erro semântico: Variável " + s.getLexema(),
										" não declarada depois do token "
												+ listaTokens.get(indiceLista - 1)
														.getNomeDoToken(), (listaTokens
												.get(indiceLista - 1).getLinhaLocalizada()));

								return;

							} else if (t.getTipoLexema().equals("BOOLEANO")) {

								numeroErro++;

								ex.excecao("Erro semântico: ",
										"Variável do tipo INTEIRO antes do token "
												+ listaTokens.get(indiceLista - 1)
														.getNomeDoToken(), (listaTokens
												.get(indiceLista - 1).getLinhaLocalizada()));

								return;

							}

						}

						consomeToken();

						if (comparaLexema(')')) {

							consomeToken();

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

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um valor numerico ou identificador depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um operador lógico depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor numerico ou identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

		}

	}

}// public class Sintatico{}

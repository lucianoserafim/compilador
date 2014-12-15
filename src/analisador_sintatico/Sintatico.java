package analisador_sintatico;

import java.util.List;

import javax.swing.JOptionPane;

import com.sun.xml.internal.bind.v2.util.FatalAdapter;

import tokens.Tag;
import tokens.Token;
import excecao.Excecao;

/**
 * Classe responsável pela análise sintática
 * 
 * @author luciano
 *
 */
public class Sintatico extends ASintatico {

	public Sintatico(List<Token> l) {

		super(l);

	}

	/*
	 * Metodo para passagem dos erros
	 */
	Excecao ex = new Excecao();

	/*
	 * Metodo <principal>
	 */
	public void principal() {

		ex.erro = "";

		/*
		 * Verifica se a lista está vazia. Se a lista estiver vazia então o
		 * programa é uma palavra vazia
		 */

		if (listaTokens == null) {

			finalizarPrograma();

			return;

		}

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

						finalizarPrograma();

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

			numeroErro++;

			ex.excecao("Erro sintático: Análise não aceita.");

			return;

		}

	}// public void principal(){}

	/*
	 * Metodo de declaração de procedimentos e funções
	 */
	private boolean declaracaoProcedFunc() {

		boolean d = true;

		if (comparaLexema(Tag.VOID)) {

			d = declaracaoProcedimento();

		} else if (comparaLexema(Tag.FUNCAO)) {

			d = declaracaoFuncao();

		} else {

			d = false;

		}

		return d;

	}// declaracaoProcedFunc() {}

	/*
	 * Metodo <declara_proced>
	 */
	private boolean declaracaoProcedimento() {

		if (comparaLexema(Tag.VOID)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

				if (comparaLexema('(')) {

					consomeToken();

					boolean lp = true;

					while (lp) {

						lp = listaParametro();

					}

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

										return true;

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

										return false;

									}

								} else {

									return false;

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

								return false;

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado um ) depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return false;

						}

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ( depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}
		}

		return false;

	}

	private boolean declaracaoFuncao() {

		if (comparaLexema(Tag.FUNCAO)) {

			consomeToken();

			if (comparaLexema(Tag.BASICO)) {

				consomeToken();

				if (comparaLexema(Tag.IDENTIFICADOR)) {

					consomeToken();

					if (comparaLexema('(')) {

						consomeToken();

						boolean lp = true;

						while (lp) {

							lp = listaParametro();

						}

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

											if (valor()) {

												// Continua

											} else {

												numeroErro++;

												ex.excecao(
														"Erro sintático: ",
														"Era esperado um valor de retorno depois do token "
																+ listaTokens
																		.get(indiceLista - 1)
																		.getNomeDoToken(),
														(listaTokens
																.get(indiceLista - 1)
																.getLinhaLocalizada()));

												return false;

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

											return false;

										}

									}

									if (comparaLexema('}')) {

										consomeToken();

										return true;

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

										return false;

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

									return false;
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

								return false;

							}

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ( depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado a declaração do identificador da função depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado a declaração do tipo de retorno da função depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		}
		return false;

	}

	private boolean valorImpressao() {

		boolean v = false;

		if (comparaLexema(Tag.NUMERICO)) {

			consomeToken();

			v = true;

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

			v = true;

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				indiceLista--;

				v = chamaFuncaoProced();

			} else {

				v = true;

			}

		} else {

			v = false;
		}

		return v;

	}

	private boolean valor() {

		boolean v = false;

		if (comparaLexema(Tag.NUMERICO)) {

			consomeToken();

			v = true;

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

			v = true;

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			v = true;

		} else if (comparaLexema('(')) {

			consomeToken();

			expressaoAritmetica();

			if (numeroErro == 0) {

				if (comparaLexema(')')) {

					consomeToken();

					v = true;

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					v = false;

				}

			} else {

				v = false;

			}

		} else {

			v = false;

		}

		return v;

	}// valor() {}

	// ############################################################################

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

	// ################################################################

	private boolean declaraVariavel() {

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

				if (comparaLexema(';')) {

					consomeToken();

					return true;

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ; depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		}
		return true;

	}// declaraVariavel() {}

	private boolean atribuirChamarFuncProced() {

		boolean da = true;

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced();

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

				da = false;

			}

		}

		return da;

	}// atribuirChamarFuncProced() {}

	private boolean chamaFuncaoProced() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				boolean la = true;

				while (la) {

					la = listaArgumentos();

				}

				if (comparaLexema(')')) {

					consomeToken();

					if (comparaLexema(';')) {

						consomeToken();

						return true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado um ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			}

		}

		return true;

	}// chamaFuncaoProced() {}

	private boolean listaArgumentos() {

		Boolean laux = true;

		if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.NUMERICO)
				|| comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

			while (laux) {

				laux = listaArgumentosAuxiliar();

			}

		} else {

			laux = false;

		}

		return laux;
	}

	private boolean listaArgumentosAuxiliar() {

		if (comparaLexema(',')) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.NUMERICO)
					|| comparaLexema(Tag.VERDADEIRO)
					|| comparaLexema(Tag.FALSO)) {

				consomeToken();

				listaArgumentosAuxiliar();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um argumento depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else {

			return false;

		}

		return false;
	}

	private boolean atribuicao() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema(Tag.ATRIBUICAO)) {

				consomeToken();

				if (valor()) {

					if (comparaLexema(';')) {

						consomeToken();

						return true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um valor depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}
			}

		}

		return false;
	}// atribuicao() {}

	/*
	 * Metodo que verifica a lista de parametros
	 */
	private boolean listaParametro() {

		boolean lpaux = true;

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

				while (lpaux) {

					lpaux = listaParametroAuxiliar();

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado a declaração de um parâmetro depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				lpaux = false;

			}

		} else {

			lpaux = true;

		}

		return lpaux;

	}// listaParametro() {}

	private boolean listaParametroAuxiliar() {

		if (comparaLexema(',')) {

			consomeToken();

			listaParametro();

		}

		return false;
	}// listaParametroAuxiliar() {}

	/*
	 * Metodo comeco
	 */
	private boolean comeco() {

		boolean v = true;

		if (comparaLexema(Tag.BASICO)) {

			v = declaraVariavel();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			v = atribuirChamarFuncProced();

		} else if (comparaLexema(Tag.SE)) {

			v = condicional();

		} else if (comparaLexema(Tag.ENQUANTO)) {

			v = laco();

		} else if (comparaLexema(Tag.IMPRIME)) {

			v = imprime();

		} else {

			v = false;

		}

		return v;

	}// comeco() {}

	private boolean imprime() {

		if (comparaLexema(Tag.IMPRIME)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				if (valorImpressao()) {

					if (comparaLexema(')')) {

						consomeToken();

						if (comparaLexema(';')) {

							consomeToken();

							return true;

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo ; depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

							return false;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um valor depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		}

		return true;

	}

	private boolean laco() {

		if (comparaLexema(Tag.ENQUANTO)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				if (condicao()) {

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

									return true;

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

									return false;

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

							return false;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ) depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado uma condição depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}
		}

		return true;

	}// laco() {}

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

				if (condicao()) {

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

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado uma condição depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

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

	}// condicionalEspecial() {}

	private boolean senaoEspecial() {

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

							c = false;

						}

					}

				}

				if (comparaLexema('}')) {

					consomeToken();

					return true;

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo } depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					c = false;

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

				return false;

			}

		} else {

			return true;

		}

		return false;

	}// senaoEspecial() {}

	private boolean condicional() {

		boolean c = true;

		if (comparaLexema(Tag.SE)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				if (condicao()) {

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

									c = false;

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

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado uma condição depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

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

	}// condicional() {}

	private boolean senao() {

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

						return true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo } depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						c = false;

					}

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

				return false;

			}

		} else {

			return true;

		}

		return false;

	}// senao() {}

	private boolean condicao() {

		boolean cond = false;

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.FALSO)) {

			consomeToken();

			cond = true;

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				indiceLista--;

				cond = chamaFuncaoProced();

			} else {

				cond = true;

			}

		} else if (comparaLexema('(')) {

			consomeToken();

			expressaoLogica();

			if (numeroErro == 0) {

				if (comparaLexema(')')) {

					consomeToken();

					cond = true;

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					cond = false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ) depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				cond = false;

			}
		} else {

			cond = false;

		}

		return cond;

	}// condicao() {}

	// ########################################################################################

	/*
	 * <exp_logica>::= (<valor_logico> <op_logico> <valor_logico>)
	 * 
	 * <valor_logico>::= <identificador> | <numerico>
	 * 
	 * <op_logico>::= != | == | > | < | >= | <=
	 */

	private void expressaoLogica() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema(Tag.MENOR_Q) || comparaLexema(Tag.MAIOR_Q)
					|| comparaLexema(Tag.MENOR_IGUAL)
					|| comparaLexema(Tag.IGUAL) || comparaLexema(Tag.DIFERENTE)) {

				consomeToken();

				if (comparaLexema(Tag.NUMERICO)
						|| comparaLexema(Tag.IDENTIFICADOR)) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado um valor numerico ou identificador depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));
					return;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um operador lógico depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));
				return;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um valor numerico ou identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));
			return;

		}

	}

	// ########################################################################################

}// public class Sintatico{}

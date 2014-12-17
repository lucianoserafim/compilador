package analisador_sintatico;

import java.util.List;

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
	 * Produção <principal>::=
	 */
	public void principal() {

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

			dpf = declaracaoVarProcedFunc();

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

						System.out.println("SINTÁTICA : \n");

						int n = 0;

						while (n < listaTokens.size() - 1) {

							System.out.println(listaTokens.get(n)
									.getNomeDoToken());

							n++;

						}

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

			ex.excecao("Erro sintático: Análise não aceita.");

			return;

		}

	}// public void principal(){}

	/*
	 * Produção <declara_proced> || <declara_func>
	 */
	private boolean declaracaoVarProcedFunc() {

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

		if (comparaLexema(Tag.VOID)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

				if (comparaLexema('(')) {

					consomeToken();

					listaParametro();

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

	}

	/*
	 * Produção <declara_func>::=
	 */
	private void declaraFunc() {

		if (comparaLexema(Tag.FUNCAO)) {

			consomeToken();

			if (comparaLexema(Tag.BASICO)) {

				consomeToken();

				if (comparaLexema(Tag.IDENTIFICADOR)) {

					consomeToken();

					if (comparaLexema('(')) {

						consomeToken();

						listaParametro();

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

												valor();

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

	}

	/*
	 * Produção <lista_param>::=
	 */
	private void listaParametro() {

		if (comparaLexema(Tag.BASICO)) {

			parametro();

			if (comparaLexema(',')) {

				listaParametroAuxiliar();

			} else {

			}

		} else if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(',')) {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado a declaração de um tipo depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		} else {

		}

	}

	/*
	 * Produção <parametro>::=
	 */
	private void parametro() {

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado a declaração de um identificador depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado a declaração de um tipo depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return;

		}

	}

	/*
	 * Produção <lista_param_aux>::=
	 */
	private void listaParametroAuxiliar() {

		if (comparaLexema(',')) {

			consomeToken();

			parametro();

			if (comparaLexema(',')) {

				listaParametroAuxiliar();

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

	/*
	 * Produção <valor>::=
	 */
	private void valor() {

		if (comparaLexema(Tag.NUMERICO)) {

			consomeToken();

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

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
					"Era esperado um valor de retorno depois do token "
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

		}else if(comparaLexema(Tag.ATRIBUICAO)){
			
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

				valor();

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

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

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

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			indiceLista++;

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

				return;

			}

		} else {

		}

	}

	private void chamaFuncaoProced() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema('(')) {

				consomeToken();

				listaArgumentos();

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

	private void atribuicao() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			consomeToken();

			if (comparaLexema(Tag.ATRIBUICAO)) {

				consomeToken();

				valor();

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

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				chamaFuncaoProced();

				if (numeroErro == 0) {

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado uma condição depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			} else {

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

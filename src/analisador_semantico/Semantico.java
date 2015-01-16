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

		/*
		 * Limpa o console da aplicação
		 */
		ex.erro = "";
		ex.codigoIntermediario = "";

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

			finalizarPrograma("semântica");

			return;

		}

		/*
		 * Limpa lista de simbolos
		 */
		TabelaDeSimbolos.getTabelaDeSimbolos().getListaDeSimbolos().clear();

		boolean dv = true;

		while (dv) {

			dv = declaraVar();

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

						finalizarPrograma("semântica");

					} else if (comparaLexema(Tag.CONTINUE)
							|| comparaLexema(Tag.PARE)) {

						numeroErro++;

						ex.excecao("104");

						return;

					} else {

						numeroErro++;

						ex.excecao("89");

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro semântico: Análise não aceita.");

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("109");

				return;

			}

		} else {

			ex.excecao("Erro semântico: Análise não aceita.");

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

					Simbolo simb = TabelaDeSimbolos.getTabelaDeSimbolos()
							.verificarDeclFuncProced(simboloProced,
									simboloProced.getListaParametros().size());

					if (simb != null) {

						int j = 0;

						while (j < simboloProced.getListaParametros().size()) {

							if (simboloProced
									.getListaParametros()
									.get(j)
									.getNomeDoTipo()
									.equals(simb.getListaParametros().get(j)
											.getNomeDoTipo())) {

								numeroErro++;

								ex.excecao("Erro semântico: ",
										"Procedimento já declarado ",
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								break;

							} else {

							}

							j++;

						}

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

									} else {

										numeroErro++;

										ex.excecao("203");

										return;

									}

								} else {

									return;

								}

							} else {

								numeroErro++;

								ex.excecao("219");

								return;

							}

						} else {

							numeroErro++;

							ex.excecao("229");

							return;

						}

					} else {

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("245");

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("255");

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

						Simbolo simb = TabelaDeSimbolos
								.getTabelaDeSimbolos()
								.verificarDeclFuncProced(simboloFunc,
										simboloFunc.getListaParametros().size());

						if (simb != null) {

							if (simboloFunc.getTipoLexema().equals(
									simb.getTipoLexema())) {

								int j = 0;

								while (j < simboloFunc.getListaParametros()
										.size()) {

									if (simboloFunc
											.getListaParametros()
											.get(j)
											.getNomeDoTipo()
											.equals(simb.getListaParametros()
													.get(j).getNomeDoTipo())) {

										numeroErro++;

										ex.excecao("Erro semântico: ",
												"Função já declarada ",
												(listaTokens
														.get(indiceLista - 1)
														.getLinhaLocalizada()));

										break;

									} else {

									}

									j++;

								}

							}

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

											if (comparaLexema('(')) {

												consomeToken();

												retorno(simboloFunc);

												if (numeroErro == 0) {

													if (comparaLexema(')')) {

														consomeToken();

														if (comparaLexema(';')) {

															consomeToken();

														} else {

															numeroErro++;

															ex.excecao("342");

															return;

														}

													} else {

														numeroErro++;

														ex.excecao("352");

														return;

													}

												} else {

													return;

												}

											} else {

												numeroErro++;

												ex.excecao("368");

												return;

											}

										} else {

											numeroErro++;

											ex.excecao("378");

											return;

										}

									} else {

										return;

									}

									if (comparaLexema('}')) {

										consomeToken();

									} else {

										numeroErro++;

										ex.excecao("398");

										return;

									}

								} else {

									numeroErro++;

									ex.excecao("408");

									return;
								}

							} else {

								numeroErro++;

								ex.excecao("417");

								return;

							}

						} else {

							return;

						}

					} else {

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("439");

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("449");

				return;

			}

		} else {

		}

		TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(simboloFunc);

	}

	private void retorno(Simbolo s) {

		if (comparaLexema(Tag.FALSO) || comparaLexema(Tag.VERDADEIRO)) {

			consomeToken();

		} else if (comparaLexema(Tag.NUMERICO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo c = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
					.verificarDeclaracaoVariavel(c);

			String t = TabelaDeSimbolos.getTabelaDeSimbolos()
					.retornaVariavel(c).getTipoLexema();

			c.setTipo(t);

			if (!b) {

				numeroErro++;

				ex.excecao("Erro semântico: ",
						"A variável não foi declarada depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else if (s.getTipoLexema().equals(c.getTipoLexema())) {

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao(
						"Erro semântico: ",
						"O retorno da função " + s.getLexema() + " é "
								+ s.getTipoLexema(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

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

			ex.excecao("553");

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

			ex.excecao("590");

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

				TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(v);

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao("629");

				return null;

			}

		} else {

			numeroErro++;

			ex.excecao("639");

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

				s.setValor(listaTokens.get(indiceLista).toString());

				TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(s);

				consomeToken();

			} else {

				numeroErro++;

				ex.excecao("Erro semântico: 666 ",
						"Variável do tipo INTEIRO antes do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			}

		} else if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			if (s.getTipoLexema().equals("INTEIRO")) {

				numeroErro++;

				ex.excecao("Erro semântico: 682 ",
						"Variável do tipo INTEIRO antes do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else if (s.getTipoLexema().equals("BOOLEANO")) {

				s.setValor(listaTokens.get(indiceLista).toString());

				TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(s);

				consomeToken();

			}

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo c = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "");

			indiceLista++;

			if (comparaLexema('(')) {

				indiceLista--;

				String retorno = chamaFuncaoProced(c);

				System.out.println(retorno);

				if (!s.getTipoLexema().equals(retorno)) {

					numeroErro++;

					ex.excecao(
							"Erro semântico: 713 ",
							"Tipo de retorno da função "
									+ retorno
									+ " depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				if (isProcedFunc == 1) {

					numeroErro++;

					ex.excecao("Erro semântico: 713 ",
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

					ex.excecao("Erro semântico: 736 ",
							"Variável não declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				Simbolo e = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(c);

				c.setTipo(e.getTipoLexema());

				c.setValor(e.getValor());

				TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(c);

				if (c.getValor() == null) {

					numeroErro++;

					ex.excecao("Erro semântico: 736 ",
							"Variável "
									+ c.getLexema()
									+ " não inicializada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				if (s.getTipoLexema().equals(c.getTipoLexema())) {

					s.setValor(c.getValor());

					TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(s);

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("Erro semântico: 759 ",
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

			if (!s.getTipoLexema().equals("INTEIRO")) {

				numeroErro++;

				ex.excecao("Erro semântico: 784 ",
						"Variável do tipo INTEIRO antes do token " + " = ",
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return;

			} else {

				s.setValor("exp");

				TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(s);

				expressaoAritmetica();

			}

			if (numeroErro == 0) {

				if (comparaLexema(')')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("804");

					return;

				}

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao("818");

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

			ex.excecao("846");

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

				ex.excecao("872");

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

			ex.excecao("912");

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

				ex.excecao("943");

				return;

			}

		} else {

			numeroErro++;

			ex.excecao("953");

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

				ex.excecao("980");

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

				s.setTipo(t.getTipoLexema());
				
				s.setValor(t.getValor());

				if (s.getValor() == null) {

					numeroErro++;

					ex.excecao("Erro semântico: 736 ",
							"Variável "
									+ s.getLexema()
									+ " não inicializada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

				if (!b) {

					numeroErro++;

					ex.excecao("Erro semântico : Variável " + s.getLexema(),
							" não declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				} else if (t.getTipoLexema().equals("BOOLEANO")) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável "
									+ s.getLexema()
									+ " do tipo BOOLEANO depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

			}

			fatorAux();

		} else {

			numeroErro++;

			ex.excecao("1036");

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

			ex.excecao("1059");

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

			com = false;

		} else if (comparaLexema(Tag.ATRIBUICAO)) {

			numeroErro++;

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

							ex.excecao("1146");

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("1156");

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("1172");

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

					ex.excecao("1257");

					return;

				}

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao("1271");

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

									ex.excecao("1319");

									return;

								}

							} else {

								return;

							}

						} else {

							numeroErro++;

							ex.excecao("1335");

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("1345");

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("1361");

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

										c = false;

									}

								}

							}

							if (comparaLexema('}')) {

								consomeToken();

								senaoEspecial();

							} else {

								numeroErro++;

								c = false;

							}

						} else {

							numeroErro++;

							c = false;

						}

					} else {

						numeroErro++;

						c = false;

					}

				} else {

					c = false;

				}

			} else {

				numeroErro++;

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

							ex.excecao("1525");

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

					ex.excecao("1547");

					return;

				}

			} else if (comparaLexema(Tag.SE)) {

				condicionalEspecial();

			} else {

				numeroErro++;

				ex.excecao("1561");

				return;

			}

		} else {

			return;

		}

	}

	private boolean declaraVar() {

		Simbolo simboloVariavel = null;

		boolean b = true;

		if (comparaLexema(Tag.BASICO)) {

			consomeToken();

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				simboloVariavel = new Simbolo(escopoAtual, listaTokens.get(
						indiceLista - 1).getNomeDoToken(), listaTokens.get(
						indiceLista).getNomeDoToken(), null, "VARIAVEL");

				boolean k = TabelaDeSimbolos.getTabelaDeSimbolos()
						.verificarDeclaracaoVariavel(simboloVariavel);

				if (k) {

					numeroErro++;

					ex.excecao("Erro semântico: ",
							"Variável já declarada depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					b = false;

				} else {

					TabelaDeSimbolos.getTabelaDeSimbolos().inserirSimbolo(
							simboloVariavel);

					consomeToken();

				}

				if (comparaLexema(';')) {

					consomeToken();

				} else {

					numeroErro++;

					ex.excecao("1621");

					b = false;

				}

			} else {

				numeroErro++;

				ex.excecao("1631");

				b = false;

			}

		} else {

			b = false;

		}

		return b;

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

				ex.excecao("1670");

				return;

			}

		} else {

		}

	}

	private String chamaFuncaoProced(Simbolo s) {

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

						isProcedFunc = 1;

					} else if (s.getClasse() == "FUNCAO") {

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

						return null;

					} else {

						s.setListaParametros(listaArgumentos(t));

						int k = 0;

						while (k < s.getListaParametros().size()) {

							Simbolo q0 = TabelaDeSimbolos.getTabelaDeSimbolos()
									.retornaVariavel(
											s.getListaParametros().get(k));
							
							Simbolo q1 = TabelaDeSimbolos.getTabelaDeSimbolos()
									.retornaVariavel(
											t.getListaParametros().get(k));
							
							q1.setValor(q0.getValor());
							
							TabelaDeSimbolos.getTabelaDeSimbolos().atualizarSimbolo(q0);

							k++;

						}

						int j = 0;

						while (j < s.getListaParametros().size()) {

							if (s.getListaParametros()
									.get(j)
									.getNomeDoTipo()
									.equals(r.getListaParametros().get(j)
											.getNomeDoTipo())) {

							} else {

								numeroErro++;

								ex.excecao(
										"Erro semântico: ",
										"Tipo do parâmetro não equivalente "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								return null;

							}

							j++;

						}

					}

				} else {

					numeroErro++;

					ex.excecao("Erro semântico: ", s.getLexema()
							+ " não declarada ", (listaTokens
							.get(indiceLista - 1).getLinhaLocalizada()));

					return null;

				}

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
						 * ex.excecao("1771");
						 * 
						 * return null;
						 * 
						 * }
						 */

					} else {

						numeroErro++;

						ex.excecao("1781");

						return null;

					}

				} else {

					numeroErro++;

					ex.excecao("1791");

					return null;

				}

			}

		} else {

		}

		return s.getTipoRetorno();

	}

	private List<Parametro> listaArgumentos(Simbolo s) {

		List<Parametro> listaParametros = new ArrayList<Parametro>();

		if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.NUMERICO)
				|| comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			listaParametros.add(argumentos(s));

			if (comparaLexema(',')) {

				listaParametros.add(listaArgumentosAuxiliar(s));

			} else {

			}

		} else if (comparaLexema(',')) {

			numeroErro++;

			ex.excecao("1824");

			return null;

		} else {

		}

		return listaParametros;

	}

	private Parametro argumentos(Simbolo s) {

		Parametro p = null;

		if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			p = new Parametro(escopoAtual, "BOOLEANO", listaTokens.get(
					indiceLista).getNomeDoToken());

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo c = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			Simbolo b = TabelaDeSimbolos.getTabelaDeSimbolos().retornaVariavel(
					c);

			if (b == null) {

				numeroErro++;

				ex.excecao("Erro semântico: ", "Variável não declarada "
						+ listaTokens.get(indiceLista).getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return null;

			} else {

				c.setTipo(b.getTipoLexema());

				p = new Parametro(escopoAtual, b.getTipoLexema(), listaTokens
						.get(indiceLista).getNomeDoToken());

				consomeToken();
			}

		} else if (comparaLexema(Tag.NUMERICO)) {

			p = new Parametro(escopoAtual, "INTEIRO", listaTokens.get(
					indiceLista).getNomeDoToken());

			consomeToken();

		} else {

			numeroErro++;

			ex.excecao("1875");

			return null;

		}

		return p;

	}

	private Parametro listaArgumentosAuxiliar(Simbolo s) {

		Parametro p = null;

		if (comparaLexema(',')) {

			consomeToken();

			p = argumentos(s);

			if (comparaLexema(',')) {

				listaArgumentosAuxiliar(s);

			} else {

			}

		} else {

			numeroErro++;

			ex.excecao("1903");

			return null;

		}

		return p;

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

					ex.excecao(
							"Erro semântico: 1985 ",
							"Variável não declarada "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				} else {

					Simbolo t = TabelaDeSimbolos.getTabelaDeSimbolos()
							.retornaVariavel(s);

					s.setTipo(t.getTipoLexema());

					consomeToken();

					valor(s);

				}

				if (numeroErro == 0) {

					if (comparaLexema(';')) {

						consomeToken();

						if (comparaLexema(';')) {

							numeroErro++;

							ex.excecao("2054");

							return;

						} else {

						}

					} else {

						numeroErro++;

						ex.excecao("1960");

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("1976");

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

									ex.excecao("2030");

									return;

								}

							}

						} else {

							numeroErro++;

							ex.excecao("2042");

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("2052");

						return;

					}

				} else {

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("2068");

				return;

			}

		} else {

		}

	}

	private void condicao() {

		if (comparaLexema(Tag.VERDADEIRO) || comparaLexema(Tag.FALSO)) {

			consomeToken();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			Simbolo s = new Simbolo(escopoAtual, null, listaTokens.get(
					indiceLista).getNomeDoToken(), null, "VARIAVEL");

			boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
					.verificarDeclaracaoVariavel(s);

			if (b) {

				Simbolo c = TabelaDeSimbolos.getTabelaDeSimbolos()
						.retornaVariavel(s);

				s.setTipo(c.getTipo());

				if (!s.getTipoLexema().equals("BOOLEANO")) {

					numeroErro++;

					ex.excecao("Erro semântico: ", "Variável " + c.getLexema()
							+ " do tipo " + c.getTipoLexema(), (listaTokens
							.get(indiceLista - 1).getLinhaLocalizada()));

					return;

				}

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

				ex.excecao("2135");

				return;

			}

		} else {

			numeroErro++;

			ex.excecao("2145");

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

						ex.excecao("2183");

						return;

					}

				} else {

					return;

				}

			} else if (comparaLexema(Tag.SE)) {

				condicional();

			} else {

				numeroErro++;

				ex.excecao("2203");

				return;

			}

		} else {

		}

	}

	private void expressaoLogica() {

		if (comparaLexema('(')) {

			consomeToken();

			if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.IDENTIFICADOR)) {

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
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					} else if (t.getTipoLexema().equals("BOOLEANO")) {

						numeroErro++;

						ex.excecao("Erro semântico: ",
								"Variável do tipo INTEIRO antes do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return;

					}

				}

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

							Simbolo s = new Simbolo(escopoAtual, null,
									listaTokens.get(indiceLista)
											.getNomeDoToken(), null, "VARIAVEL");

							boolean b = TabelaDeSimbolos.getTabelaDeSimbolos()
									.verificarDeclaracaoVariavel(s);

							Simbolo t = TabelaDeSimbolos.getTabelaDeSimbolos()
									.retornaVariavel(s);

							if (!b) {

								numeroErro++;

								ex.excecao(
										"Erro semântico: Variável "
												+ s.getLexema(),
										" não declarada depois do token "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								return;

							} else if (t.getTipoLexema().equals("BOOLEANO")) {

								numeroErro++;

								ex.excecao(
										"Erro semântico: ",
										"Variável do tipo INTEIRO antes do token "
												+ listaTokens.get(
														indiceLista - 1)
														.getNomeDoToken(),
										(listaTokens.get(indiceLista - 1)
												.getLinhaLocalizada()));

								return;

							}

						}

						consomeToken();

						if (comparaLexema(')')) {

							consomeToken();

						} else {

							numeroErro++;

							ex.excecao("2293");

							return;

						}

					} else {

						numeroErro++;

						ex.excecao("2303");

						return;

					}

				} else {

					numeroErro++;

					ex.excecao("2313");

					return;

				}

			} else {

				numeroErro++;

				ex.excecao("2323");

				return;

			}

		} else {

		}

	}

}// public class Sintatico{}

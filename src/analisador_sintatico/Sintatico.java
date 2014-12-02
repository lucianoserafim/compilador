package analisador_sintatico;

import java.util.List;

import analisador_lexico.Tag;
import analisador_lexico.Token;
import excessao.Excecao;

/**
 * Classe responsável pela análise sintática
 * 
 * @author luciano
 *
 */
public class Sintatico {

	Excecao ex = new Excecao();

	protected List<Token> listaTokens;

	/*
	 * Contagem de erros
	 */
	private int numeroErro = 0;

	/*
	 * Contrutor
	 */
	public Sintatico(List<Token> l) {

		listaTokens = l;

	}/* public Sintatico(List<Token> l) { */

	/*
	 * Metodo <principal>
	 */
	public void principal() {

		/*
		 * Verifica se a lista está vazia. Se a lista estiver vazia então o
		 * programa é uma palavra vazia
		 */
		if (listaTokens == null) {

			return;

		} else if (comparaLexema(Tag.INICIO)) {

			boolean i = true;

			while (i) {

				i = comeco();

			}

			if (numeroErro == 0) {

				if (comparaLexema(Tag.FIM)) {

					if (comparaLexema('(')) {

						boolean dpf = true;

						while (dpf) {

							dpf = declaraProcedFunc();

						}

						if (comparaLexema(')')) {

							if (comparaLexema(Tag.FINAL) && (numeroErro == 0)) {

								finalizarPrograma();

							} else {

								ex.excecao("Erro sintático: Análise sintática não aceita ");

							}

						} else {

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo ) depois do token "
											+ listaTokens.get(indiceLista - 1)
													.getNomeDoToken(),
									(listaTokens.get(indiceLista - 1)
											.getLinhaLocalizada()));

						}

					} else {

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ( depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

					}

				} else {

					ex.excecao("Erro sintático: ",
							"Era esperado a palavra reservada FIM depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

				}

			} else {

				ex.excecao("Erro sintático: Análise não aceita");

			}

		} else {

			ex.excecao("Erro sintático: Era esperado a palavra reservada INICIO");

			return;

		}

	}/* public void principal() */

	int indiceLista = 0;

	/*
	 * Metodo que verifica se o token existe na lista de tokes. Se existir é
	 * retornado true senão false
	 */
	public boolean comparaLexema(int t) {

		if (!listaTokens.isEmpty()) {

			if (listaTokens.get(indiceLista).tag == t) {

				indiceLista++;

				return true;

			} else {

				return false;

			}

		} else {

			return false;

		}

	}

	/*
	 * Metodo que se é um procedimento ou função
	 */
	private boolean declaraProcedFunc() {

		boolean dpf = true;

		if (comparaLexema(Tag.VOID)) {

			dpf = declaraProcedimento();

		} else if (comparaLexema(Tag.FUNCAO)) {

			dpf = declaraFuncao();

		} else {

			return false;

		}

		return dpf;

	}/* private boolean declaraProcedFunc() */

	/*
	 * Metodo que declara um procedimento
	 */
	private boolean declaraProcedimento() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			if (comparaLexema('(')) {

				boolean lp = true;

				while (lp) {

					lp = listaParametro();

				}

				if (comparaLexema(')')) {

					if (comparaLexema('{')) {

						boolean c = true;

						while (c) {

							c = comeco();

						}

						if (comparaLexema('}')) {

							return true;

						} else {

							erroSintatico("Era esperado um }.");

							return false;

						}

					} else {

						erroSintatico("Era esperado um {.");

						return false;

					}

				} else {

					erroSintatico("Era esperado uma ).");

					return true;

				}

			} else {

				erroSintatico("Era esperado um (.");

				return false;

			}

		} else {

			erroSintatico("Era esperado um identificador.");

			return false;

		}

	}

	/*
	 * Metodo que declara uma função
	 */
	private boolean declaraFuncao() {

		if (comparaLexema(Tag.BASICO)) {

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				if (comparaLexema('(')) {

					boolean lp = true;

					while (lp) {

						lp = listaParametro();

					}

					if (comparaLexema(')')) {

						if (comparaLexema('{')) {

							boolean c = true;

							while (c) {

								c = comeco();

							}

							if (comparaLexema(Tag.RETORNA)) {

								if (valor()) {

									// Continua

								} else {

									erroSintatico("Declarar o retorno.");

									return false;

								}

							} else {

								erroSintatico("Era esperado um retorno.");

								return false;

							}

							if (comparaLexema('}')) {

								return true;

							} else {

								erroSintatico("Era esperado um }.");

								return false;

							}

						} else {

							erroSintatico("Era esperado um {.");

							return false;
						}

					} else {

						erroSintatico("Era esperado um ).");

						return false;

					}

				} else {

					erroSintatico("Era esperado um (.");

					return false;

				}

			} else {

				erroSintatico("Declare o identificador da função.");

				return false;

			}

		} else {

			erroSintatico("Declare o tipo de retorno da função.");

			return false;

		}

	}

	/*
	 * Metodo que verifica a lista de parametros
	 */
	private boolean listaParametro() {

		if (comparaLexema(Tag.BASICO)) {

			if (comparaLexema(Tag.IDENTIFICADOR)) {

				if (comparaLexema(',')) {

					if (comparaLexema(Tag.BASICO)) {

						if (comparaLexema(Tag.IDENTIFICADOR)) {

							listaParametro();

						} else {

							erroSintatico("Declare o parametro.");

							return false;

						}

					} else {

						erroSintatico("Declare o tipo do parametro.");

						return false;

					}

				} else {

					return false;

				}

			} else {

				erroSintatico("Declare o parametro.");

				return false;

			}

		} else {

			return false;

		}

		return false;

	}

	/*
	 * Metodo comeco
	 */
	private boolean comeco() {
		
		System.out.println("comeco()");

		boolean v = true;

		if (comparaLexema(Tag.BASICO)) {

			v = declaraVariavel();

		} else if (comparaLexema(Tag.FUNCAO)) {

			v = chamadaFuncao();

		} else if (comparaLexema(Tag.PROCED)) {

			v = chamadaProcedimento();

		} else if (comparaLexema(Tag.IDENTIFICADOR)) {

			v = atribuicao();

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

	}/* private boolean comeco() */

	/*
	 * Metodo que implementa a produção IMPRIME(<valor>);
	 */
	private boolean imprime() {

		if (comparaLexema('(')) {

			if (valor()) {

				if (comparaLexema(')')) {

					if (comparaLexema(';')) {

						return true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado o simbolo ( depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return false;

		}

	}

	/*
	 * Produção <enquanto>
	 */
	private boolean laco() {

		if (comparaLexema('(')) {

			if (condicao()) {

				if (comparaLexema(')')) {

					if (comparaLexema('{')) {

						boolean es = true;

						while (es) {

							es = escopo();

						}

						if (numeroErro == 0) {

							if (comparaLexema('}')) {

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

						} else {

							numeroErro++;

							return false;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo { depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						return false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					return false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma condição depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado o simbolo ( depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return false;

		}

	}

	/*
	 * Inicio dos métodos que implementa o SE que está dentro de um laço
	 */
	private boolean escopo() {

		boolean es = false;

		if (comparaLexema(Tag.SE)) {

			es = condicional_esp();

		} else {

			es = comeco();

		}

		return es;
	}

	private boolean condicional_esp() {

		boolean ce = true;

		if (comparaLexema('(')) {

			if (condicao()) {

				if (comparaLexema(')')) {

					if (comparaLexema('{')) {

						while (ce) {

							ce = comeco();

						}

						if (numeroErro == 0) {

							if (comparaLexema('}')) {

								if (comparaLexema(Tag.SENAO)) {

									if (comparaLexema('{')) {

										while (ce) {

											ce = comeco();

										}

										while (ce) {

											ce = incondicional();

										}

										if (numeroErro == 0) {

											if (comparaLexema('}')) {

												ce = true;

											} else {

												numeroErro++;

												ex.excecao(
														"Erro sintático: ",
														"Era esperado o simbolo } depois do token "
																+ listaTokens
																		.get(indiceLista - 1)
																		.getNomeDoToken(),
														(listaTokens
																.get(indiceLista - 1)
																.getLinhaLocalizada()));

												ce = false;

											}

										} else {

											numeroErro++;

											ce = false;

										}

									} else if (comparaLexema(Tag.SE)) {

										condicional_esp();

									} else {

										ce = true;

									}

								} else {

									ce = true;

								}

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

								ce = false;

							}

						} else {

							numeroErro++;

							ce = false;

						}

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo { depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						ce = false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					ce = false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma condição depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				ce = false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado o simbolo ( depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			ce = false;

		}

		return ce;

	}/* Fim dos métodos que implementa o SE que está dentro de um <enquanto> */

	private boolean condicional() {

		boolean c = true;

		if (comparaLexema('(')) {

			if (condicao()) {

				if (comparaLexema(')')) {

					if (comparaLexema('{')) {

						while (c) {

							c = comeco();

						}

						if (comparaLexema('}')) {

							if (comparaLexema(Tag.SENAO)) {

								if (comparaLexema('{')) {

									while (c) {

										c = comeco();

									}

									if (comparaLexema('}')) {

										c = true;

									} else {

										numeroErro++;

										ex.excecao(
												"Erro sintático: ",
												"Era esperado o simbolo } depois do token "
														+ listaTokens
																.get(indiceLista - 1)
																.getNomeDoToken(),
												(listaTokens
														.get(indiceLista - 1)
														.getLinhaLocalizada()));

										c = false;

									}

								} else if (comparaLexema(Tag.SE)) {

									c = condicional();

								} else {

									numeroErro++;

									ex.excecao(
											"Erro sintático: ",
											"Era esperado o simbolo { ou SE depois do token "
													+ listaTokens.get(
															indiceLista - 1)
															.getNomeDoToken(),
											(listaTokens.get(indiceLista - 1)
													.getLinhaLocalizada()));

									c = false;

								}

							} else {

								c = true;

							}

						} else {

							numeroErro++;

							ex.excecao("Erro sintático: ",
									"Era esperado o simbolo } depois do token "
											+ listaTokens.get(indiceLista - 1)
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
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						c = false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					c = false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma condição depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				c = false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado o simbolo ( depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			c = false;

		}

		return c;

	}

	private boolean incondicional() {

		if (comparaLexema(Tag.PARE) || comparaLexema(Tag.CONTINUE)) {

			if (comparaLexema(';')) {

				return true;

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ; depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else {

			return false;

		}

	}

	private boolean condicao() {

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.FALSO) || comparaLexema(Tag.IDENTIFICADOR)) {

			return true;

		} else if (comparaLexema(Tag.FUNCAO)) {

			if (chamadaFuncao()) {

				return true;

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado uma condição depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else if (comparaLexema('(')) {

			if (exp_logica()) {

				if (comparaLexema(')')) {

					return true;

				} else {

					return false;

				}

			} else {

				return false;

			}

		} else {

			return false;

		}

	}

	private boolean atribuicao() {

		boolean a = false;

		if (comparaLexema(Tag.ATRIBUICAO)) {

			if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.VERDADEIRO)
					|| comparaLexema(Tag.FALSO)
					|| comparaLexema(Tag.IDENTIFICADOR)) {

				if (comparaLexema(';')) {

					a = true;

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ; depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					a = false;

				}

			} else if (comparaLexema(Tag.FUNCAO)) {

				a = chamadaFuncao();

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado um valor depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				a = false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado o simbolo = depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return false;

		}

		return a;

	}

	private boolean valor() {

		boolean v = true;

		if (comparaLexema(Tag.NUMERICO) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.FALSO) || comparaLexema(Tag.IDENTIFICADOR)) {

			v = true;

		} else if (comparaLexema(Tag.FUNCAO)) {

			v = chamadaFuncao();

		}

		return v;

	}

	private boolean exp_logica() {

		return true;

	}/* private boolean exp_logica() { */

	/*
	 * Metodo responsável pela expressão aritmetica
	 */
	private boolean exp_aritmetica() {

		return false;

	}/* private void exp_aritmetica() { */

	/*
	 * Metodo que implementa uma chamada de procedimento
	 */
	private boolean chamadaProcedimento() {

		boolean p = true;

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			if (comparaLexema('(')) {

				while (p) {

					p = listaArgumentos();

				}

				if (comparaLexema(')')) {

					if (comparaLexema(';')) {

						p = true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						p = false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				p = false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			p = false;

		}

		return p;

	}/* private boolean chamadaProcedimento() { */

	/*
	 * Metodo que verifica a lista de argumentos
	 */
	private boolean listaArgumentos() {

		if (comparaLexema(Tag.IDENTIFICADOR) || comparaLexema(Tag.VERDADEIRO)
				|| comparaLexema(Tag.FALSO) || comparaLexema(Tag.NUMERICO)) {

			if (comparaLexema(',')) {

				// <lista_arg_aux> ::= ,<argumentos><lista_arg_aux> | $
				listaArgumentos();

			} else {

				return false;

			}

		} else {

			return false;

		}

		return false;

	}/* private boolean listaArgumentos() { */

	/*
	 * Metodo implementa uma chamada de função
	 */
	private boolean chamadaFuncao() {

		boolean f = true;

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			if (comparaLexema('(')) {

				while (f) {

					f = listaArgumentos();

				}

				if (comparaLexema(')')) {

					if (comparaLexema(';')) {

						f = true;

					} else {

						numeroErro++;

						ex.excecao("Erro sintático: ",
								"Era esperado o simbolo ; depois do token "
										+ listaTokens.get(indiceLista - 1)
												.getNomeDoToken(), (listaTokens
										.get(indiceLista - 1)
										.getLinhaLocalizada()));

						f = false;

					}

				} else {

					numeroErro++;

					ex.excecao("Erro sintático: ",
							"Era esperado o simbolo ) depois do token "
									+ listaTokens.get(indiceLista - 1)
											.getNomeDoToken(), (listaTokens
									.get(indiceLista - 1).getLinhaLocalizada()));

					f = false;

				}

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ( depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				f = false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			f = false;

		}
		return f;

	}/* private boolean chamadaFuncao() { */

	/*
	 * Metodo implementa a declaração de variável
	 */
	private boolean declaraVariavel() {

		if (comparaLexema(Tag.IDENTIFICADOR)) {

			if (comparaLexema(';')) {

				return true;

			} else {

				numeroErro++;

				ex.excecao("Erro sintático: ",
						"Era esperado o simbolo ; depois do token "
								+ listaTokens.get(indiceLista - 1)
										.getNomeDoToken(),
						(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

				return false;

			}

		} else {

			numeroErro++;

			ex.excecao(
					"Erro sintático: ",
					"Era esperado um Identificador depois do token "
							+ listaTokens.get(indiceLista - 1).getNomeDoToken(),
					(listaTokens.get(indiceLista - 1).getLinhaLocalizada()));

			return false;
		}

	}/* private boolean declaraVariavel() */

	/*
	 * Metodo finaliza o programa caso a análise léxica seja aceita
	 */
	private void finalizarPrograma() {

		ex.excecao("Análise sintática aceita.");

		return;

	}/* private void finalizarPrograma() */

	private void erroSintatico(String e) {

		numeroErro++;

		System.out.println("Erro sintático: " + e);

	}

}/* public class Sintatico { */

package gui;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import simbolos.Simbolo;
import simbolos.TabelaDeSimbolos;
import tokens.Token;
import analisador_lexico.Lexico;
import analisador_semantico.Semantico;
import analisador_sintatico.Sintatico;
import excecao.Excecao;

import javax.swing.JTable;

import java.awt.Color;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = -7004555160270830849L;

	private JPanel contentPane;

	final Button btnLexica;
	final Button btnSintatica;
	final Button btnSemantica;

	JTextPane textPaneListaTokens = new JTextPane();

	private JTable table;

	private List<Token> ltl;

	private List<Token> lts;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		setTitle("Compilador");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPaneCodigo = new JScrollPane();
		scrollPaneCodigo.setBounds(12, 27, 537, 350);
		contentPane.add(scrollPaneCodigo);

		final JTextPane textPaneCodigo = new JTextPane();

		textPaneCodigo
				.setText(""
						+ "VOID testeproced(INTEIRO a, INTEIRO b){\n\n"
						+ "INTEIRO  x;\n\n"
						+ "x = (a + b + (5 * 3));\n\n"
						+ "}\n\n"
						+ "FUNCAO INTEIRO testefuncao(INTEIRO c){\n\nINTEIRO d;\n\nRETORNA (d);\n\n}\n\n"
						+ "INICIO\n\n"
						+ "INTEIRO a;\n\n"
						+ "INTEIRO b;\n\n"
						+ "a = 5;\n\n"
						+ "testefuncao(e);\n\n"
						+ "testeproced(a,k);\n\n"
						+ "SE(a){"
						+ "\n\nINTEIRO idade;\n\n"
						+ "idade = 5;"
						+ "\n\n}"
						+ "SENAO SE(a){\n\n"
						+ "INTEIRO idade;\n\n"
						+ "idade = 5;\n\n"
						+ "IMPRIME(idade);\n\n"
						+ "}\n\n"
						+ "ENQUANTO((a < b)){\n\nSE(FALSO){\n\nBOOLEANO f;\n\n}SENAO SE(a){\n\nf = FALSO;\n\n}\n\n}\n\n"
						+ "testeproced(a,b);" 
						+ "\n\nFIM");

		textPaneCodigo.setBorder(new Linhas());
		scrollPaneCodigo.setViewportView(textPaneCodigo);

		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(12, 405, 776, 131);
		contentPane.add(scrollPaneConsole);

		final JTextPane textPaneConsole = new JTextPane();
		textPaneConsole.setEditable(false);
		scrollPaneConsole.setViewportView(textPaneConsole);

		btnLexica = new Button("Análise léxica");
		btnLexica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				Lexico lex = new Lexico(textPaneCodigo.getText());

				ltl = lex.scanear();

				lts = ltl;

				int i = 0;

				String s = "";

				if (ltl != null) {

					while (i < ltl.size() - 1) {

						s = s + ltl.get(i).toString() + "\n";

						textPaneListaTokens.setText(s);

						i++;

					}

				}

				textPaneConsole.setText(Excecao.erro);

				btnLexica.disable();
				btnSintatica.enable();

			}
		});
		btnLexica.setBounds(796, 542, 194, 48);
		contentPane.add(btnLexica);

		JScrollPane scrollPaneListaTokens = new JScrollPane();
		scrollPaneListaTokens.setBounds(794, 405, 194, 131);
		contentPane.add(scrollPaneListaTokens);
		scrollPaneListaTokens.setViewportView(textPaneListaTokens);

		textPaneListaTokens.setEditable(false);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBounds(12, 0, 537, 25);
		contentPane.add(panel);

		JLabel labelCodigo = new JLabel("Código");
		panel.add(labelCodigo);

		JPanel panelListaTokens = new JPanel();
		FlowLayout fl_panelListaTokens = (FlowLayout) panelListaTokens
				.getLayout();
		fl_panelListaTokens.setAlignment(FlowLayout.LEFT);
		panelListaTokens.setBounds(794, 378, 194, 25);
		contentPane.add(panelListaTokens);

		JLabel labelListaTokens = new JLabel("Lista de Tokens");
		panelListaTokens.add(labelListaTokens);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(12, 378, 776, 25);
		contentPane.add(panel_1);

		JLabel label = new JLabel("Console");
		panel_1.add(label);

		JPanel panelTabelaSimbolos = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panelTabelaSimbolos.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelTabelaSimbolos.setBounds(553, 0, 427, 25);
		contentPane.add(panelTabelaSimbolos);

		JLabel lblTabelaSimbolos = new JLabel("Tabela de simbolos");
		panelTabelaSimbolos.add(lblTabelaSimbolos);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(553, 27, 435, 350);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		btnSintatica = new Button("Análise Sintática");
		btnSintatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				Sintatico sin = new Sintatico(lts);

				sin.principalSintatico();

				int i = 0;

				String s = "";

				if (lts != null) {

					while (i < lts.size() - 1) {

						s = s + lts.get(i).toString() + "\n";

						textPaneListaTokens.setText(s);

						i++;

					}

				}

				textPaneConsole.setText(Excecao.erro);

				btnSintatica.disable();
				btnLexica.disable();
				btnSemantica.enable();

				preencherTabelaSimlobos();

			}
		});
		btnSintatica.setBounds(596, 542, 194, 48);
		btnSintatica.disable();
		contentPane.add(btnSintatica);
		

		btnSemantica = new Button("Análise Semântia");
		btnSemantica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				Semantico sem = new Semantico(lts);

				sem.principalSemantico();

				int i = 0;

				String s = "";

				if (lts != null) {

					while (i < lts.size() - 1) {

						s = s + lts.get(i).toString() + "\n";

						textPaneListaTokens.setText(s);

						i++;

					}

				}

				textPaneConsole.setText(Excecao.erro);

				btnSintatica.disable();
				btnLexica.enable();
				btnSemantica.disable();

				preencherTabelaSimlobos();

			}
		});
		
		btnSemantica.setBounds(396, 542, 194, 48);
		btnSemantica.disable();
		contentPane.add(btnSemantica);

	}

	private void preencherTabelaSimlobos() {

		List<Simbolo> lTS = TabelaDeSimbolos.getTabelaDeSimbolos()
				.getListaDeSimbolos();
		TabelaSimbolosModel tsm = new TabelaSimbolosModel(lTS);
		table.setModel(tsm);

	}
}

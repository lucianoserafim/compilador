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

import codigo_intermediario.Gerador;

import java.awt.Color;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = -7004555160270830849L;

	private JPanel contentPane;

	final Button btnLexica;
	final Button btnSintatica;
	final Button btnSemantica;
	final Button btnGerarCodigo;

	final JTextPane textPaneTresEnd;

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
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 690);
		setTitle("Compilador");
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPaneCodigo = new JScrollPane();
		scrollPaneCodigo.setBounds(12, 27, 537, 350);
		contentPane.add(scrollPaneCodigo);

		final JTextPane textPaneCodigo = new JTextPane();

		textPaneCodigo.setText(""
				+ "VOID testeproced(INTEIRO a, INTEIRO b){\n\n"
				+ "\tINTEIRO  x;\n\n" + "\tx = (a + b + (5 * 3));\n\n"
				+ "}\n\n"
				+ "FUNCAO INTEIRO testefuncao(INTEIRO c){\n\n"
				+ "\tINTEIRO d;\n\n"
				+ "\td = c;\n\n"
				+ "\tRETORNA (d);\n\n}\n\n"
				+ "INICIO\n\n" + "\tINTEIRO a;\n\n"
				+ "\tINTEIRO b;\n\n"
				+ "\ta = 5;\n\n"
				+ "\tb = 10;\n\n"
				+ "\tb = (a + b + (5 * 3));\n\n"
				+ "\tBOOLEANO c;\n\n"
				+ "\tBOOLEANO d;\n\n"
				+ "\tc = FALSO;\n\n"
				+ "\td = VERDADEIRO;\n\n"
				+ "\tINTEIRO e;\n\n"
				+ "\tINTEIRO k;\n\n" + "\tk = 5;\n\n"
				+ "\tINTEIRO r;\n\n"
				+ "\tr = testefuncao(e);\n\n"
				+ "\ttesteproced(a,k)\n\n"
				+ "SE(c){\n\n"
				+ "\tINTEIRO idade;\n\n"
				+ "\tidade = 5;"
				+ "\n\n}"
				+ "SENAO SE(d){\n\n"
				+ "\tINTEIRO idade;\n\n"
				+ "\tidade = 5;\n\n"
				+ "\tIMPRIME(idade);\n\n"
				+ "}\n\n"
				+ "ENQUANTO((a < b)){\n\n"
				+ "\tINTEIRO x;\n\n"
				+ "\tINTEIRO y;\n\n"
				+ "\tx = (x + 1);\n\n"
				+ "\ty = 100;\n\n"
				+ "SE((x == y)){\n\n"
				+ "\tPARE;\n\n}SENAO{\n\n"
				+ "\tCONTINUE;\n\n"
				+ "}\n\n" + "}\n\n"
				+ "\n\nFIM");

		textPaneCodigo.setBorder(new Linhas());
		scrollPaneCodigo.setViewportView(textPaneCodigo);

		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(12, 405, 788, 72);
		contentPane.add(scrollPaneConsole);

		final JTextPane textPaneConsole = new JTextPane();
		textPaneConsole.setEditable(false);
		textPaneConsole.setForeground(Color.RED);
		scrollPaneConsole.setViewportView(textPaneConsole);

		btnLexica = new Button("Análise léxica");
		btnLexica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				textPaneTresEnd.setText(" ");

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
		btnLexica.setBounds(800, 626, 190, 48);
		contentPane.add(btnLexica);

		JScrollPane scrollPaneListaTokens = new JScrollPane();
		scrollPaneListaTokens.setBounds(800, 405, 190, 215);
		contentPane.add(scrollPaneListaTokens);
		scrollPaneListaTokens.setViewportView(textPaneListaTokens);

		textPaneListaTokens.setEditable(false);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBounds(12, 0, 537, 25);
		contentPane.add(panel);

		JLabel labelCodigo = new JLabel("Código");
		panel.add(labelCodigo);

		JPanel panelListaTokens = new JPanel();
		panelListaTokens.setBackground(Color.WHITE);
		FlowLayout fl_panelListaTokens = (FlowLayout) panelListaTokens
				.getLayout();
		fl_panelListaTokens.setAlignment(FlowLayout.LEFT);
		panelListaTokens.setBounds(800, 378, 190, 25);
		contentPane.add(panelListaTokens);

		JLabel labelListaTokens = new JLabel("Lista de Tokens");
		panelListaTokens.add(labelListaTokens);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(12, 378, 788, 25);
		contentPane.add(panel_1);

		JLabel label = new JLabel("Console");
		panel_1.add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.WHITE);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(12, 507, 788, 113);
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
		btnSintatica.setBounds(604, 626, 190, 48);
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
				btnLexica.disable();
				btnSemantica.disable();
				btnGerarCodigo.enable();

				preencherTabelaSimlobos();

			}
		});

		btnSemantica.setBounds(408, 626, 190, 48);
		btnSemantica.disable();
		contentPane.add(btnSemantica);

		btnGerarCodigo = new Button("Gerar código");
		btnGerarCodigo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				textPaneTresEnd.setText(" ");

				Gerador ger = new Gerador(lts);

				ger.principalGerador();

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

				textPaneTresEnd.setText(Excecao.codigoIntermediario);

				btnSintatica.disable();
				btnLexica.enable();
				btnSemantica.disable();
				btnGerarCodigo.disable();

				preencherTabelaSimlobos();

			}
		});
		btnGerarCodigo.setBounds(212, 626, 190, 48);
		btnGerarCodigo.disable();
		contentPane.add(btnGerarCodigo);

		JScrollPane scrollPaneTresEnd = new JScrollPane();
		scrollPaneTresEnd.setBackground(Color.WHITE);
		scrollPaneTresEnd.setBounds(549, 27, 439, 350);
		contentPane.add(scrollPaneTresEnd);

		textPaneTresEnd = new JTextPane();
		scrollPaneTresEnd.setViewportView(textPaneTresEnd);
		textPaneTresEnd.setEditable(false);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_2.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(551, 0, 439, 25);
		contentPane.add(panel_2);

		JLabel label_1 = new JLabel("Código de três endereços");
		panel_2.add(label_1);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_3.setBackground(Color.WHITE);
		panel_3.setBounds(12, 476, 788, 25);
		contentPane.add(panel_3);

		JLabel label_2 = new JLabel("Tabela de simbolos");
		panel_3.add(label_2);

	}

	private void preencherTabelaSimlobos() {

		List<Simbolo> lTS = TabelaDeSimbolos.getTabelaDeSimbolos()
				.getListaDeSimbolos();
		TabelaSimbolosModel tsm = new TabelaSimbolosModel(lTS);
		table.setModel(tsm);

	}
}

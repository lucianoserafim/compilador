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

import tokens.Token;
import analisador_lexico.Lexico;
import analisador_sintatico.Sintatico;
import excecao.Excecao;

public class TelaPrincipal extends JFrame {
	
	private static final long serialVersionUID = -7004555160270830849L;

	private JPanel contentPane;

	private List<Token> lts;

	JTextPane textPaneListaTokens = new JTextPane();

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
		setBounds(100, 100, 800, 600);
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
						+ "VOID testeproced(INTEIRO a, INTEIRO b){\n\n}\n\n"
						+ "FUNCAO INTEIRO testefuncao(INTEIRO c){RETORNA c\n\n}\n\n"
						+ "INICIO\n\n"
						+ "INTEIRO variavel;\n\n"
						+ "variavel = 5;\n\n"
						+ "testefuncao(c);\n\n"
						+ "SE(testefuncao();){\n\nINTEIRO idade;\n\n"
						+ "idade = (1 + 2 + (a - b) - (5 * 3) / c);"
						+ "\n\n}SENAO SE(FALSO){\n\nIMPRIME(idade);\n\n}\n\n"
						+ "ENQUANTO((a < b)){\n\nSE(FALSO){\n\nBOOLEANO f;\n\n}SENAO SE(a){\n\nf = FALSO;\n\n}\n\n}"
						+ "testeproced(a,b);\n\n" + "FIM");

		scrollPaneCodigo.setViewportView(textPaneCodigo);

		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(12, 405, 776, 131);
		contentPane.add(scrollPaneConsole);

		final JTextPane textPaneConsole = new JTextPane();
		textPaneConsole.setEditable(false);
		scrollPaneConsole.setViewportView(textPaneConsole);

		Button btnSintatica = new Button("Sintática");
		btnSintatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				Sintatico sin = new Sintatico(lts);

				sin.principal();

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

			}
		});
		btnSintatica.setBounds(441, 542, 172, 48);
		contentPane.add(btnSintatica);

		Button btnLexica = new Button("Léxica");
		btnLexica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textPaneConsole.setText(" ");

				final Lexico lex = new Lexico(textPaneCodigo.getText());

				final List<Token> lt = lex.scanear();

				lts = lt;

				int i = 0;

				String s = "";

				if (lt != null) {

					while (i < lt.size() - 1) {

						s = s + lt.get(i).toString() + "\n";

						textPaneListaTokens.setText(s);

						i++;

					}

				}

				textPaneConsole.setText(Excecao.erro);

			}
		});
		btnLexica.setBounds(263, 542, 172, 48);
		contentPane.add(btnLexica);

		Button btnSemantica = new Button("Semântica");
		btnSemantica.setBounds(616, 542, 172, 48);
		contentPane.add(btnSemantica);

		JScrollPane scrollPaneListaTokens = new JScrollPane();
		scrollPaneListaTokens.setBounds(551, 27, 237, 376);
		contentPane.add(scrollPaneListaTokens);

		textPaneListaTokens.setEditable(false);
		scrollPaneListaTokens.setViewportView(textPaneListaTokens);

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
		panelListaTokens.setBounds(551, 0, 223, 25);
		contentPane.add(panelListaTokens);

		JLabel labelListaTokens = new JLabel("Lista de Tokens");
		panelListaTokens.add(labelListaTokens);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(12, 378, 537, 25);
		contentPane.add(panel_1);

		JLabel label = new JLabel("Console");
		panel_1.add(label);
	}
}

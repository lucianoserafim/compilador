package gui;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;

import excessao.Excecao;
import analisador_lexico.Lexico;
import analisador_lexico.Token;
import analisador_sintatico.Sintatico;

public class TelaPrincipal extends JFrame {

	private final TextArea textAreaCodigo = new TextArea();

	public final TextArea mostraListaLexema = new TextArea();

	public final TextArea textAreaConsole = new TextArea();

	private static final long serialVersionUID = 6924426300033658579L;

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
		getContentPane().setLayout(null);

		textAreaCodigo.setBounds(10, 10, 615, 309);
		getContentPane().add(textAreaCodigo);

		mostraListaLexema.setBounds(631, 10, 159, 309);
		getContentPane().add(mostraListaLexema);

		Button button = new Button("Compilar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				textAreaConsole.setText("");
				
				Lexico lex = new Lexico(textAreaCodigo.getText());

				List<Token> lt = lex.scanear();

				Sintatico sin = new Sintatico(lt);

				sin.principal();

				int i = 0;

				String s = "";
				
				if(lt != null){
					
					while (i < lt.size() - 1) {

						s = s + lt.get(i).toString() + "\n";

						mostraListaLexema.setText(s);

						i++;

					}
					
				}
				
				textAreaConsole.setText(Excecao.erro);

			}
		});
		button.setBounds(10, 467, 780, 105);
		getContentPane().add(button);

		textAreaConsole.setBounds(10, 325, 780, 136);
		getContentPane().add(textAreaConsole);

	}
}

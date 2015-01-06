package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simbolos.Simbolo;

public class TabelaSimbolosModel extends AbstractTableModel {

	private static final long serialVersionUID = 8253452932821048798L;
	private final List<Simbolo> lista;

	public TabelaSimbolosModel(List<Simbolo> l) {

		this.lista = l;
	}

	public int getRowCount() {

		return lista.size();
		
	}

	@Override
	public int getColumnCount() {

		return 5;
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Simbolo n = lista.get(rowIndex);

		switch (columnIndex) {
		
		case 0:

			return n.getLexema();

		case 1:

			return (n.getEscopo() + "");

		case 2:

			if (n.getTipoLexema() == null) {

				return "---";

			} else {

				return n.getTipoLexema() + "";

			}

		case 3:

			if (n.getListaParametros() == null) {

				return "---";

			} else {

				return (n.getListaParametros());

			}
			
		case 4:
			
			if (n.getClasse() == null) {

				return "---";

			} else {

				return (n.getClasse() + "");

			}

		}

		return null;
		
	}

	@Override
	public String getColumnName(int column) {

		switch (column) {

		case 0:

			return "Lexema";

		case 1:

			return "Escopo";

		case 2:

			return "Tipo";

		case 3:

			return "Lista de Parametros";

		case 4:
			
			return "Classe";
					
		}

		return null;

	}


}

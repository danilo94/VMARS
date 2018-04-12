package mars.venus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.text.GapContent;

import mars.Globals;
import mars.Settings;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.Coprocessor1;
import mars.mips.hardware.InvalidRegisterAccessException;
import mars.mips.hardware.Register;
import mars.mips.hardware.RegisterAccessNotice;
import mars.mips.hardware.RegisterFile;
import mars.simulator.Simulator;
import mars.simulator.SimulatorNotice;
import mars.util.Binary;
import mars.vmips.hardware.VectorRegister;
import mars.vmips.hardware.VectorMips;

public class VectorMipsWindow extends JPanel implements ActionListener,
		Observer {
	private static JTable table;
	private static VectorRegister[] registers;
	private Object[][] tableData;
	private boolean highlighting;
	private int highlightRow;
	private ExecutePane executePane;
	private static final int NAME_COLUMN = 0;
	private static final int SHOW_BYTES = 16;
	private static final int SHOW_HWORDS = 8;
	private static final int SHOW_WORDS = 4;
	private static final int SHOW_DWORDS = 2;
	private static Settings settings;
	private JRadioButton[] showTypes;
	private ButtonGroup buttonGrup;

	public VectorMipsWindow() {
		Simulator.getInstance().addObserver(this);
		settings = Globals.getSettings();
		// Display registers in table contained in scroll pane.
		this.setLayout(new BorderLayout()); // table display will occupy entire
											// width if widened

		table = new MyTippedJTable(new RegTableModel(setupWindow(SHOW_BYTES)));
		table.getColumnModel().getColumn(NAME_COLUMN).setWidth(40);
		table.getColumnModel().getColumn(NAME_COLUMN).setMinWidth(40);
		table.getColumnModel().getColumn(NAME_COLUMN).setMaxWidth(40);
		// Display register values (String-ified) right-justified in mono font
		table.getColumnModel()
				.getColumn(NAME_COLUMN)
				.setCellRenderer(
						new RegisterCellRenderer(
								MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
								SwingConstants.LEFT));

		for (int i = 1; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setWidth(80);
			table.getColumnModel().getColumn(i).setMinWidth(80);
			table.getColumnModel().getColumn(i).setMaxWidth(80);
			table.getColumnModel()
					.getColumn(i)
					.setCellRenderer(
							new RegisterCellRenderer(
									MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
									SwingConstants.RIGHT));

		}
		// Display condition flags in panel below the registers
		JPanel OptionPane = new JPanel(new BorderLayout());
		OptionPane.setToolTipText("fredy vai escrever");
		OptionPane.add(new JLabel("falta ver o nome deixa pro Fredy",
				JLabel.CENTER), BorderLayout.NORTH);
		JPanel Pane = new JPanel(new GridLayout(4, 0));
		this.showTypes = new JRadioButton[4];

		this.showTypes[0] = new JRadioButton("Show Bytes");
		this.showTypes[0].setName("bytes");
		this.showTypes[0].addActionListener(this);
		this.showTypes[0].setBackground(Color.WHITE);
		this.showTypes[0].setToolTipText("Split registers into Bytes");
		this.showTypes[0].setSelected(true);

		this.showTypes[1] = new JRadioButton("Show HalfWords");
		this.showTypes[1].setName("halfwords");
		this.showTypes[1].addActionListener(this);
		this.showTypes[1].setBackground(Color.WHITE);
		this.showTypes[1].setToolTipText("Split registers into HalfWords");

		this.showTypes[2] = new JRadioButton("Show Words");
		this.showTypes[2].setName("words");
		this.showTypes[2].addActionListener(this);
		this.showTypes[2].setBackground(Color.WHITE);
		this.showTypes[2].setToolTipText("Split registers into Words");

		this.showTypes[3] = new JRadioButton("Show DWords");
		this.showTypes[3].setName("dwords");
		this.showTypes[3].addActionListener(this);
		this.showTypes[3].setBackground(Color.WHITE);
		this.showTypes[3].setToolTipText("Split registers into DWords");
		this.buttonGrup = new ButtonGroup();
		this.buttonGrup.add(this.showTypes[0]);
		this.buttonGrup.add(this.showTypes[1]);
		this.buttonGrup.add(this.showTypes[2]);
		this.buttonGrup.add(this.showTypes[3]);

		Pane.add(this.showTypes[0]);
		Pane.add(this.showTypes[1]);
		Pane.add(this.showTypes[2]);
		Pane.add(this.showTypes[3]);

		// Tried to get interior of checkboxes to be white while its label and
		// remaining background stays same background color. Found example
		// like the following on the web, but does not appear to have any
		// affect. Might be worth further study but for now I'll just set
		// background to white. I want white so the checkbox appears
		// "responsive" to user clicking on it (it is responsive anyway but
		// looks
		// dead when drawn in gray.
		// Object saveBG = UIManager.getColor("CheckBox.interiorBackground");
		// UIManager.put("CheckBox.interiorBackground", Color.WHITE);
		// UIManager.put("CheckBox.interiorBackground", saveBG);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		OptionPane.add(Pane, BorderLayout.CENTER);
		this.add(OptionPane, BorderLayout.SOUTH);
		this.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

	}

	/**
	 * Called when user clicks on a condition flag checkbox. Updates both the
	 * display and the underlying Coprocessor 1 flag.
	 * 
	 * @param e
	 *            component that triggered this call
	 */
	public void actionPerformed(ActionEvent e) {
		for (JRadioButton b : this.showTypes) {

			if (b.isSelected()) {
				// update types show
				if (b.getName().equals("bytes")) {
					table.setModel(new RegTableModel(setupWindow(SHOW_BYTES)));
					((MyTippedJTable) table).setColumnToolTips(SHOW_BYTES+1);
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getColumnModel().getColumn(NAME_COLUMN).setWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMinWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMaxWidth(70);
					// Display register values (String-ified) right-justified in
					// mono font
					table.getColumnModel()
							.getColumn(NAME_COLUMN)
							.setCellRenderer(
									new RegisterCellRenderer(
											MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
											SwingConstants.LEFT));

					for (int i = 1; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setWidth(80);
						table.getColumnModel().getColumn(i).setMinWidth(80);
						table.getColumnModel().getColumn(i).setMaxWidth(80);
						table.getColumnModel()
								.getColumn(i)
								.setCellRenderer(
										new RegisterCellRenderer(
												MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
												SwingConstants.RIGHT));

					}
					break;
				} else if (b.getName().equals("halfwords")) {
					table.setModel(new RegTableModel(setupWindow(SHOW_HWORDS)));
					((MyTippedJTable) table).setColumnToolTips(SHOW_HWORDS+1);
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getColumnModel().getColumn(NAME_COLUMN).setWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMinWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMaxWidth(70);
					// Display register values (String-ified) right-justified in
					// mono font
					table.getColumnModel()
							.getColumn(NAME_COLUMN)
							.setCellRenderer(
									new RegisterCellRenderer(
											MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
											SwingConstants.LEFT));

					for (int i = 1; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setWidth(110);
						table.getColumnModel().getColumn(i).setMinWidth(110);
						table.getColumnModel().getColumn(i).setMaxWidth(110);
						table.getColumnModel()
								.getColumn(i)
								.setCellRenderer(
										new RegisterCellRenderer(
												MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
												SwingConstants.RIGHT));

					}
					break;
				} else if (b.getName().equals("words")) {
					table.setModel(new RegTableModel(setupWindow(SHOW_WORDS)));
					((MyTippedJTable) table).setColumnToolTips(SHOW_WORDS+1);
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getColumnModel().getColumn(NAME_COLUMN).setWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMinWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMaxWidth(70);
					// Display register values (String-ified) right-justified in
					// mono font
					table.getColumnModel()
							.getColumn(NAME_COLUMN)
							.setCellRenderer(
									new RegisterCellRenderer(
											MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
											SwingConstants.LEFT));

					for (int i = 1; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setWidth(100);
						table.getColumnModel().getColumn(i).setMinWidth(100);
						table.getColumnModel().getColumn(i).setMaxWidth(100);
						table.getColumnModel()
								.getColumn(i)
								.setCellRenderer(
										new RegisterCellRenderer(
												MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
												SwingConstants.RIGHT));

					}
					break;
				} else if (b.getName().equals("dwords")) {
					table.setModel(new RegTableModel(setupWindow(SHOW_DWORDS)));
					((MyTippedJTable) table).setColumnToolTips(SHOW_DWORDS+1);
					table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					table.getColumnModel().getColumn(NAME_COLUMN).setWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMinWidth(70);
					table.getColumnModel().getColumn(NAME_COLUMN)
							.setMaxWidth(70);
					// Display register values (String-ified) right-justified in
					// mono font
					table.getColumnModel()
							.getColumn(NAME_COLUMN)
							.setCellRenderer(
									new RegisterCellRenderer(
											MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
											SwingConstants.LEFT));

					for (int i = 1; i < table.getColumnCount(); i++) {
						table.getColumnModel()
								.getColumn(i)
								.setCellRenderer(
										new RegisterCellRenderer(
												MonoRightCellRenderer.MONOSPACED_PLAIN_12POINT,
												SwingConstants.RIGHT));

					}
					break;
				}
			}

		}
	}

	/**
	 * Sets up the data for the window.
	 * 
	 * @return The array object with the data for the window.
	 **/

	private Object[][] setupWindow(int form) {
		registers = VectorMips.getRegisters();
		this.highlighting = false;
		tableData = new Object[registers.length][form + 1];
		for (int i = 0; i < registers.length; i++) {
			tableData[i][0] = registers[i].getName();
			switch (form) {
			case SHOW_BYTES:
				byte[] Bvalues = VectorMips.getBytesFromRegister(registers[i]
						.getName());

				for (int j = 0; j < form; j++)
					tableData[i][j + 1] = NumberDisplayBaseChooser.formatNumber(
							Bvalues[j], NumberDisplayBaseChooser.getBase(settings.getDisplayValuesInHex()));
				break;
			case SHOW_DWORDS:
				long[] Dvalues = VectorMips
						.getDoubleWordsFromRegister(registers[i].getName());
				for (int j = 0; j < form; j++)
					tableData[i][j + 1] = NumberDisplayBaseChooser.formatNumber(
							Dvalues[j], NumberDisplayBaseChooser.getBase(settings.getDisplayValuesInHex()));
				break;
			case SHOW_HWORDS:
				short[] Hvalues = VectorMips
						.getHalfWordsFromRegister(registers[i].getName());
				for (int j = 0; j < form; j++)
					tableData[i][j + 1] = NumberDisplayBaseChooser.formatNumber(
							Hvalues[j], NumberDisplayBaseChooser.getBase(settings.getDisplayValuesInHex()));
				break;
			case SHOW_WORDS:
				int[] Wvalues = VectorMips.getWordsFromRegister(registers[i]
						.getName());
				for (int j = 0; j < form; j++)
					tableData[i][j + 1] = NumberDisplayBaseChooser.formatNumber(
							Wvalues[j], NumberDisplayBaseChooser.getBase(settings.getDisplayValuesInHex())); 
				break;

			}
		}
		return tableData;
	}

	/**
	 * Reset and redisplay registers.
	 */
	public void clearWindow() {
		this.clearHighlighting();
		VectorMips.resetRegisters();
		this.updateRegisters(Globals.getGui().getMainPane().getExecutePane()
				.getValueDisplayBase(), table.getColumnCount() - 1);

	}

	/**
	 * Clear highlight background color from any row currently highlighted.
	 */
	public void clearHighlighting() {
		highlighting = false;
		if (table != null) {
			table.tableChanged(new TableModelEvent(table.getModel()));
		}
		highlightRow = -1; // assure highlight will not occur upon re-assemble.
	}

	/**
	 * Refresh the table, triggering re-rendering.
	 */
	public void refresh() {
		if (table != null) {
			table.tableChanged(new TableModelEvent(table.getModel()));
		}
	}

	/**
	 * Redisplay registers using current display number base (10 or 16)
	 */
	public void updateRegisters() {
		updateRegisters(Globals.getGui().getMainPane().getExecutePane()
				.getValueDisplayBase(), table.getColumnCount() - 1);
	}

	/**
	 * Redisplay registers using specified display number base (10 or 16)
	 * 
	 * @param base
	 *            number base for display (10 or 16)
	 */
	private void updateRegisters(int base, int form) {
		registers = VectorMips.getRegisters();
		String[] values = new String[form];
		for (int i = 0; i < registers.length; i++) {
			if (form == SHOW_DWORDS) {
				long[] valuesL = VectorMips
						.getDoubleWordsFromRegister(registers[i].getNumber());
				for (int j = 0; j < SHOW_DWORDS; j++) {
					values[j] = NumberDisplayBaseChooser.formatNumber(
							valuesL[j], base);
				}
			} else if (form == SHOW_WORDS) {
				int[] valuesI = VectorMips.getWordsFromRegister(registers[i]
						.getNumber());
				for (int j = 0; j < SHOW_WORDS; j++) {
					values[j] = NumberDisplayBaseChooser.formatNumber(
							valuesI[j], base);
				}
			} else if (form == SHOW_HWORDS) {

				short[] valuesS = VectorMips
						.getHalfWordsFromRegister(registers[i].getNumber());
				for (int j = 0; j < SHOW_HWORDS; j++) {
					values[j] = NumberDisplayBaseChooser.formatNumber(
							valuesS[j], base);
				}

			} else if (form == SHOW_BYTES) {
				byte[] valuesB = VectorMips.getValues(registers[i].getNumber());
				for (int j = 0; j < SHOW_BYTES; j++) {
					values[j] = NumberDisplayBaseChooser.formatNumber(
							valuesB[j], base);
				}

			}
			updateRegisterValue(registers[i].getNumber(), values);
		}
	}

	/**
	 * This method handles the updating of the GUI. Does not affect actual
	 * register.
	 * 
	 * @param number
	 *            The number of the float register whose display to update.
	 * @param val
	 *            New value.
	 * @param base
	 *            the number base for display (e.g. 10, 16)
	 **/

	private void updateRegisterValue(int number, String[] values) {
		for (int i = 1; i < table.getColumnCount(); i++) {
			((RegTableModel) table.getModel()).setDisplayAndModelValueAt(
					values[i-1], number, i);
		}

	}

	/**
	 * Required by Observer interface. Called when notified by an Observable
	 * that we are registered with. Observables include: The Simulator object,
	 * which lets us know when it starts and stops running A register object,
	 * which lets us know of register operations The Simulator keeps us informed
	 * of when simulated MIPS execution is active. This is the only time we care
	 * about register operations.
	 * 
	 * @param observable
	 *            The Observable object who is notifying us
	 * @param obj
	 *            Auxiliary object with additional information.
	 */
	public void update(Observable observable, Object obj) {
		if (observable == mars.simulator.Simulator.getInstance()) {
			SimulatorNotice notice = (SimulatorNotice) obj;
			if (notice.getAction() == SimulatorNotice.SIMULATOR_START) {
				// Simulated MIPS execution starts. Respond to memory changes if
				// running in timed
				// or stepped mode.
				if (notice.getRunSpeed() != RunSpeedPanel.UNLIMITED_SPEED
						|| notice.getMaxSteps() == 1) {
					VectorMips.addRegistersObserver(this);
					this.highlighting = true;
				}
			} else {
				// Simulated MIPS execution stops. Stop responding.
				VectorMips.deleteRegistersObserver(this);
			}
		} else if (obj instanceof RegisterAccessNotice) {
			// NOTE: each register is a separate Observable
			RegisterAccessNotice access = (RegisterAccessNotice) obj;
			if (access.getAccessType() == AccessNotice.WRITE) {
				// For now, use highlighting technique used by Label Window
				// feature to highlight
				// memory cell corresponding to a selected label. The
				// highlighting is not
				// as visually distinct as changing the background color, but
				// will do for now.
				// Ideally, use the same highlighting technique as for Text
				// Segment -- see
				// AddressCellRenderer class in DataSegmentWindow.java.
				this.highlighting = true;
				this.highlightCellForRegister((VectorRegister) observable);
				Globals.getGui().getRegistersPane().setSelectedComponent(this);
			}
		}
	}

	/**
	 * Highlight the row corresponding to the given register.
	 * 
	 * @param register
	 *            Register object corresponding to row to be selected.
	 */
	private void highlightCellForRegister(VectorRegister register) {
		this.highlightRow = register.getNumber();
		table.tableChanged(new TableModelEvent(table.getModel()));
		/*
		 * int registerColumn = FLOAT_COLUMN; registerColumn =
		 * table.convertColumnIndexToView(registerColumn); Rectangle
		 * registerCell = table.getCellRect(registerRow, registerColumn, true);
		 * // STEP 2: Select the cell by generating a fake Mouse Pressed event
		 * and // explicitly invoking the table's mouse listener. MouseEvent
		 * fakeMouseEvent = new MouseEvent(table, MouseEvent.MOUSE_PRESSED, new
		 * Date().getTime(), MouseEvent.BUTTON1_MASK,
		 * (int)registerCell.getX()+1, (int)registerCell.getY()+1, 1, false);
		 * MouseListener[] mouseListeners = table.getMouseListeners(); for (int
		 * i=0; i<mouseListeners.length; i++) {
		 * mouseListeners[i].mousePressed(fakeMouseEvent); }
		 */
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	private class RegisterCellRenderer extends DefaultTableCellRenderer {
		private Font font;
		private int alignment;

		public RegisterCellRenderer(Font font, int alignment) {
			super();
			this.font = font;
			this.alignment = alignment;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel cell = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			cell.setFont(font);
			cell.setHorizontalAlignment(alignment);

			if (settings.getRegistersHighlighting() && highlighting
					&& row == highlightRow) {
				cell.setBackground(settings
						.getColorSettingByPosition(Settings.REGISTER_HIGHLIGHT_BACKGROUND));
				cell.setForeground(settings
						.getColorSettingByPosition(Settings.REGISTER_HIGHLIGHT_FOREGROUND));
				cell.setFont(settings
						.getFontByPosition(Settings.REGISTER_HIGHLIGHT_FONT));
			} else if (row % 2 == 0) {
				cell.setBackground(settings
						.getColorSettingByPosition(Settings.EVEN_ROW_BACKGROUND));
				cell.setForeground(settings
						.getColorSettingByPosition(Settings.EVEN_ROW_FOREGROUND));
				cell.setFont(settings.getFontByPosition(Settings.EVEN_ROW_FONT));
			} else {
				cell.setBackground(settings
						.getColorSettingByPosition(Settings.ODD_ROW_BACKGROUND));
				cell.setForeground(settings
						.getColorSettingByPosition(Settings.ODD_ROW_FOREGROUND));
				cell.setFont(settings.getFontByPosition(Settings.ODD_ROW_FONT));
			}
			return cell;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////
	// The table model.

	class RegTableModel extends AbstractTableModel {

		final String[] columnNames;
		Object[][] data;

		public RegTableModel(Object[][] d) {
			data = d;
			String[] colums = new String[data[0].length];
			colums[0] = "Name";
			String tipo = "";
			if (colums.length == 17)
				tipo = "Byte";
			else if (colums.length == 9)
				tipo = "HafWord";
			else if (colums.length == 5)
				tipo = "Word";
			else if (colums.length == 3)
				tipo = "DWord";

			for (int i = 1; i < data[0].length; i++) {
				colums[i] = "Value " + tipo + "[" + (i-1) + "]";
			}
			this.columnNames = colums;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Float column and even-numbered rows of double column are editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			return (col > 0);
		}

		/*
		 * Update cell contents in table model. This method should be called
		 * only when user edits cell, so input validation has to be done. If
		 * value is valid, MIPS register is updated.
		 */
		public void setValueAt(Object value, int row, int col) {
		    
		     int valueBase = Globals.getGui().getMainPane().getExecutePane().getValueDisplayBase();
	            try {
	            	switch( data[row].length-1){
	            	case SHOW_BYTES:
	            		byte[] valuesB = VectorMips.getBytesFromRegister(registers[row].getName());
	            		valuesB[col-1] = (byte) Binary.stringToInt((String) value);
	    	            synchronized (Globals.memoryAndRegistersLock) {
	    	               VectorMips.setRegisterToBytes(row, valuesB);
	    	            }
	    	            data[row][col] = NumberDisplayBaseChooser.formatNumber(valuesB[col-1], valueBase); 
	            		break;
	            	case SHOW_HWORDS:
	            		short[] valuesH = VectorMips.getHalfWordsFromRegister(registers[row].getName());
	            		valuesH[col-1] = (short) Binary.stringToInt((String) value);
	    	            synchronized (Globals.memoryAndRegistersLock) {
	    	               VectorMips.setRegisterToHalfWords(row, valuesH);
	    	            }
	    	            data[row][col] = NumberDisplayBaseChooser.formatNumber(valuesH[col-1], valueBase); 
	            		break;
	            	case SHOW_WORDS:
	            		int[] valuesW = VectorMips.getWordsFromRegister(registers[row].getName());
	            		valuesW[col-1] = (int) Binary.stringToInt((String) value);
	    	            synchronized (Globals.memoryAndRegistersLock) {
	    	               VectorMips.setRegisterToWords(row, valuesW);
	    	            }
	    	            data[row][col] = NumberDisplayBaseChooser.formatNumber(valuesW[col-1], valueBase); 
	            		break;
	            	case SHOW_DWORDS:
	            		long[] valuesD = VectorMips.getDoubleWordsFromRegister(registers[row].getName());
	            		valuesD[col-1] = (long) Binary.stringToInt((String) value);
	    	            synchronized (Globals.memoryAndRegistersLock) {
	    	               VectorMips.setRegisterToDoubleWords(row, valuesD);
	    	            }
	    	            data[row][col] = NumberDisplayBaseChooser.formatNumber(valuesD[col-1], valueBase); 
	            		break;
	            	}
	            }
	            catch (NumberFormatException nfe) {
	                  data[row][col] = "INVALID";
	                  fireTableCellUpdated(row, col);
	                  return;
	            }
	            fireTableCellUpdated(row, col);
		}

		/**
		 * Update cell contents in table model. Does not affect MIPS register.
		 */
		private void setDisplayAndModelValueAt(Object value, int row, int col) {
			data[row][col] = value;
            fireTableCellUpdated(row, col);
		}

	}

	// /////////////////////////////////////////////////////////////////
	//
	// JTable subclass to provide custom tool tips for each of the
	// register table column headers and for each register name in
	// the first column. From Sun's JTable tutorial.
	// http://java.sun.com/docs/books/tutorial/uiswing/components/table.html
	//
	private class MyTippedJTable extends JTable {

		private String[] regToolTips;
		private String[] columnToolTips;

		MyTippedJTable(RegTableModel m) {
			super(m);
			this.setRowSelectionAllowed(true); // highlights background color of
												// entire row
			this.setSelectionBackground(Color.BLUE);
			regToolTips = new String[32];
			for (int i = 0; i < 32; i++) {
				regToolTips[i] = "temporary (not preserved across call)";
			}
			columnToolTips = new String[m.data[0].length];
			columnToolTips[0] = "Each register has a tool tip describing its usage convention";
			for (int i = 1; i < columnToolTips.length; i++) {
				columnToolTips[i] = "Current "+(128/(columnToolTips.length-1))+" bit value";
			}
		}

		// Implement table cell tool tips.
		public String getToolTipText(MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = rowAtPoint(p);
			int colIndex = columnAtPoint(p);
			int realColumnIndex = convertColumnIndexToModel(colIndex);
			if (realColumnIndex == NAME_COLUMN) {
				tip = regToolTips[rowIndex];
				/*
				 * You can customize each tip to encorporiate cell contents if
				 * you like: TableModel model = getModel(); String regName =
				 * (String)model.getValueAt(rowIndex,0); ....... etc .......
				 */
			} else {
				// You can omit this part if you know you don't have any
				// renderers that supply their own tool tips.
				tip = super.getToolTipText(e);
			}
			return tip;
		}
	     //Implement table header tool tips. 
        protected JTableHeader createDefaultTableHeader() {
          return 
              new JTableHeader(columnModel) {
                 public String getToolTipText(MouseEvent e) {
                   String tip = null;
                   java.awt.Point p = e.getPoint();
                   int index = columnModel.getColumnIndexAtX(p.x);
                   int realIndex = columnModel.getColumn(index).getModelIndex();
                   return columnToolTips[realIndex];
                }
             };
       }
    	public void setColumnToolTips(int numColumns) {
			columnToolTips = new String[numColumns];
			columnToolTips[0] = "Each register has a tool tip describing its usage convention";
			for (int i = 1; i < columnToolTips.length; i++) {
				
				columnToolTips[i] = "Current "+(128/(columnToolTips.length-1))+" bit value";
			}
		}

	}

}

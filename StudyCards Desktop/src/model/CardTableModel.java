 /* Copyright 2012 Kristofer Mitchell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/

package model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public 
class CardTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -52400586421889313L;
	public static final int QUESTION_COL = 0;
	public static final int ANSWER_COL = 1;
	public static final int SCORE_COL = 2;
	
	private String[] columnNames = { "Question", "Answer", "Score"};
	private ArrayList<Card> data;

	public CardTableModel(){
		data = new ArrayList<Card>();
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
			
		switch (col){
		case QUESTION_COL:
			return data.get(row).getFront();
			
		case ANSWER_COL:
			return data.get(row).getBack();
			
		case SCORE_COL:
			if (data.get(row).getScore() == -1)
				return "";
			return String.format("%04.2f%%", data.get(row).getPercentage());
			
		}
		
		return null;
	}

	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public void setValueAt(Object value, int row, int col) {
		
		while (row >= data.size())
			addEmptyRow();
		
		switch (col){
		case QUESTION_COL:
			data.get(row).setFront((String) value);
			break;
		case ANSWER_COL:
			data.get(row).setBack((String) value);
			break;
		case SCORE_COL:
			break;
		}
		
		fireTableCellUpdated(row, col);

		
	}
	
	public void addEmptyRow() {
         data.add(new Card());
         fireTableRowsInserted(
            data.size() - 1,
            data.size() - 1);
     }

	public void setCard(Card card, int caret) {
		
		while(caret >= data.size())
			addEmptyRow();
		
		data.set(caret, card);
		
		for (int i = 0; i < this.columnNames.length; i++)
			fireTableCellUpdated(caret,i);
	}

	public void clear() {
		data.clear();
		fireTableDataChanged();
	}

	public static boolean belowThreshold(String str) {
		
		return false;
	}


}
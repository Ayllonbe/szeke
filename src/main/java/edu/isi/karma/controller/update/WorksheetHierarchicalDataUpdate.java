/**
 * 
 */
package edu.isi.karma.controller.update;

import java.io.PrintWriter;

import edu.isi.karma.view.VWorksheet;
import edu.isi.karma.view.VWorkspace;
import edu.isi.karma.view.tabledata.VDCell;

/**
 * @author szekely
 * 
 */
public class WorksheetHierarchicalDataUpdate extends AbstractUpdate {

	private final VWorksheet vWorksheet;

	/**
	 * The types of cells in the display of data.
	 * 
	 */
	public enum CellType {
		content("c"), // A cell containing a value.
		columnSpace("cs"), // A cell with the space between adjacent cells in a
							// row when space is needed for nested tables.
		dummyContent("_"), // A cell below a content cell when one is needed.
		rowSpace("rs") // A cell for space between rows.
		;

		private String code;

		private CellType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public enum JsonKeys {
		worksheetId, rows, hTableId,
		//
		rowCells,
		//
		cellType, fillId, topBorder, leftBorder, rightBorder,
		// row types
		rowType/* key */, separatorRow, contentRow,
		// for content cells
		value, status, attr
	}

	public WorksheetHierarchicalDataUpdate(VWorksheet vWorksheet) {
		super();
		this.vWorksheet = vWorksheet;
	}

	@Override
	public void generateJson(String prefix, PrintWriter pw,
			VWorkspace vWorkspace) {
		vWorksheet.generateWorksheetHierarchicalDataJson(pw, vWorkspace);
	}

	public static String getStrokePositionKey(VDCell.Position position) {
		return position.name() + "Stroke";
	}

}

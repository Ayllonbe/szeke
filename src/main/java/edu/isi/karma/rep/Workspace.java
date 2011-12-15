/**
 * 
 */
package edu.isi.karma.rep;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.isi.karma.controller.history.CommandHistory;

/**
 * Contains all the data to support a single instance of Karma, called a
 * workspace.
 * 
 * @author szekely
 * 
 */
public class Workspace extends Entity {

	/**
	 * Keep a copy of RepFactor for convenience.
	 */
	private final RepFactory factory;

	/**
	 * History of commands performed in this workspace.
	 */
	private final CommandHistory commandHistory = new CommandHistory();

	/**
	 * Record all the worksheets defined in this workspace.
	 */
	private final Map<String, Worksheet> worksheets = new HashMap<String, Worksheet>();

	/**
	 * In the future we may need to keep track of user info.
	 */
	protected Workspace(String id, RepFactory factory) {
		super(id);
		this.factory = factory;
	}

	public CommandHistory getCommandHistory() {
		return commandHistory;
	}

	void addWorksheet(Worksheet worksheet) {
		worksheets.put(worksheet.getId(), worksheet);
	}

	public Worksheet getWorksheet(String id) {
		return worksheets.get(id);
	}

	public Collection<Worksheet> getWorksheets() {
		return worksheets.values();
	}

	public RepFactory getFactory() {
		return factory;
	}
}

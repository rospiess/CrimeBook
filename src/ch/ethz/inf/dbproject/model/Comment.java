package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a user comment.
 */
public class Comment {

	private final String username;
	private final String comment;
	private final int idnote;
	
	public Comment(final String username, final String comment, final int idnote) {
		this.username = username;
		this.comment = comment;
		this.idnote = idnote;
	}
	
	public Comment(final ResultSet rs) throws SQLException{
		this.username = "not working yet";//rs.getString("username");
		this.comment = rs.getString("text");
		this.idnote = rs.getInt("nr");
	}

	public String getUsername() {
		return username;
	}

	public String getComment() {
		return comment;
	}	
	
	public int getIdnote() {
		return idnote;
	}
}

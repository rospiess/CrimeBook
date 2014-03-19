package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Object that represents a conviction.
 */
public class Conviction {

	private final int idcon;
	private final Date date;
	private final Date enddate;
	private final String type;
	private final int idcase;
	private final int idpoi;

	public Date getDate() {
		return date;
	}

	public Date getEnddate() {
		return enddate;
	}

	public String getType() {
		return type;
	}
	
	public int getIdcase() {
		return idcase;
	}

	
	public int getIdcon() {
		return idcon;
	}

	public int getIdpoi() {
		return idpoi;
	}

	public Conviction(final int idcon, final Date date, final Date enddate, final String type, final int idcase, final int idpoi) {
		this.idcon = idcon;
		this.date = date;
		this.enddate = enddate;
		this.type = type;
		this.idcase = idcase;
		this.idpoi = idpoi;
	}
	
	public Conviction(final ResultSet rs) throws SQLException {
		this.idcon = rs.getInt("idconviction");
		this.date = rs.getDate("begindate");
		this.enddate = rs.getDate("enddate");
		this.type = rs.getString("type");
		this.idcase = rs.getInt("idcase");
		this.idpoi = rs.getInt("idpersonofinterest");
	}

		
}

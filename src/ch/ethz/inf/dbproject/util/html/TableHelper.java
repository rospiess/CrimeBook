package ch.ethz.inf.dbproject.util.html;

public final class TableHelper extends HtmlHelperIface {
	
	private final Object[] headersTopRow;
	private final Object[] headersLeftRow;
	private final Object[][] contents;
	
	public TableHelper(
		final String tableId,
		final String tableClass,
		final Object[] headersTopRow, 
		final Object[] headersLeftRow,
		final Object[][] contents
	) {
		super();
		this.headersTopRow = headersTopRow;
		this.headersLeftRow = headersLeftRow;
		this.contents = contents;

		if (this.headersLeftRow != null && 
			this.contents.length != this.headersLeftRow.length
		) {
			throw new RuntimeException(
				"Headers Left Row is not the same size as the number of rows in contents");
		}
		if (this.contents.length > 0 && 
			this.headersTopRow.length != this.contents[0].length
		) {
			throw new RuntimeException(
				"Headers Top Row is not the same size as the number of columns in contents");
		}
		
	}

	@Override
	public final String generateHtmlCode() {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append("<!-- TableHelper Generated Code -->\n");
		sb.append("<table>\n");
		
		if (this.headersTopRow != null) {
			sb.append("\t<tr>\n");
			if (this.headersLeftRow != null) {
				sb.append("\t\t<th>&nbsp;</th>\n");
			}
			for (int c = 0; c < this.headersTopRow.length; ++c) {
				sb.append("\t\t<th>" + this.headersTopRow[c].toString() + "</th>\n");
			}
			sb.append("\t</tr>\n");
		}
		
		for (int r = 0; r < this.contents.length; ++r) {
			sb.append("\t<tr>\n");
			if (this.headersLeftRow != null) {
				sb.append("\t\t<th>");
				sb.append(this.headersLeftRow[r].toString());
				sb.append("</th>\n");
			}
			
			for (int c = 0; c < this.contents[r].length; ++c) {
				sb.append("\t\t<td>");
				sb.append(this.contents[r][c].toString());
				sb.append("</td>\n");
			}
			
			sb.append("\t</tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}
	
}

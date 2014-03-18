package ch.ethz.inf.dbproject.util.html;

public final class ALinkHelper extends HtmlHelperIface {
	
	private final String text;
	private final String url;
	
	public ALinkHelper(
		final String text,
		final String url
	) {
		this.text = text;
		this.url = url;
	}

	@Override
	public final String generateHtmlCode() {
		return "<a href=\"" + this.url + "\">" + this.text + "</a>";
	}
	
}

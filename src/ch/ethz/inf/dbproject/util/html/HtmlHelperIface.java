package ch.ethz.inf.dbproject.util.html;

public abstract class HtmlHelperIface {
	
	public abstract String generateHtmlCode();
	
	@Override
	public String toString() {
		return this.generateHtmlCode();
	}
	
}

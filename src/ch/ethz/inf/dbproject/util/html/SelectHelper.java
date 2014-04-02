package ch.ethz.inf.dbproject.util.html;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SelectHelper<T> extends HtmlHelperIface {

	// Creates a drop down selection
	// takes a name and a number (e.g. case title + case number)
	
	private final String selectName;
	private final String selectOptgroup;
	private final ArrayList<T> contents;
	private final Method getNameMethod;
	private final Method getNumberMethod;
	
	public SelectHelper(final String selName, final String optgroup, final String name, final String number, final Class<T> clazz){
		selectName = selName;
		selectOptgroup = optgroup;
		contents = new ArrayList<T>();

		String methodName = "get"
				+ name.substring(0, 1).toUpperCase()
				+ name.substring(1);

		Method getMethod = null;
		try {
			getMethod = clazz.getMethod(methodName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		this.getNameMethod = getMethod;
		
		methodName = "get"
				+ number.substring(0, 1).toUpperCase()
				+ number.substring(1);

		getMethod = null;
		try {
			getMethod = clazz.getMethod(methodName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		this.getNumberMethod = getMethod;
	}
	
	
	
	
	private String getString(final T t, Method m){
		String value = "";

		try {
			value = "" + m.invoke(t);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return value;
	}
	
	public final void addObjects(final List<T> content){
		contents.addAll(content);
	}
	
	
	@Override
	public String generateHtmlCode() {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append("<!-- SelectHelper generated code -->\n");
		sb.append("<select name = \"" + selectName + "\">\n" +
				"<optgroup label=\""+selectOptgroup+"\">");
		
		final Iterator<T> cit = contents.iterator();
		while(cit.hasNext()){
			final T t = cit.next();
			final String name = getString(t, getNameMethod);
			final String nr = getString(t, getNumberMethod);
			sb.append("<option value = \"" + nr + "\">" + name + "</option>\n");
		}
		
		sb.append("</select>");
		
		return sb.toString();
	}
	
	
	
}

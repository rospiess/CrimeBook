package ch.ethz.inf.dbproject.util.html;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BeanTableHelper<T> extends HtmlHelperIface {

	private abstract class Column {

		private final String header;

		public Column(final String header) {
			this.header = header;
		}

		public abstract String getString(T t);

	}

	private class BeanColumn extends Column {

		private final String field;
		private final Method getMethod;
		private final Class<T> clazz;

		private BeanColumn(final String header, final String field,
				final Class<T> clazz) {
			super(header);
			this.field = field;
			this.clazz = clazz;
			final String methodName = "get"
					+ this.field.substring(0, 1).toUpperCase()
					+ this.field.substring(1);

			Method getMethod = null;
			try {
				getMethod = this.clazz.getMethod(methodName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			this.getMethod = getMethod;
		}

		@Override
		public String getString(final T t) {

			String value = "";

			try {
				value = "" + this.getMethod.invoke(t);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return value;

		}

	}

	private final class LinkColumn extends BeanColumn {

		private final String text;
		private final String urlBase;

		public LinkColumn(final String header, final String text,
				final String urlBase, final String urlExtraParamBean,
				final Class<T> clazz) {
			super(header, urlExtraParamBean, clazz);
			this.text = text;
			this.urlBase = urlBase;
		}

		@Override
		public final String getString(final T t) {

			final String urlParam = super.getString(t);
			final String url = this.urlBase + urlParam;

			return "<a href=\"" + url + "\">" + this.text + "</a>";

		}

	}

	private final String tableHtmlId;
	private final String tableHtmlClass;
	private final Class<T> clazz;
	private final ArrayList<Column> columns;
	private final ArrayList<T> contents;
	private boolean vertical;

	public BeanTableHelper(final String tableHtmlId,
			final String tableHtmlClass, final Class<T> tClass) {
		super();
		this.tableHtmlId = tableHtmlId;
		this.tableHtmlClass = tableHtmlClass;
		this.clazz = tClass;
		this.columns = new ArrayList<Column>();
		this.contents = new ArrayList<T>();
		this.vertical = false;
	}

	/**
	 * Change the orientation of the table
	 */
	public final void setVertical(final boolean vertical) {
		this.vertical = vertical;
	}

	public final void addBeanColumn(final String header, final String fieldName) {
		this.columns.add(new BeanColumn(header, fieldName, this.clazz));
	}

	public final void addLinkColumn(final String header, final String text,
			final String urlBase, final String urlExtraParamBean) {
		this.columns.add(new LinkColumn(header, text, urlBase,
				urlExtraParamBean, this.clazz));
	}

	public final void addObjects(final List<T> contentBeans) {
		this.contents.addAll(contentBeans);
	}

	public final void addObjects(final T[] contentBeans) {
		for (int i = 0; i < contentBeans.length; i++) {
			this.contents.add(contentBeans[i]);
		}
	}

	public final void addObject(final T contentBeans) {
		this.contents.add(contentBeans);
	}

	@Override
	public final String generateHtmlCode() {

		final StringBuilder sb = new StringBuilder();

		sb.append("<!-- TableHelper Generated Code -->\n");
		sb.append("<table " + "cellpadding=\"0\" cellspacing=\"0\" " + "id=\""
				+ this.tableHtmlId + "\" " + "class=\"" + this.tableHtmlClass
				+ "\">\n");

		if (this.vertical) {

			for (final Iterator<Column> it = this.columns.iterator(); it
					.hasNext();) {
				final Column c = it.next();
				sb.append("\t<tr>\n");
				// Header
				sb.append("\t\t<th>" + c.header + "</th>\n");

				// Contents
				for (final Iterator<T> cit = this.contents.iterator(); cit
						.hasNext();) {
					final T t = cit.next();
					sb.append("\t\t<td>" + c.getString(t) + "</td>\n");
				}

				sb.append("\t</tr>\n");
			}

		} else {

			// Print headers
			sb.append("\t<tr>\n");
			for (final Iterator<Column> it = this.columns.iterator(); it
					.hasNext();) {
				final Column c = it.next();
				sb.append("\t\t<th>" + c.header + "</th>\n");
			}
			sb.append("\t</tr>\n");

			// Print contents
			for (final Iterator<T> it = this.contents.iterator(); it.hasNext();) {
				final T t = it.next();

				sb.append("\t<tr>\n");
				for (final Iterator<Column> cit = this.columns.iterator(); cit
						.hasNext();) {
					final Column c = cit.next();
					sb.append("\t\t<td>" + c.getString(t) + "</td>\n");
				}
				sb.append("\t</tr>\n");

			}

		}

		sb.append("</table>\n");
		return sb.toString();
	}

}

package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a category of project (i.e. Theft, Assault...) 
 */
public final class Category {

	private final Category supercat;
	private final String name;

	public Category(final String name, final Category supercat) {
		super();
		this.name = name;
		this.supercat = supercat;
	}

	public final String getName() {
		if (name.equals("OtherPers"))
			return "Other Personal Crime";
		else if (name.equals("OtherProp"))
			return "Other Property Crime";
		return name;
	}
	
	public String toString()
	{
		return name;
	}
	
	public final Category getSupercat() {
		return supercat;
	}
	
	
}

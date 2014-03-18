package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a category of project (i.e. Theft, Assault...) 
 */
public final class Category {

	/**
	 * TODO All properties need to be added here 
	 */	
	private final String name;

	public Category(final String name) {
		super();
		this.name = name;
	}

	public final String getName() {
		return name;
	}
	
}

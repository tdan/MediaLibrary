package model;

/**
 * The Collection Class
 */
public class Collection {
    
    private String description = "";
    private Integer id = 0;
    private String name = "";
    private Integer parentId = 0;
    private Integer profileId = 0;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Collection other = (Collection) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	// Collections are the same if their ids are the same.
	return true;
    }

    /**
     * Gets the description of the collection
     * 
     * @return the description of the collection
     */
    public String getDescription() {
	return description;
    }

    /**
     * Gets the id of the collection
     * 
     * @return the id of the collection
     */
    public Integer getId() {
	return id;
    }

    /**
     * Gets the name of the collection
     * 
     * @return the name of the collection
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the id of the parent collection the collection belongs to
     * 
     * @return the id of the parent collection
     */
    public Integer getParentId() {
	return parentId;
    }

    /**
     * Gets the id of the profile the collection belongs to
     * 
     * @return the id of the parent profile
     */
    public Integer getProfileId() {
	return profileId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /**
     * Sets the description of the collection
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the id of the collection
     * 
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
	this.id = id;
    }

    /**
     * Sets the name of the collection
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Sets the parent collection of the collection
     * 
     * @param p
     *            the id of the parent collection to set
     */
    public void setParentId(Integer p) {
	parentId = p;
    }

    /**
     * Sets the owner profile of the collection
     * 
     * @param p
     *            the id of the profile to set
     */
    public void setProfileId(Integer p) {
	profileId = p;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return name;
    }
}

package model;

/**
 * The MediaItem Class
 */
public class MediaItem {

    private String description = "";
    private String filePath = "";
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
	MediaItem other = (MediaItem) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	// MediaItems are the same if their ids are the same.
	return true;
    }

    /**
     * Gets the description of the media item
     * 
     * @return the description of the media item
     */
    public String getDescription() {
	return description;
    }

    /**
     * Gets the file path of the media item
     * 
     * @return the file path of the media item
     */
    public String getFilePath() {
	return filePath;
    }

    /**
     * Gets the id of the media item
     * 
     * @return the id of the media item
     */
    public Integer getId() {
	return id;
    }

    /**
     * Gets the name of the media item
     * 
     * @return the name of the media item
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the id of the parent collection of the media item
     * 
     * @return the id of the parent collection of the media item
     */
    public Integer getParentId() {
	return parentId;
    }

    /**
     * Gets the id of the owner profile of the media item
     * 
     * @return the id of the owner profile of the media item
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
     * Sets the description of the media item
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the file path of the media item
     * 
     * @param filePath
     *            the file path to set
     */
    public void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    /**
     * Sets the id of the media item
     * 
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
	this.id = id;
    }

    /**
     * Sets the name of the media item
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Sets the parent collection of the media item
     * 
     * @param p
     *            the id of the parent collection to set
     */
    public void setParentId(Integer p) {
	this.parentId = p;
    }

    /**
     * Sets the owner profile of the media item
     * 
     * @param p
     *            the id of the owner profile to set
     */
    public void setProfileId(Integer p) {
	this.profileId = p;
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

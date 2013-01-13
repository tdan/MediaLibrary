package model;

import java.util.List;
import java.util.Vector;

import model.Collection;
import database.DatabaseSupport;

/**
 * The Profile Class
 */
public class Profile {
    
    private String description = "";
    private Integer id = 0;
    private Integer isDefault = 0;
    private String name = "";

    /**
     * Adds a collection to another collection
     * 
     * @param parentCollectionId
     *            the id of the parent collection
     * @param childCollectionId
     *            the id of the child collection
     * @return true if the child collection was added successfully to the parent collection
     */
    public boolean addCollectionToCollection(int parentCollectionId, int childCollectionId) {
	Collection addTo = DatabaseSupport.getCollection(parentCollectionId);
	if (id != addTo.getProfileId())
	    return false;
	Collection toAdd = DatabaseSupport.getCollection(childCollectionId);
	if (id != toAdd.getProfileId())
	    return false;
	toAdd.setParentId(parentCollectionId);
	return DatabaseSupport.putCollection(toAdd);
    }

    /**
     * Adds a media item to a collection
     * 
     * @param collectionId
     *            the id of the collection
     * @param mediaItemId
     *            the id of the media item
     * @return true if the media item was added successfully to the collection
     */
    public boolean addMediaItemToCollection(int collectionId, int mediaItemId) {
	MediaItem addTo = DatabaseSupport.getMediaItem(collectionId);
	if (id != addTo.getProfileId())
	    return false;
	MediaItem toAdd = DatabaseSupport.getMediaItem(mediaItemId);
	if (id != toAdd.getProfileId())
	    return false;
	toAdd.setParentId(collectionId);
	return DatabaseSupport.putMediaItem(toAdd);
    }

    /**
     * Removes media items that are duplicates of the specified media item
     * 
     * @param mediaItemId
     *            the id of the media item to keep
     * @return true if successful
     */
    public boolean consolidateMediaItems(int mediaItemId) {
	Vector<Integer> duplicates = DatabaseSupport.getDuplicatesOfMediaItem(mediaItemId);
	for (Integer id : duplicates) {
	    DatabaseSupport.beginTransaction();
	    if (!DatabaseSupport.deleteMediaItem(id)) {
		DatabaseSupport.rollBackTransaction();
		return false;
	    }
	    DatabaseSupport.endTransaction();
	}
	return true;
    }

    /**
     * Counts the number of children of the specified collection
     * 
     * @param collectionId
     *            the id of the collection
     * @return the number of children
     */
    public int countChildrenOfCollection(int collectionId) {
	return DatabaseSupport.countProfileCollections(id, collectionId)
		+ DatabaseSupport.countProfileMediaItems(id, collectionId);
    }

    /**
     * Creates a new collection
     * 
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @return the new collection if it was created successfully
     */
    public Collection createCollection(String name, String description) {
	Collection collection = new Collection();
	collection.setProfileId(id);
	collection.setName(name);
	collection.setDescription(description);
	if (DatabaseSupport.putCollection(collection)) {
	    return collection;
	}
	return null;
    }

    /**
     * Creates a new media item
     * 
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @param filePath
     *            the file path to set
     * @return true if the media item was created successfully
     */
    public MediaItem createMediaItem(String name, String description, String filePath) {
	MediaItem mediaItem = new MediaItem();
	mediaItem.setProfileId(id);
	mediaItem.setName(name);
	mediaItem.setDescription(description);
	mediaItem.setFilePath(filePath);
	if (DatabaseSupport.putMediaItem(mediaItem)) {
	    return mediaItem;
	}
	return null;
    }

    /**
     * Deletes a collection
     * 
     * @param id
     *            the id of the collection to delete
     * @return true if the collection was deleted successfully
     */
    public boolean deleteCollection(int id) {
	Collection c = DatabaseSupport.getCollection(id);
	if (this.id != c.getProfileId())
	    return false;
	return DatabaseSupport.deleteCollection(id);
    }

    /**
     * Deletes a media item
     * 
     * @param id
     *            the id of the media item to delete
     * @return true if the media item was successfully deleted
     */
    public boolean deleteMediaItem(int id) {
	MediaItem mediaItem = DatabaseSupport.getMediaItem(id);
	if (this.id != mediaItem.getProfileId())
	    return false;
	return DatabaseSupport.deleteMediaItem(id);
    }

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
	Profile other = (Profile) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    /**
     * Gets an array of the children of a collection
     * 
     * @param collectionId
     *            the id of the collection
     * @return the array
     */
    public List<Object> getChildrenOfCollection(int collectionId) {
	Vector<Object> toReturn = new Vector<Object>();
	toReturn.addAll(DatabaseSupport.getProfileCollections(id, collectionId));
	toReturn.addAll(DatabaseSupport.getProfileMediaItems(id, collectionId));
	return toReturn;
    }

    /**
     * Returns the collection specified by its id
     * 
     * @param id
     *            the id
     * @return the collection
     */
    public Collection getCollectionById(Integer id) {
	return DatabaseSupport.getCollection(id);
    }

    /**
     * Returns the collections matching a description
     * 
     * @param description
     *            the description
     * @return the collections
     */
    public Vector<Integer> getCollectionsByDescription(String description) {
	return DatabaseSupport.getCollectionsByDescription(id, description);
    }

    /**
     * Returns the collections matching a name
     * 
     * @param name
     *            the name
     * @return the collections
     */
    public Vector<Integer> getCollectionsByName(String name) {
	return DatabaseSupport.getCollectionsByName(id, name);
    }

    /**
     * Gets the description of the profile
     * 
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Return a list of duplicate media items
     * 
     * @return a list of duplicate media items
     */
    public Vector<Integer> getDuplicateMediaItems() {
	return DatabaseSupport.getDuplicateMediaItems(id);
    }

    /**
     * Gets the id of the profile
     * 
     * @return the id
     */
    public Integer getId() {
	return id;
    }

    /**
     * Gets whether the profile is the default profile
     * 
     * @return true if the profile is the default profile
     */
    public Integer getIsDefault() {
	return isDefault;
    }

    /**
     * Returns a media item specified by its id
     * 
     * @param id
     *            the id
     * @return the media item
     */
    public MediaItem getMediaItemById(Integer id) {
	return DatabaseSupport.getMediaItem(id);
    }

    /**
     * Returns media items matching a description
     * 
     * @param description
     *            the description
     * @return the media items
     */
    public Vector<Integer> getMediaItemsByDescription(String description) {
	return DatabaseSupport.getMediaItemsByDescription(id, description);
    }

    /**
     * Returns media items matching a name
     * 
     * @param name
     *            the name
     * @return the media items
     */
    public Vector<Integer> getMediaItemsByName(String name) {
	return DatabaseSupport.getMediaItemsByName(id, name);
    }

    /**
     * Gets the name of the profile
     * 
     * @return the name
     */
    public String getName() {
	return name;
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
     * Removes a collection from its parent collection
     * 
     * @param collectionId
     *            the id of the collection to remove
     * @return true if the collection was removed successfully from its parent collection
     */
    public boolean removeCollectionFromParent(int collectionId) {
	Collection toRemove = DatabaseSupport.getCollection(collectionId);
	if (id != toRemove.getProfileId())
	    return false;
	toRemove.setParentId(null);
	return DatabaseSupport.putCollection(toRemove);
    }

    /**
     * Removes a media item from its parent collection
     * 
     * @param mediaItemId
     *            the id of the media item to remove
     * @return true if the media item was removed successfully from its parent collection
     */
    public boolean removeMediaItemFromParent(int mediaItemId) {
	MediaItem toRemove = DatabaseSupport.getMediaItem(mediaItemId);
	if (id != toRemove.getProfileId())
	    return false;
	toRemove.setParentId(null);
	return DatabaseSupport.putMediaItem(toRemove);
    }

    /**
     * Sets the description of the profile
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the ID of the profile
     * 
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
	this.id = id;
    }

    /**
     * Sets whether the profile is the default profile
     * 
     * @param isDefault
     *            true if the profile is the default profile
     */
    public void setIsDefault(Integer isDefault) {
	if (this.isDefault == 1 && isDefault != 1) {
	    return;
	}
	this.isDefault = isDefault == 1 ? 1 : 0;
    }

    /**
     * Sets the name of the profile
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	if (isDefault == 1)
	    return name + " (default)";
	return name;
    }

    /**
     * Updates a collection
     * 
     * @param id
     *            the id of the collection to update
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @return true if the collection was updated successfully
     */
    public boolean updateCollection(int id, String name, String description) {
	Collection c = DatabaseSupport.getCollection(id);
	if (this.id != c.getProfileId())
	    return false;
	c.setName(name);
	c.setDescription(description);
	return DatabaseSupport.putCollection(c);
    }

    /**
     * Updates a media item
     * 
     * @param id
     *            the id of the media item to update
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @param filePath
     *            the file path to set
     * @return true if the media item was updated successfully
     */
    public boolean updateMediaItem(int id, String name, String description, String filePath) {
	MediaItem mediaItem = DatabaseSupport.getMediaItem(id);
	if (this.id != mediaItem.getProfileId())
	    return false;
	mediaItem.setName(name);
	mediaItem.setDescription(description);
	mediaItem.setFilePath(filePath);
	return DatabaseSupport.putMediaItem(mediaItem);
    }

}

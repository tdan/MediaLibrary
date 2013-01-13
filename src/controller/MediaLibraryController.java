package controller;

import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import model.Collection;
import model.MediaItem;
import model.MediaLibrary;
import model.Profile;

/**
 * The MediaLibraryController Class
 */
public class MediaLibraryController extends ui.TreeModelSupport implements TreeModel {

    public static boolean setCurrentProfile(int profileId) {
	return MediaLibrary.setCurrentProfile(profileId);
    }

    public boolean addCollectionToCollection(int parentCollectionId, int childCollectionId) {
	return new MediaLibrary().getCurrentProfile().addCollectionToCollection(parentCollectionId, childCollectionId);
    }

    public boolean addMediaItemToCollection(int collectionId, int mediaItemId) {
	return new MediaLibrary().getCurrentProfile().addMediaItemToCollection(collectionId, mediaItemId);
    }

    public boolean consolidateMediaItems(int mediaItemId) {
	return new MediaLibrary().getCurrentProfile().consolidateMediaItems(mediaItemId);
    }

    public int countChildrenOfCollection(int collectionId) {
	return new MediaLibrary().getCurrentProfile().countChildrenOfCollection(collectionId);
    }

    public Collection createCollection(String name, String description) {
	return new MediaLibrary().getCurrentProfile().createCollection(name, description);
    }

    public MediaItem createMediaItem(String name, String description, String filePath) {
	return new MediaLibrary().getCurrentProfile().createMediaItem(name, description, filePath);
    }

    public Profile createProfile(String name, String description) {
	return new MediaLibrary().createProfile(name, description);
    }

    public boolean deleteCollection(int collectionId) {
	return new MediaLibrary().getCurrentProfile().deleteCollection(collectionId);
    }

    public boolean deleteMediaItem(int mediaItemId) {
	return new MediaLibrary().getCurrentProfile().deleteMediaItem(mediaItemId);
    }

    public boolean deleteProfile(int profileId) {
	return new MediaLibrary().deleteProfile(profileId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    @Override
    public Object getChild(Object parent, int index) {
	if (parent instanceof Profile) {
	    return new MediaLibrary().getCurrentProfile().getChildrenOfCollection(0).get(index);
	}
	if (parent instanceof Collection) {
	    return new MediaLibrary().getCurrentProfile().getChildrenOfCollection(((Collection) parent).getId())
		    .get(index);
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(Object parent) {
	if (parent instanceof Profile) {
	    return new MediaLibrary().getCurrentProfile().countChildrenOfCollection(0);
	}
	if (parent instanceof Collection) {
	    return new MediaLibrary().getCurrentProfile().countChildrenOfCollection(((Collection) parent).getId());
	}
	return 0;
    }

    public List<Object> getChildrenOfCollection(int collectionId) {
	return new MediaLibrary().getCurrentProfile().getChildrenOfCollection(collectionId);
    }

    public Collection getCollectionById(Integer id) {
	return new MediaLibrary().getCurrentProfile().getCollectionById(id);
    }

    public List<Integer> getCollectionsByDescription(String description) {
	return new MediaLibrary().getCurrentProfile().getCollectionsByName(description);
    }

    public List<Integer> getCollectionsByName(String name) {
	return new MediaLibrary().getCurrentProfile().getCollectionsByName(name);
    }

    public Profile getCurrentProfile() {
	return new MediaLibrary().getCurrentProfile();
    }

    public Vector<Integer> getDuplicateMediaItems() {
	return new MediaLibrary().getCurrentProfile().getDuplicateMediaItems();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
	if (parent instanceof Profile) {
	    return new MediaLibrary().getCurrentProfile().getChildrenOfCollection(0).indexOf(child);
	}
	if (parent instanceof Collection) {
	    return new MediaLibrary().getCurrentProfile().getChildrenOfCollection(((Collection) parent).getId())
		    .indexOf(child);
	}
	return 0;
    }

    public MediaItem getMediaItemById(Integer id) {
	return new MediaLibrary().getCurrentProfile().getMediaItemById(id);
    }

    public Vector<Integer> getMediaItemsByDescription(String description) {
	return new MediaLibrary().getCurrentProfile().getCollectionsByName(description);
    }

    public Vector<Integer> getMediaItemsByName(String name) {
	return new MediaLibrary().getCurrentProfile().getCollectionsByName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    @Override
    public Object getRoot() {
	return getCurrentProfile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    @Override
    public boolean isLeaf(Object node) {
	return node instanceof MediaItem;
    }

    public boolean removeCollectionFromParent(int collectionId) {
	return new MediaLibrary().getCurrentProfile().removeCollectionFromParent(collectionId);
    }

    public boolean removeMediaItemFromParent(int collectionId) {
	return new MediaLibrary().getCurrentProfile().removeMediaItemFromParent(collectionId);
    }

    public boolean setDefaultProfile(int profileId) {
	return new MediaLibrary().setDefaultProfile(profileId);
    }

    public boolean updateCollection(int id, String name, String description) {
	return new MediaLibrary().getCurrentProfile().updateCollection(id, name, description);
    }

    public boolean updateMediaItem(int id, String name, String description, String filePath) {
	return new MediaLibrary().getCurrentProfile().updateMediaItem(id, name, description, filePath);
    }

    public boolean updateProfile(int profileId, String name, String description) {
	return new MediaLibrary().updateProfile(profileId, name, description);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public List<Profile> getProfiles() {
	return new MediaLibrary().getProfiles();
    }
}

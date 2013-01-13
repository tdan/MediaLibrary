package model;

import java.util.List;

import database.DatabaseSupport;

/**
 * The MediaLibrary Class
 */
public class MediaLibrary {
    
    // The id of the profile that is in use
    private static Integer currentProfileId = DatabaseSupport.getDefaultProfileId();

    /**
     * Creates a new profile
     * 
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @return the profile if it was created successfully
     */
    public Profile createProfile(String name, String description) {
	Profile profile = new Profile();
	profile.setName(name);
	profile.setDescription(description);
	if (DatabaseSupport.putProfile(profile))
	    return profile;
	return null;
    }

    /**
     * Deletes a profile
     * 
     * @param profileId
     *            the id of the profile to delete
     * @return true if the profile was deleted successfully
     */
    public boolean deleteProfile(int profileId) {
	return DatabaseSupport.deleteProfile(profileId);
    }

    /**
     * Gets the current profile
     * 
     * @return the current profile
     */
    public Profile getCurrentProfile() {
	return DatabaseSupport.getProfile(currentProfileId);
    }

    /**
     * Gets a profile
     * 
     * @param profileId
     *            the id of the profile to get
     * @return the profile
     */
    public Profile getProfile(int profileId) {
	return DatabaseSupport.getProfile(profileId);
    }

    /**
     * Sets the current profile
     * 
     * @param profileId
     *            the id of the profile to set
     * @return true if the current profile was set successfully
     */
    public static boolean setCurrentProfile(Integer profileId) {
	currentProfileId = profileId;
	return true;
    }

    /**
     * Sets the default profile
     * 
     * @param profileId
     *            the id of the profile to set
     * @return true if the default profile was set successfully
     */
    public boolean setDefaultProfile(int profileId) {
	Profile profile = DatabaseSupport.getProfile(profileId);
	profile.setIsDefault(1);
	return DatabaseSupport.putProfile(profile);
    }

    /**
     * Updates a profile
     * 
     * @param profileId
     *            the id of the profile to update
     * @param name
     *            the name to set
     * @param description
     *            the description to set
     * @return true if the profile was updated successfully
     */
    public boolean updateProfile(int profileId, String name, String description) {
	Profile profile = DatabaseSupport.getProfile(profileId);
	profile.setName(name);
	profile.setDescription(description);
	return DatabaseSupport.putProfile(profile);
    }

    public List<Profile> getProfiles() {
	return DatabaseSupport.getProfiles();
    }

}

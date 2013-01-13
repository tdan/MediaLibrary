package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import model.Collection;
import model.MediaItem;
import model.Profile;

/**
 * The Database Support Class
 */
public class DatabaseSupport {

    private static Connection connection = null;

    static final String TBLCOLLECTION = "Collection";
    static final String TBLMEDIAITEM = "MediaItem";
    static final String TBLPROFILE = "Profile";

    /**
     * Begins tracking modifications to the database
     */
    public static void beginTransaction() {
	if (connection != null) {
	    Statement statement = null;
	    try {
		statement = connection.createStatement();
		statement.executeUpdate("BEGIN TRANSACTION;");
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (statement != null) {
		    try {
			statement.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Closes the database connection
     */
    public static void close() {
	if (connection != null) {
	    try {
		connection.close();
	    } catch (SQLException sQLexception) {
		System.out.println("Could not close database connection!");
	    } finally {
		connection = null;
	    }
	}
    }

    /**
     * Returns the number of collections directly assigned to a given collection in a given profile
     * 
     * @param profileId
     *            the profile
     * @param collectionId
     *            the collection
     * @return the number of collections directly assigned to a given collection in a given profile
     */
    public static int countProfileCollections(int profileId, int collectionId) {
	int toReturn = 0;
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet statementRS = null;
	    try {
		statement = connection.prepareStatement("SELECT COUNT(*) FROM " + TBLCOLLECTION
			+ " WHERE profileId=? AND parentId=?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, collectionId, java.sql.Types.INTEGER);
		statementRS = statement.executeQuery();
		toReturn = statementRS.getInt(1);
		statementRS.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (statementRS != null)
			statementRS.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Returns the number of media items directly assigned to a given collections in a given profile
     * 
     * @param profileId
     *            the profile
     * @param collectionId
     *            the collection
     * @return the number of media items directly assigned to a given collections in a given profile
     */
    public static int countProfileMediaItems(int profileId, int collectionId) {
	int toReturn = 0;
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet statementRS = null;
	    try {
		statement = connection.prepareStatement("SELECT COUNT(*) FROM " + TBLMEDIAITEM
			+ " WHERE profileId=? AND parentId=?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, collectionId, java.sql.Types.INTEGER);
		statementRS = statement.executeQuery();
		toReturn = statementRS.getInt(1);
		statementRS.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (statementRS != null)
			statementRS.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Deletes the database
     * 
     * @return true if the database was deleted successfully
     */
    public static boolean delete() {
	if (connection != null)
	    return false;
	File database = new File("client.db");
	if (database.exists()) {
	    return database.delete(); // Will fail if the database file is open
	}
	return false;
    }

    /**
     * Deletes a collection from the database
     * 
     * @param id
     *            the id of the collection to delete
     * @return true if the collection was deleted successfully
     */
    public static boolean deleteCollection(int id) {
	return deleteRecord(TBLCOLLECTION, id);
    }

    /**
     * Deletes a media item from the database
     * 
     * @param id
     *            the id of the media item to delete
     * @return true if the media item was deleted successfully
     */
    public static boolean deleteMediaItem(int id) {
	return deleteRecord(TBLMEDIAITEM, id);
    }

    /**
     * Deletes a profile from the database
     * 
     * @param id
     *            the id of the profile to delete
     * @return true if the profile was deleted successfully
     */
    public static boolean deleteProfile(int id) {
	Profile profile = getProfile(id);
	if(profile != null && profile.getIsDefault() != 1)
	    return deleteRecord(TBLPROFILE, id);
	else
	    return false;
    }

    /**
     * Deletes a record from the database
     * 
     * @param table
     *            the table of the record
     * @param id
     *            the id of the record to delete
     * @return true if the record was deleted successfully
     */
    private static boolean deleteRecord(String table, int id) {
	if (connection != null) {
	    PreparedStatement delete = null;
	    try {
		delete = connection.prepareStatement("DELETE FROM " + table + " WHERE id=?");
		delete.setObject(1, id, java.sql.Types.INTEGER);
		delete.executeUpdate();
		delete.close();
	    } catch (SQLException e) {
		return false;
	    } finally {
		try {
		    if (delete != null)
			delete.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    return true;
	}
	return false;
    }

    /**
     * Commits modifications to the database
     */
    public static void endTransaction() {
	if (connection != null) {
	    Statement statement = null;
	    try {
		statement = connection.createStatement();
		statement.executeUpdate("END TRANSACTION;");
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (statement != null) {
		    try {
			statement.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Gets a collection from the database
     * 
     * @param id
     *            the id of the collection to get
     * @return the collection
     */
    public static Collection getCollection(int id) {
	Collection toReturn = null;
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT profileId,parentId,name,description FROM "
			+ TBLCOLLECTION + " WHERE id=? LIMIT 1;");
		statement.setObject(1, id, java.sql.Types.INTEGER);
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
		    toReturn = new Collection();
		    toReturn.setId(id);
		    toReturn.setProfileId(resultSet.getInt(1));
		    toReturn.setParentId(resultSet.getInt(2));
		    toReturn.setName(resultSet.getString(3));
		    toReturn.setDescription(resultSet.getString(4));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the ids of collections by description from the database
     * 
     * @param profileId
     *            the id of profile to look in
     * @param description
     *            the description
     * @return the ids
     */
    public static Vector<Integer> getCollectionsByDescription(int profileId, String description) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLCOLLECTION
			+ " WHERE profileId=? AND description LIKE ?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, "%" + description + "%", java.sql.Types.VARCHAR);
		resultSet = statement.executeQuery();
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the ids of collections by name from the database
     * 
     * @param profileId
     *            the id of profile to look in
     * @param name
     *            the name
     * @return the ids
     */
    public static Vector<Integer> getCollectionsByName(int profileId, String name) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLCOLLECTION
			+ " WHERE profileId=? AND name LIKE ?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, "%" + name + "%", java.sql.Types.VARCHAR);
		resultSet = statement.executeQuery();
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the id of the default profile
     * 
     * @return the id
     */
    public static Integer getDefaultProfileId() {
	Integer toReturn = 0;
	if (connection != null) {
	    Statement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT id FROM " + TBLPROFILE + " WHERE isDefault='1' LIMIT 1;");
		if (resultSet.next()) {
		    toReturn = resultSet.getInt(1);
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the ids of duplicate media items from the database
     * 
     * @param profileId
     *            the id of the profile to look in
     * @return the ids
     */
    public static Vector<Integer> getDuplicateMediaItems(int profileId) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    Statement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT id FROM " + TBLMEDIAITEM + " t1 WHERE EXISTS (SELECT * "
			+ TBLMEDIAITEM + " t2 WHERE t2.id<>t1.id AND t2.filePath=t1.filePath);");
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets duplicate media items of a media item
     * 
     * @param mediaItemId
     *            the id of the media item
     * @return the duplicate media items
     */
    public static Vector<Integer> getDuplicatesOfMediaItem(int mediaItemId) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLMEDIAITEM
			+ " t1 WHERE EXISTS (SELECT * " + TBLMEDIAITEM
			+ " t2 WHERE t2.id=? AND t2.id<>t1.id AND t2.filePath=t1.filePath);");
		statement.setObject(1, mediaItemId, java.sql.Types.INTEGER);
		resultSet = statement.executeQuery();
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets a media item from the database
     * 
     * @param id
     *            the id of the media item to get
     * @return the media item
     */
    public static MediaItem getMediaItem(int id) {
	MediaItem toReturn = null;
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT profileId,parentId,name,description,filePath FROM "
			+ TBLMEDIAITEM + " WHERE id=? LIMIT 1;");
		statement.setObject(1, id, java.sql.Types.INTEGER);
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
		    toReturn = new MediaItem();
		    toReturn.setId(id);
		    toReturn.setProfileId(resultSet.getInt(1));
		    toReturn.setParentId(resultSet.getInt(2));
		    toReturn.setName(resultSet.getString(3));
		    toReturn.setDescription(resultSet.getString(4));
		    toReturn.setFilePath(resultSet.getString(5));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the ids of media items by description from the database
     * 
     * @param profileId
     *            the id of profile to look in
     * @param description
     *            the description
     * @return the ids
     */
    public static Vector<Integer> getMediaItemsByDescription(int profileId, String description) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLMEDIAITEM
			+ " WHERE profileId=? AND description LIKE ?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, "%" + description + "%", java.sql.Types.VARCHAR);
		resultSet = statement.executeQuery();
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets the ids of media items by name from the database
     * 
     * @param profileId
     *            the id of profile look in
     * @param name
     *            the name
     * @return the ids
     */
    public static Vector<Integer> getMediaItemsByName(int profileId, String name) {
	Vector<Integer> toReturn = new Vector<Integer>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLMEDIAITEM
			+ " WHERE profileId=? AND name LIKE ?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, "%" + name + "%", java.sql.Types.VARCHAR);
		resultSet = statement.executeQuery();
		while (resultSet.next()) {
		    toReturn.add(resultSet.getInt(1));
		}
		resultSet.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets a profile from the database
     * 
     * @param id
     *            the id of the profile to get
     * @return the profile
     */
    public static Profile getProfile(int id) {
	Profile toReturn = null;
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.prepareStatement("SELECT name,description,isDefault FROM " + TBLPROFILE
			+ " WHERE id=? LIMIT 1;");
		statement.setObject(1, id, java.sql.Types.INTEGER);
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
		    toReturn = new Profile();
		    toReturn.setId(id);
		    toReturn.setName(resultSet.getString(1));
		    toReturn.setDescription(resultSet.getString(2));
		    toReturn.setIsDefault(resultSet.getInt(3));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Gets collections in a profile
     * 
     * @param profileId
     *            the profile to look in
     * @param collectionId
     *            the id of the collection to look in
     * @return the collections
     */
    public static List<Collection> getProfileCollections(int profileId, int collectionId) {
	Vector<Collection> toReturn = new Vector<Collection>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet statementRS = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLCOLLECTION
			+ " WHERE profileId=? AND parentId=?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, collectionId, java.sql.Types.INTEGER);
		statementRS = statement.executeQuery();
		while (statementRS.next()) {
		    toReturn.add(getCollection(statementRS.getInt(1)));
		}
		statementRS.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (statementRS != null)
			statementRS.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Get media items in a profile
     * 
     * @param profileId
     *            the profile to look in
     * @param collectionId
     *            the id of the collection to look in
     * @return the media items
     */
    public static Vector<MediaItem> getProfileMediaItems(int profileId, int collectionId) {
	Vector<MediaItem> toReturn = new Vector<MediaItem>();
	if (connection != null) {
	    PreparedStatement statement = null;
	    ResultSet statementRS = null;
	    try {
		statement = connection.prepareStatement("SELECT id FROM " + TBLMEDIAITEM
			+ " WHERE profileId=? AND parentId=?;");
		statement.setObject(1, profileId, java.sql.Types.INTEGER);
		statement.setObject(2, collectionId, java.sql.Types.INTEGER);
		statementRS = statement.executeQuery();
		while (statementRS.next()) {
		    toReturn.add(getMediaItem(statementRS.getInt(1)));
		}
		statementRS.close();
		statement.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (statementRS != null)
			statementRS.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Initializes the database
     */
    private static void init() {
	if (connection != null) {
	    Statement statement = null;
	    try {
		beginTransaction();
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + TBLPROFILE
			+ " (id INTEGER PRIMARY KEY, name TEXT, description TEXT, isDefault INTEGER);");
		statement
			.executeUpdate("CREATE TABLE IF NOT EXISTS "
				+ TBLCOLLECTION
				+ " (id INTEGER PRIMARY KEY, profileId INTEGER, parentId INTEGER, name TEXT, description TEXT, FOREIGN KEY(profileID) REFERENCES "
				+ TBLPROFILE + "(id), FOREIGN KEY(parentId) REFERENCES " + TBLCOLLECTION + "(id));");
		statement
			.executeUpdate("CREATE TABLE IF NOT EXISTS "
				+ TBLMEDIAITEM
				+ " (id INTEGER PRIMARY KEY, profileId INTEGER, parentId INTEGER, name TEXT, description TEXT, filePath TEXT, FOREIGN KEY(profileID) REFERENCES "
				+ TBLPROFILE + "(id), FOREIGN KEY(parentId) REFERENCES " + TBLCOLLECTION + "(id));");
		statement.executeUpdate("INSERT INTO " + TBLPROFILE
			+ "(name,description,isDefault) VALUES('Default Profile','Default Profile','1');");
		statement.close();
		endTransaction();
	    } catch (SQLException e) {
		rollBackTransaction();
		e.printStackTrace();
	    } finally {
		if (statement != null) {
		    try {
			statement.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Opens a connection to the database
     */
    public static void open() {
	try {
	    Class.forName("org.sqlite.JDBC");
	    connection = DriverManager.getConnection("jdbc:sqlite:client.db");
	} catch (ClassNotFoundException e) {
	    System.err.println("Missing SQLite driver");
	    System.exit(1);
	} catch (SQLException e) {
	    System.err.println("Could not connect to database");
	    System.exit(1);
	}
	init();
    }

    /**
     * Puts a collection in the database
     * 
     * @param collection
     *            the collection to put
     * @return true if the collection was put successfully
     */
    public static boolean putCollection(Collection collection) {
	if (connection != null) {
	    PreparedStatement updateCollection = null;
	    PreparedStatement insertCollection = null;
	    Statement selectRowid = null;
	    ResultSet selectRowidRS = null;
	    try {
		if (collection.getId() > 0) {
		    updateCollection = connection.prepareStatement("UPDATE " + TBLCOLLECTION
			    + " SET profileId=?, parentID=?, name=?, description=? WHERE id=?;");
		    updateCollection.setObject(1, collection.getProfileId(), java.sql.Types.INTEGER);
		    updateCollection.setObject(2, collection.getParentId(), java.sql.Types.INTEGER);
		    updateCollection.setObject(3, collection.getName(), java.sql.Types.VARCHAR);
		    updateCollection.setObject(4, collection.getDescription(), java.sql.Types.VARCHAR);
		    updateCollection.setObject(5, collection.getId(), java.sql.Types.INTEGER);
		    updateCollection.executeUpdate();
		    updateCollection.close();
		} else {
		    insertCollection = connection.prepareStatement("INSERT INTO " + TBLCOLLECTION
			    + "(id,profileId,parentId,name,description) VALUES(?,?,?,?,?);");
		    insertCollection.setNull(1, java.sql.Types.INTEGER);
		    insertCollection.setObject(2, collection.getProfileId(), java.sql.Types.INTEGER);
		    insertCollection.setObject(3, collection.getParentId(), java.sql.Types.INTEGER);
		    insertCollection.setObject(4, collection.getName(), java.sql.Types.VARCHAR);
		    insertCollection.setObject(5, collection.getDescription(), java.sql.Types.VARCHAR);
		    insertCollection.executeUpdate();
		    insertCollection.close();

		    selectRowid = connection.createStatement();
		    selectRowidRS = selectRowid.executeQuery("SELECT last_insert_rowid();");
		    if (selectRowidRS.next()) {
			collection.setId(selectRowidRS.getInt(1));
		    }
		    selectRowidRS.close();
		    selectRowid.close();
		}
		return true;
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (selectRowidRS != null)
			selectRowidRS.close();
		    if (selectRowid != null)
			selectRowid.close();
		    if (insertCollection != null)
			insertCollection.close();
		    if (updateCollection != null)
			updateCollection.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return false;
    }

    /**
     * Puts a media item in the database
     * 
     * @param mediaItem
     *            a media item to put
     * @return true if the media item was put successfully
     */
    public static boolean putMediaItem(MediaItem mediaItem) {
	boolean toReturn = false;
	if (connection != null) {
	    PreparedStatement updateMediaItem = null;
	    PreparedStatement insertMediaItem = null;
	    Statement selectRowid = null;
	    ResultSet selectRowidRS = null;
	    try {
		if (mediaItem.getId() > 0) {
		    updateMediaItem = connection.prepareStatement("UPDATE " + TBLMEDIAITEM
			    + " SET profileId=?, parentId=?, name=?, description=?, filePath=? WHERE id=?;");
		    updateMediaItem.setObject(1, mediaItem.getProfileId(), java.sql.Types.INTEGER);
		    updateMediaItem.setObject(2, mediaItem.getParentId(), java.sql.Types.INTEGER);
		    updateMediaItem.setObject(3, mediaItem.getName(), java.sql.Types.VARCHAR);
		    updateMediaItem.setObject(4, mediaItem.getDescription(), java.sql.Types.VARCHAR);
		    updateMediaItem.setObject(5, mediaItem.getFilePath(), java.sql.Types.VARCHAR);
		    updateMediaItem.setObject(6, mediaItem.getId(), java.sql.Types.INTEGER);
		    updateMediaItem.executeUpdate();
		    updateMediaItem.close();
		} else {
		    insertMediaItem = connection.prepareStatement("INSERT OR IGNORE INTO " + TBLMEDIAITEM
			    + " (id,profileId,parentId,name,description,filePath) VALUES (?,?,?,?,?,?);");
		    insertMediaItem.setNull(1, java.sql.Types.INTEGER);
		    insertMediaItem.setObject(2, mediaItem.getProfileId(), java.sql.Types.INTEGER);
		    insertMediaItem.setObject(3, mediaItem.getParentId(), java.sql.Types.INTEGER);
		    insertMediaItem.setObject(4, mediaItem.getName(), java.sql.Types.VARCHAR);
		    insertMediaItem.setObject(5, mediaItem.getDescription(), java.sql.Types.VARCHAR);
		    insertMediaItem.setObject(6, mediaItem.getFilePath(), java.sql.Types.VARCHAR);
		    insertMediaItem.executeUpdate();
		    insertMediaItem.close();

		    selectRowid = connection.createStatement();
		    selectRowidRS = selectRowid.executeQuery("SELECT last_insert_rowid();");
		    mediaItem.setId(selectRowidRS.getInt(1));
		    selectRowidRS.close();
		    selectRowid.close();
		}
		toReturn = true;
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (selectRowidRS != null)
			selectRowidRS.close();
		    if (selectRowid != null)
			selectRowid.close();
		    if (insertMediaItem != null)
			insertMediaItem.close();
		    if (updateMediaItem != null)
			updateMediaItem.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }

    /**
     * Puts a profile in the databsae
     * 
     * @param profile
     *            the profile to put
     * @return true if the profile was put successfully
     */
    public static boolean putProfile(Profile profile) {
	if (connection != null) {
	    PreparedStatement updateProfile = null;
	    PreparedStatement insertProfile = null;
	    PreparedStatement updateProfiles = null;
	    Statement selectRowid = null;
	    ResultSet selectRowidRS = null;
	    try {
		if (profile.getId() > 0) {
		    updateProfile = connection.prepareStatement("UPDATE " + TBLPROFILE
			    + " SET name=?, description=?, isDefault=? WHERE id=?;");
		    updateProfile.setObject(1, profile.getName(), java.sql.Types.VARCHAR);
		    updateProfile.setObject(2, profile.getDescription(), java.sql.Types.VARCHAR);
		    updateProfile.setObject(3, profile.getIsDefault(), java.sql.Types.INTEGER);
		    updateProfile.setObject(4, profile.getId(), java.sql.Types.INTEGER);
		    updateProfile.executeUpdate();
		    updateProfile.close();
		} else {
		    insertProfile = connection.prepareStatement("INSERT INTO " + TBLPROFILE
			    + "(id,name,description,isDefault) VALUES(?,?,?,?);");
		    insertProfile.setNull(1, java.sql.Types.INTEGER);
		    insertProfile.setObject(2, profile.getName(), java.sql.Types.VARCHAR);
		    insertProfile.setObject(3, profile.getDescription(), java.sql.Types.VARCHAR);
		    insertProfile.setObject(4, profile.getIsDefault(), java.sql.Types.INTEGER);
		    insertProfile.executeUpdate();
		    insertProfile.close();

		    selectRowid = connection.createStatement();
		    selectRowidRS = selectRowid.executeQuery("SELECT last_insert_rowid();");
		    if (selectRowidRS.next()) {
			profile.setId(selectRowidRS.getInt(1));
		    }
		    selectRowidRS.close();
		    selectRowid.close();
		}
		if (profile.getIsDefault() == 1) {
		    updateProfiles = connection.prepareStatement("UPDATE " + TBLPROFILE
			    + " SET isDefault=0 WHERE id<>?;");
		    updateProfiles.setObject(1, profile.getId(), java.sql.Types.INTEGER);
		    updateProfiles.executeUpdate();
		    updateProfiles.close();
		}
		return true;
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (updateProfiles != null)
			updateProfiles.close();
		    if (selectRowidRS != null)
			selectRowidRS.close();
		    if (selectRowid != null)
			selectRowid.close();
		    if (insertProfile != null)
			insertProfile.close();
		    if (updateProfile != null)
			updateProfile.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return false;
    }

    /**
     * Rolls back modifications to the database
     */
    public static void rollBackTransaction() {
	if (connection != null) {
	    Statement statement = null;
	    try {
		statement = connection.createStatement();
		statement.executeUpdate("ROLLBACK TRANSACTION;");
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		if (statement != null) {
		    try {
			statement.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    // Disables instantiation
    private DatabaseSupport() {
    }

    /**
     * Returns a list of all profiles
     * 
     * @return a list of all profiles
     */
    public static List<Profile> getProfiles() {
	Vector<Profile> toReturn = new Vector<Profile>();
	if (connection != null) {
	    Statement statement = null;
	    ResultSet resultSet = null;
	    try {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT id FROM " + TBLPROFILE + ";");
		while (resultSet.next()) {
		    toReturn.add(getProfile(resultSet.getInt(1)));
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (resultSet != null)
			resultSet.close();
		    if (statement != null)
			statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return toReturn;
    }
}

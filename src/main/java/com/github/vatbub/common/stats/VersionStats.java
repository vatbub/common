package com.github.vatbub.common.stats;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.*;

import common.Common;

/**
 * This class allows the maintainer of an app to see which version of the app is
 * used by how many users using a
 * <a href="https://www.mongodb.com/">MongoDB</a>.
 * 
 * @author frede
 *
 */
public class VersionStats {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> mongoCollection;
	private String appVersion;

	public VersionStats(String mongoDBServerAddress, String mongoDBDatabaseName, String mongoDBCollectionName) {
		this(mongoDBServerAddress, mongoDBDatabaseName, mongoDBCollectionName, Common.getAppVersion());
	}

	public VersionStats(String mongoDBServerAddress, String mongoDBDatabaseName, String mongoDBCollectionName,
			String appVersion) {
		this.appVersion = appVersion;
		init(mongoDBServerAddress, mongoDBDatabaseName, mongoDBCollectionName);
	}

	/**
	 * Initializes the connection to the mongodb
	 * 
	 * @param mongoDBServerAddress
	 *            The address string of the database in the following form:
	 *            {@code mongodb://<dbuser>:<dbpassword>@<dbhost>:<dbport>/<dbname>}
	 * @param mongoDBDatabaseName
	 *            The name of the database
	 * @param mongoDBCollectionName
	 *            The name of the collection where stats shall be stored in,
	 *            ideally the app name
	 */
	private void init(String mongoDBServerAddress, String mongoDBDatabaseName, String mongoDBCollectionName) {
		mongoClient = new MongoClient(mongoDBServerAddress);
		mongoDatabase = mongoClient.getDatabase(mongoDBDatabaseName);
		mongoCollection = mongoDatabase.getCollection(mongoDBCollectionName);
	}

	/**
	 * This actually updates the stats with the data specified in the
	 * constructor. After the stats are updated, the {@link #close()}-method is
	 * automatically called so you don't need to worryx about that.
	 */
	public void updateStats() {
		
	}

	/**
	 * Closes the connection to the
	 * <a href="https://www.mongodb.com/">MongoDB</a> properly.
	 */
	public void close() {
		mongoClient.close();
	}

	/**
	 * Checks if the <a href="https://www.mongodb.com/">MongoDB</a> is
	 * reachable.<br>
	 * NOTE: This method will take a very short time to run if the
	 * <a href="https://www.mongodb.com/">MongoDB</a> is reachable but will take
	 * up to 30 seconds if not due to the way the
	 * <a href="https://docs.mongodb.com/ecosystem/drivers/">MongoDB driver</a>
	 * works.
	 * 
	 * @return {@code true} if the
	 *         <a href="https://www.mongodb.com/">MongoDB</a> is reachable,
	 *         {@code false} otherwise.
	 */
	public boolean isReachable() {
		try {
			mongoClient.getAddress();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

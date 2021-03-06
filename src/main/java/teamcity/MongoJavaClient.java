package teamcity;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoJavaClient {
	public static void insertMongoDoc(String jsonStr) {

		try {
			MongoClientURI uri = new MongoClientURI("mongodb://dbadmin:dbadmin@hzn-oe-090.bedford.progress.com:27017/admin");
			MongoClient mongoClient = new MongoClient(uri);
			MongoDatabase db = mongoClient.getDatabase("teamcity");
			MongoCollection<Document> collection = db.getCollection("teststats");

			// Document doc = new Document("name","test");
			// collection.insertOne(doc);

			// 4. JSON parse example
			
			Document mydoc = Document.parse(jsonStr);

			collection.insertOne(mydoc);
			mongoClient.close();


		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}

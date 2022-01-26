package data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

abstract public class DbHelper {
    // DB Access variables
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 27017;
    private static final String DATABASE_NAME = "LA2_BDD";

    // Collections name
    private static final String PROPOSAL_HAS_KEYWORDS_COLLECTION = "proposal_has_keywords";
    private static final String KEYWORD_HAS_PROPOSALS_COLLECTION = "keyword_has_proposals";
    private static final String TRANSACTION_HAS_COMMENTS_COLLECTION = "transaction_has_comments";
//    private static final String PROPOSAL_HAS_KEYWORDS_COLLECTION = "proposal_has_keyword";

    private static final MongoClient client = new MongoClient(SERVER_ADDRESS, SERVER_PORT);
    private static final MongoDatabase db = client.getDatabase(DATABASE_NAME);

    public static MongoCollection<Document> getProposalCollection() {
        return db.getCollection(PROPOSAL_HAS_KEYWORDS_COLLECTION);
    }

    public static MongoCollection<Document> getKeywordCollection() {
        return db.getCollection(KEYWORD_HAS_PROPOSALS_COLLECTION);
    }

    public static MongoCollection<Document> getTransactionCollection() {
        return db.getCollection(TRANSACTION_HAS_COMMENTS_COLLECTION);
    }

//    public static MongoCollection<Document> getProposalCollection() {
//
//    }

}

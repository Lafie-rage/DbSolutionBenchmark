package data.dao.proposition;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.UpdateResult;
import data.DbHelper;
import data.dao.Dao;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class KeywordHasProposalsDao implements Dao {
    private static final MongoCollection<Document> collection = DbHelper.getKeywordCollection();

    @Override
    public List<Document> getAll() {
        List<Document> results = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().cursor();
        while (cursor.hasNext()) {
            results.add(cursor.next());
        }
        return results;
    }

    @Override
    public List<Document> getWithFilter(Document filter) {
        List<Document> results = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().filter(filter).cursor();
        while (cursor.hasNext()) {
            results.add(cursor.next());
        }
        return results;
    }

    @Override
    public void insert(Document proposal) {
        collection.insertOne(proposal);
    }

    @Override
    public boolean update(Document filter, Document values) {
        UpdateResult result = collection.updateMany(filter, values);
        return result.getModifiedCount() > 1;
    }

    @Override
    public void remove(Document filter) {
        collection.deleteMany(filter);
    }

    @Override
    public void createIndexForField(String field) {
        collection.createIndex(Indexes.ascending(field));
    }

    @Override
    public void removeIndexFromField(String field) {
        collection.dropIndex(Indexes.ascending(field));
    }

    public int getLastProposalId() {
        int lastId = 0;
        MongoCursor<Document> cursor =  collection.find().sort(
                new BasicDBObject(
                        "proposals.id", -1
                )
        ).limit(1).cursor();
        while(cursor.hasNext()) {
            Document keyword = cursor.next();
            List<Document> proposals = (List<Document>) keyword.get("proposal");
            for(Document proposal : proposals) {
                int proposalId = proposal.getInteger("id");
                if(proposalId > lastId) {
                    lastId = proposalId;
                }
            }
        }
        return lastId;
    }

    public void removeLast(String keyword) {
        remove(new Document("keyword", keyword));
    }
}


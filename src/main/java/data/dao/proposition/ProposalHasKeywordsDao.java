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

public class ProposalHasKeywordsDao implements Dao {
    private static final MongoCollection<Document> collection = DbHelper.getProposalCollection();

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
        return collection.find().sort(
                new BasicDBObject(
                        "id", -1
                )
        ).limit(1).cursor().next().getInteger("id");
    }

    public void removeLast() {
        remove(new Document("id", getLastProposalId()));
    }
}

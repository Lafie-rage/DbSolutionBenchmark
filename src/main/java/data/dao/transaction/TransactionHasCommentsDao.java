package data.dao.transaction;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import data.DbHelper;
import data.dao.Dao;
import org.bson.Document;

import java.util.List;

public class TransactionHasCommentsDao implements Dao {
    private static final MongoCollection<Document> collection = DbHelper.getTransactionCollection();

    @Override
    public List<Document> getAll() {
        return null;
    }

    @Override
    public List<Document> getWithFilter(Document filter) {
        return null;
    }

    @Override
    public void insert(Document transaction) {
    }

    @Override
    public boolean update(Document filter, Document values) {
        return false;
    }

    @Override
    public void remove(Document filter) {

    }

    @Override
    public void createIndexForField(String field) {

    }

    @Override
    public void removeIndexFromField(String field) {

    }
}

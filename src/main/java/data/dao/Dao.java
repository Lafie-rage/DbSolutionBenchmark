package data.dao;

import org.bson.Document;

import java.util.List;

public interface Dao {

    List<Document> getAll();

    List<Document> getWithFilter(Document filter);

    void insert(Document item);

    boolean update(Document filter, Document values);

    void remove(Document filter);

    void createIndexForField(String field);

    void removeIndexFromField(String field);
}

package org.opencb.cellbase.lib.mongodb.db;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.opencb.cellbase.core.lib.api.ProteinDBAdaptor;
import org.opencb.cellbase.core.lib.dbquery.QueryOptions;
import org.opencb.cellbase.core.lib.dbquery.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imedina on 06/03/14.
 */
public class ProteinMongoDBAdaptor extends MongoDBAdaptor implements ProteinDBAdaptor {

    public ProteinMongoDBAdaptor(DB db, String species, String version) {
        super(db, species, version);
        mongoDBCollection = db.getCollection("protein");
    }

    @Override
    public QueryResult getAll(QueryOptions options) {
        return null;
    }

    @Override
    public QueryResult next(String chromosome, int position, QueryOptions options) {
        return null;
    }

    @Override
    public QueryResult getAllById(String id, QueryOptions options) {

        return null;
    }

    @Override
    public List<QueryResult> getAllByIdList(List<String> idList, QueryOptions options) {
        List<DBObject> queries = new ArrayList<>(idList.size());
        for (String id : idList) {
            QueryBuilder builder = QueryBuilder.start("name").is(id);
            queries.add(builder.get());
        }

//        options = addExcludeReturnFields("transcripts", options);
        return executeQueryList(idList, queries, options);
    }

    @Override
    public QueryResult getAllByAccession(String id, QueryOptions options) {
        return null;
    }

    @Override
    public List<QueryResult> getAllByAccessionList(List<String> idList, QueryOptions options) {
        return null;
    }
}

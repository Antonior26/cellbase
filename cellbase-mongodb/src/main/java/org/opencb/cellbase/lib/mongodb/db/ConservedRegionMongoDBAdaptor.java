package org.opencb.cellbase.lib.mongodb.db;

import com.mongodb.*;
import org.opencb.cellbase.core.common.ConservedRegionFeature;
import org.opencb.cellbase.core.common.GenomeSequenceFeature;
import org.opencb.cellbase.core.common.Region;
import org.opencb.cellbase.core.lib.api.ConservedRegionDBAdaptor;
import org.opencb.cellbase.core.lib.dbquery.QueryOptions;
import org.opencb.cellbase.core.lib.dbquery.QueryResult;

import java.util.*;

@Deprecated
public class ConservedRegionMongoDBAdaptor extends MongoDBAdaptor implements ConservedRegionDBAdaptor {


    int CHUNKSIZE;

    public ConservedRegionMongoDBAdaptor(DB db) {
        super(db);
    }

    public ConservedRegionMongoDBAdaptor(DB db, String species, String version) {
        super(db, species, version);
        CHUNKSIZE = Integer.parseInt(applicationProperties.getProperty("CELLBASE." + version.toUpperCase() + ".CONSERVED_REGION.CHUNK_SIZE", "2000"));
        mongoDBCollection = db.getCollection("conserved_region");
    }

    private int getChunk(int position) {
        return (position / Integer.parseInt(applicationProperties.getProperty("CELLBASE." + version.toUpperCase()
                + ".CONSERVED_REGION.CHUNK_SIZE", "2000")));
    }

    private int getChunkStart(int id) {
        return (id == 0) ? 1 : id * CHUNKSIZE;
    }

    private int getChunkEnd(int id) {
        return (id * CHUNKSIZE) + CHUNKSIZE - 1;
    }

    private int getOffset(int position) {
        return ((position) % Integer.parseInt(applicationProperties.getProperty("CELLBASE." + version.toUpperCase()
                + ".CONSERVED_REGION.CHUNK_SIZE", "2000")));
    }

    @Override
    public QueryResult getAllByRegion(Region region, QueryOptions options) {
        return getAllByRegionList(Arrays.asList(region), options).get(0);
    }

    @Override
    public List<QueryResult> getAllByRegionList(List<Region> regions, QueryOptions options) {
        //TODO not finished yet
        List<DBObject> queries = new ArrayList<>();
        List<String> ids = new ArrayList<>(regions.size());
        List<Integer> integerChunkIds;
        for (Region region : regions) {
            integerChunkIds = new ArrayList<>();
            // positions below 1 are not allowed
            if (region.getStart() < 1) {
                region.setStart(1);
            }
            if (region.getEnd() < 1) {
                region.setEnd(1);
            }

            /****/
            int regionChunkStart = getChunk(region.getStart());
            int regionChunkEnd = getChunk(region.getEnd());
            for (int chunkId = regionChunkStart; chunkId <= regionChunkEnd; chunkId++) {
                integerChunkIds.add(chunkId);
            }
//            QueryBuilder builder = QueryBuilder.start("chromosome").is(region.getChromosome()).and("chunkId").in(hunkIds);
            QueryBuilder builder = QueryBuilder.start("chromosome").is(region.getChromosome()).and("chunkId").in(integerChunkIds);
            /****/


            queries.add(builder.get());
            ids.add(region.toString());

            logger.info(builder.get().toString());

        }
        List<QueryResult> queryResults = executeQueryList(ids, queries, options);


        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);
            QueryResult queryResult = queryResults.get(i);
            BasicDBList list = (BasicDBList) queryResult.get("result");

            Map<String, List<Float>> typeMap = new HashMap();


//            int start = region.getStart();


            for (int j = 0; j < list.size(); j++) {
                BasicDBObject chunk = (BasicDBObject) list.get(j);
                String type = chunk.getString("type");
                List<Float> valuesList;
                if (!typeMap.containsKey(type)) {
                    valuesList = new ArrayList<>(region.getEnd() - region.getStart() + 1);
                    for (int val = 0; val < region.getEnd() - region.getStart() + 1; val++) {
                        valuesList.add(null);
                    }
                    typeMap.put(type, valuesList);
                } else {
                    valuesList = typeMap.get(type);
                }

                BasicDBList valuesChunk = (BasicDBList) chunk.get("values");

                int pos = 0;
                if( region.getStart() > chunk.getInt("start")){
                    pos = region.getStart() - chunk.getInt("start");
                }


                for (; pos < valuesChunk.size() && (pos + chunk.getInt("start") <= region.getEnd()); pos++) {
//                    System.out.println("valuesList SIZE = " + valuesList.size());
//                    System.out.println("pos = " + pos);
//                    System.out.println("DIV " + (chunk.getInt("start") - region.getStart()));
//                    System.out.println("valuesChunk = " + valuesChunk.get(pos));
//                    System.out.println("indexFinal = " + (pos + chunk.getInt("start") - region.getStart()));
                    valuesList.set(pos + chunk.getInt("start") - region.getStart(), new Float((Double) valuesChunk.get(pos)));
                }
            }
//
            BasicDBList resultList = new BasicDBList();
            ConservedRegionFeature conservedRegionChunk;
            for (Map.Entry<String, List<Float>> elem : typeMap.entrySet()) {
                conservedRegionChunk = new ConservedRegionFeature(region.getChromosome(), region.getStart(), region.getEnd(), elem.getKey(), elem.getValue());
                resultList.add(conservedRegionChunk);
            }
            queryResult.setResult(resultList);
        }

        return queryResults;
    }


//    private List<ConservedRegion> executeQuery(DBObject query) {
//        List<ConservedRegion> result = null;
//        DBCursor cursor = mongoDBCollection.find(query);
//        try {
//            if (cursor != null) {
//                result = new ArrayList<ConservedRegion>(cursor.size());
////                Gson jsonObjectMapper = new Gson();
//                ConservedRegion feature = null;
//                while (cursor.hasNext()) {
////                    feature = (ConservedRegion) jsonObjectMapper.fromJson(cursor.next().toString(), ConservedRegion.class);
//                    result.add(feature);
//                }
//            }
//        } finally {
//            cursor.close();
//        }
//        return result;
//    }


//    @Override
//    public List<ConservedRegion> getByRegion(String chromosome, int start, int end) {
//        // positions below 1 are not allowed
//        if (start < 1) {
//            start = 1;
//        }
//        if (end < 1) {
//            end = 1;
//        }
//        QueryBuilder builder = QueryBuilder.start("chromosome").is(chromosome).and("end")
//                .greaterThan(start).and("start").lessThan(end);
//
//        System.out.println(builder.get().toString());
//        List<ConservedRegion> conservedRegionList = executeQuery(builder.get());
//
//        return conservedRegionList;
//    }

//    @Override
//    public List<List<ConservedRegion>> getByRegionList(List<Region> regions) {
//        List<List<ConservedRegion>> result = new ArrayList<List<ConservedRegion>>(regions.size());
//        for (Region region : regions) {
//            result.add(getByRegion(region.getSequenceName(), region.getStart(), region.getEnd()));
//        }
//        return result;
//    }


}

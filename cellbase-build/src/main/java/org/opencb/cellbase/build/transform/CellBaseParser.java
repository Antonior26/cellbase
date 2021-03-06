package org.opencb.cellbase.build.transform;

import org.opencb.cellbase.core.serializer.CellBaseSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by imedina on 30/08/14.
 */
public class CellBaseParser {

    protected CellBaseSerializer serializer;
    protected Logger logger;

    public CellBaseParser(CellBaseSerializer serializer) {
        this.serializer = serializer;

        logger = LoggerFactory.getLogger(this.getClass());
    }

}

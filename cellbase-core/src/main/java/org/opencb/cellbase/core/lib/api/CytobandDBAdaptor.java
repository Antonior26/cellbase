package org.opencb.cellbase.core.lib.api;


import org.opencb.cellbase.core.common.Region;
import org.opencb.cellbase.core.common.core.Cytoband;

import java.util.List;

@Deprecated
public interface CytobandDBAdaptor extends FeatureDBAdaptor {

	
	public List<Cytoband> getAllByRegion(String chromosome);

	public List<Cytoband> getAllByRegion(String chromosome, int start);

	public List<Cytoband> getAllByRegion(String chromosome, int start, int end);

	public List<Cytoband> getAllByRegion(Region region);

	public List<List<Cytoband>> getAllByRegionList(List<Region> regionList);

	
	public List<String> getAllChromosomeNames();

	public List<Cytoband> getAllByChromosome(String chromosome);

	public List<List<Cytoband>> getAllByChromosomeList(List<String> chromosome);

	
}

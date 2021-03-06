package org.opencb.cellbase.core.lib.api.variation;


import org.opencb.cellbase.core.common.IntervalFeatureFrequency;
import org.opencb.cellbase.core.common.Region;
import org.opencb.cellbase.core.common.variation.StructuralVariation;

import java.util.List;

public interface StructuralVariationDBAdaptor {
	
	
	public List<StructuralVariation> getAllByRegion(Region region);
	
	public List<List<StructuralVariation>> getAllByRegionList(List<Region> regionList);
	
	
	public List< StructuralVariation> getAllByRegion(Region region, int minLength, int maxLength);
	
	public List<List<StructuralVariation>> getAllByRegionList(List<Region> regionList, int minLength, int maxLength);
	
	
	public List<IntervalFeatureFrequency> getAllIntervalFrequencies(Region region, int interval);
	
}

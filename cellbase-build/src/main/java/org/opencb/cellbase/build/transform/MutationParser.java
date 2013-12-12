package org.opencb.cellbase.build.transform;

import org.opencb.cellbase.build.transform.serializers.CellbaseSerializer;
import org.opencb.cellbase.build.transform.utils.FileUtils;
import org.opencb.cellbase.build.transform.utils.VariationUtils;
import org.opencb.cellbase.core.common.variation.Mutation;
import org.opencb.commons.bioformats.protein.uniprot.UniprotParser;
import org.opencb.commons.bioformats.protein.uniprot.v201311jaxb.Entry;
import org.opencb.commons.bioformats.protein.uniprot.v201311jaxb.OrganismNameType;
import org.opencb.commons.bioformats.protein.uniprot.v201311jaxb.Uniprot;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: imedina
 * Date: 9/21/13
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutationParser {

    private CellbaseSerializer serializer;

    private static final int CHUNK_SIZE = 1000;

    public MutationParser(CellbaseSerializer serializer) {
        this.serializer = serializer;
    }

    // 0 Gene name***
    // 1 Accession Number***
    // 2 HGNC ID***
    // 3 Sample name***
    // 4 ID_sample***
    // 5 ID_tumour***
    // 6 Primary site***
    // 7 Site subtype***
    // 8 Primary histology***
    // 9 Histology subtype***
    // 10 Genome-wide screen***
    // 11 Mutation ID***
    // 12 Mutation CDS***
    // 13 Mutation AA***
    // 14 Mutation Description***
    // 15 Mutation zygosity***
    // 16 Mutation NCBI36 genome position***
    // 17 Mutation NCBI36 strand***
    // 18 Mutation GRCh37 genome position***
    // 19 Mutation GRCh37 strand***
    // 20 Mutation somatic status***
    // 21 Pubmed_PMID***
    // 22 Sample source***
    // 23 Tumour origin***
    // 24 Comments



    // Ensembl: phenotype_feature_id 0| phenotype_id 1| source_id 2| study_id 3| type 4| object_id 5      | is_significant 6| seq_region_id 7| seq_region_start 8| seq_region_end 9| seq_region_strand 10
    public void parseEnsembl(Path ensemblVariationDir) throws IOException {
        Map<String, String> seqRegionMap = VariationUtils.parseSeqRegionToMap(ensemblVariationDir);
        Map<String, String> sourceMap = VariationUtils.parseSourceToMap(ensemblVariationDir);
        Map<String, String> phenotypeMap = VariationUtils.parsePhenotypeToMap(ensemblVariationDir);

        MutationMongoDB mutation;
        String seqRegion = null;
        String source = null;
        String phenotype = null;
        String chunkIdSuffix = CHUNK_SIZE/1000+"k";

        BufferedReader br = FileUtils.newBufferedReader(ensemblVariationDir.resolve("phenotype_feature.txt.gz"), Charset.defaultCharset());
        String[] fields = null;
        String line = null;
        while((line = br.readLine()) != null) {
            fields = line.split("\t");

            if(fields[4].equals("Variation")) {
                seqRegion = seqRegionMap.get(fields[7]);
                source = sourceMap.get(fields[2]).split(",")[0];
                phenotype = phenotypeMap.get(fields[1]);

                //Mutation(String id, String chromosome, int start, int end,
                //String strand, String protein, int proteinStart, int proteinEnd, String gene, String transcriptId, String hgncId, String sampleId, String sampleName, String sampleSource, String tumourId, String primarySite, String siteSubtype, String primaryHistology, String histologySubtype, String genomeWideScreen, String mutationCDS, String mutationAA, String mutationZygosity, String status, String pubmed, String tumourOrigin, String description) {
                mutation = new MutationMongoDB(fields[5], seqRegion, Integer.parseInt(fields[8]), Integer.parseInt(fields[9]),
                        fields[10], "", 0, 0, "gene", "transcript", "", "", "", "", "",
                        "6", "7", phenotype, "9", "10", "12",
                        "13", "15", "20", "21", "23", "14", source);
                int chunkStart = (mutation.getStart()) / CHUNK_SIZE;
                int chunkEnd = (mutation.getEnd()) / CHUNK_SIZE;
                for(int i=chunkStart; i<=chunkEnd; i++) {
                    mutation.getChunkIds().add(mutation.getChromosome()+"_"+i+"_"+chunkIdSuffix);
                }
                serializer.serialize(mutation);

            }
        }

        br.close();
    }

    public void parse(Path cosmicMutationFile) {
        try {
//            BufferedReader br;
//            if(mutationFile.getName().endsWith(".gz")) {
//                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(mutationFile))));
//            }else {
//                br = Files.newBufferedReader(Paths.get(mutationFile.getAbsolutePath()), Charset.defaultCharset());
//            }

            BufferedReader br = FileUtils.newBufferedReader(cosmicMutationFile, Charset.defaultCharset());

            String chunkIdSuffix = CHUNK_SIZE/1000+"k";
            MutationMongoDB mutation;
            String line;
            String[] fields, regionFields;
            // First line is a header, we read and discard it
            br.readLine();
            while ((line = br.readLine()) != null) {
                fields = line.split("\t", -1);
                if(!fields[18].equals("")) {
                    regionFields = fields[18].split("[:-]");
                    if(regionFields.length == 3) {
                        String proteinStartString = fields[13].replaceAll("\\D", "");
//                        System.out.println(fields[13]+" =>"+proteinStartString+"<=");
                        int proteinStart = (proteinStartString.length() > 0 && proteinStartString.length() < 8) ? Integer.parseInt(proteinStartString) : 0;
                        mutation = new MutationMongoDB("COSM"+fields[11], regionFields[0], Integer.parseInt(regionFields[1]), Integer.parseInt(regionFields[2]),
                                fields[19], "", proteinStart, 0, fields[0], fields[1], fields[2], fields[4], fields[3], fields[22], fields[5],
                                fields[6], fields[7], fields[8], fields[9], fields[10], fields[12],
                                fields[13], fields[15], fields[20], fields[21], fields[23], fields[14], "cosmic");
                        int chunkStart = (mutation.getStart()) / CHUNK_SIZE;
                        int chunkEnd = (mutation.getEnd()) / CHUNK_SIZE;
                        for(int i=chunkStart; i<=chunkEnd; i++) {
                            mutation.getChunkIds().add(mutation.getChromosome()+"_"+i+"_"+chunkIdSuffix);
                        }
                        serializer.serialize(mutation);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public class MutationMongoDB extends Mutation{

        private List<String> chunkIds;

        public MutationMongoDB() {
            chunkIds = new ArrayList<>(2);
        }

        public MutationMongoDB(String mutationID, String chromosome, int start, int end, String strand, String protein, int proteinStart, int proteinEnd, String geneName, String ensemblTranscriptId, String hgncId, String sampleName, String sampleId, String tumourId, String primarySite, String siteSubtype, String primaryHistology, String histologySubtype, String genomeWideScreen, String mutationCDS, String mutationAA, String mutationZygosity, String mutationSomaticStatus, String pubmedPMID, String sampleSource, String tumourOrigin, String description, String source) {
            super(mutationID, chromosome, start, end, strand, protein, proteinStart, proteinEnd, geneName, ensemblTranscriptId, hgncId, sampleName, sampleId, tumourId, primarySite, siteSubtype, primaryHistology, histologySubtype, genomeWideScreen, mutationCDS, mutationAA, mutationZygosity, mutationSomaticStatus, pubmedPMID, sampleSource, tumourOrigin, description, source);
            chunkIds = new ArrayList<>(2);
        }

        public List<String> getChunkIds() {
            return chunkIds;
        }

        public void setChunkIds(List<String> chunkIds) {
            this.chunkIds = chunkIds;
        }
    }

}

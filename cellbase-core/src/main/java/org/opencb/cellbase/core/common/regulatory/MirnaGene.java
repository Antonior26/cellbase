package org.opencb.cellbase.core.common.regulatory;

// Generated Jun 5, 2012 6:41:13 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * MirnaGene generated by hbm2java
 */
public class MirnaGene implements java.io.Serializable {

	private int mirnaGeneId;
	private String mirbaseAcc;
	private String mirbaseId;
	private String status;
	private String sequence;
	private String source;
	private Set<MirnaDisease> mirnaDiseases = new HashSet<MirnaDisease>(0);
	private Set<MirnaGeneToMature> mirnaGeneToMatures = new HashSet<MirnaGeneToMature>(
			0);
	private Set<MirnaToGene> mirnaToGenes = new HashSet<MirnaToGene>(0);

	public MirnaGene() {
	}

	public MirnaGene(int mirnaGeneId, String mirbaseAcc, String mirbaseId,
			String status, String sequence, String source) {
		this.mirnaGeneId = mirnaGeneId;
		this.mirbaseAcc = mirbaseAcc;
		this.mirbaseId = mirbaseId;
		this.status = status;
		this.sequence = sequence;
		this.source = source;
	}

	public MirnaGene(int mirnaGeneId, String mirbaseAcc, String mirbaseId,
			String status, String sequence, String source,
			Set<MirnaDisease> mirnaDiseases,
			Set<MirnaGeneToMature> mirnaGeneToMatures,
			Set<MirnaToGene> mirnaToGenes) {
		this.mirnaGeneId = mirnaGeneId;
		this.mirbaseAcc = mirbaseAcc;
		this.mirbaseId = mirbaseId;
		this.status = status;
		this.sequence = sequence;
		this.source = source;
		this.mirnaDiseases = mirnaDiseases;
		this.mirnaGeneToMatures = mirnaGeneToMatures;
		this.mirnaToGenes = mirnaToGenes;
	}

	public int getMirnaGeneId() {
		return this.mirnaGeneId;
	}

	public void setMirnaGeneId(int mirnaGeneId) {
		this.mirnaGeneId = mirnaGeneId;
	}

	public String getMirbaseAcc() {
		return this.mirbaseAcc;
	}

	public void setMirbaseAcc(String mirbaseAcc) {
		this.mirbaseAcc = mirbaseAcc;
	}

	public String getMirbaseId() {
		return this.mirbaseId;
	}

	public void setMirbaseId(String mirbaseId) {
		this.mirbaseId = mirbaseId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Set<MirnaDisease> getMirnaDiseases() {
		return this.mirnaDiseases;
	}

	public void setMirnaDiseases(Set<MirnaDisease> mirnaDiseases) {
		this.mirnaDiseases = mirnaDiseases;
	}

	public Set<MirnaGeneToMature> getMirnaGeneToMatures() {
		return this.mirnaGeneToMatures;
	}

	public void setMirnaGeneToMatures(Set<MirnaGeneToMature> mirnaGeneToMatures) {
		this.mirnaGeneToMatures = mirnaGeneToMatures;
	}

	public Set<MirnaToGene> getMirnaToGenes() {
		return this.mirnaToGenes;
	}

	public void setMirnaToGenes(Set<MirnaToGene> mirnaToGenes) {
		this.mirnaToGenes = mirnaToGenes;
	}

}

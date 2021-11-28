package com.api.docman.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="document")
public class Document {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id")
	private Long documentId;
	
	@Column(name = "document_name")
	private String documentName;
	
	@Column(name = "document_type")
	private String documentType;
	
	@Column(name = "document_path")
	private String documentPath;
	
	@Column(name = "document_size")
	private Long documentSize;
	
	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Long getDocumentSize() {
		return documentSize;
	}

	public void setDocumentSize(Long documentSize) {
		this.documentSize = documentSize;
	}
	
	
}

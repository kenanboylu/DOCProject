package com.api.docman.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.api.docman.model.Document;
import com.api.docman.model.DocumentResponse;
import com.api.docman.model.ResponseByteArray;

@Component
public interface DocumentService {
	
	/* Finding document list on repository **/
	List<Document> findAllDocument();
	
	/* Finding document on repository by id  and return Document model  */
	Document findDocumentById(Long id);
	
	/* Saving document on repository  and return DocumentResponse model  **/
	DocumentResponse saveDocument(String name, String type, MultipartFile file);
	
	/* updating document content and return DocumentResponse model **/
	DocumentResponse updateDocument(Long id, MultipartFile file);
	
	/* deleting document from repository  and return DocumentResponse model  **/
	DocumentResponse removeDocument(Long id);
	
	/* get document content  and return ResponseByteArray model  **/
	ResponseByteArray getDocumentContent(Long id);
	

}

package com.api.docman.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.api.docman.exception.DocumentStorageException;
import com.api.docman.exception.ResourceNotFoundException;
import com.api.docman.model.Document;
import com.api.docman.model.DocumentResponse;
import com.api.docman.model.ResponseByteArray;
import com.api.docman.property.DocumentStorageProperty;
import com.api.docman.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	
	@Autowired 
	DocumentRepository documentRepo;
	
	@Autowired
	DocumentStorageProperty documentStoragePropery;

	@Override
	public List<Document> findAllDocument() {
		List<Document> docList = documentRepo.findAll();
		
		if(CollectionUtils.isEmpty(docList)) {
			throw new ResourceNotFoundException("Document does not exist!" );
		}
		
		return docList;
	}

	@Override
	public Document findDocumentById(Long id) throws ResourceNotFoundException{
		Optional<Document> document=documentRepo.findById(id);
		
		if(!document.isPresent()) {
			throw new ResourceNotFoundException("Document is not found!!");
		}
		
		return document.get();
	}


	@Override
	public DocumentResponse saveDocument(String name, String type,MultipartFile file) {
		DocumentResponse response = new DocumentResponse();
		
		//if invalid data exists return exception.
		doValidateDocument(file,name,type);
		
		//store file on server and return path.
		String path = storeDocument(file,name,type);
		
		if (path != null && path !="") {
			Document document = new Document();
			document.setDocumentName(name);
			document.setDocumentPath(path);
			document.setDocumentType(type);
			document.setDocumentSize(file.getSize());
			
			documentRepo.save(document);

			response.setCode("000");
			response.setMessage("Document is saved successfully!");
		}
		return response;
	}

	@Override
	public DocumentResponse updateDocument(Long id, MultipartFile file) {
		DocumentResponse response = new DocumentResponse();
		Document doc=findDocumentById(id);
		
		if(doc != null) {
			
			File oldFile=null;
			
			try {
				oldFile = ResourceUtils.getFile(documentStoragePropery.getUploadDir()+"/"+doc.getDocumentName()+"."+doc.getDocumentType());
			} catch (FileNotFoundException e) {
				throw new DocumentStorageException("Document is not found.");
			}
			
		    if(oldFile.exists()) {
		    	oldFile.delete();
		    }
		    
		    storeDocument(file, doc.getDocumentName(), doc.getDocumentType());
		    
		    response.setCode("000");
		    response.setMessage("Document is updated succesfully!");
		}
		return response;
	}

	@Override
	public DocumentResponse removeDocument(Long id) {
		DocumentResponse response = new DocumentResponse();
		Document doc=findDocumentById(id);
		
		if(doc != null) {
			documentRepo.deleteById(id);
			
			try {
				File file = ResourceUtils.getFile(documentStoragePropery.getUploadDir()+"/"+doc.getDocumentName()+"."+doc.getDocumentType());
			
			    if(file.exists()) {
			    	file.delete();
			    }
			    
			    response.setCode("000");
			    response.setMessage("Document is removed succesfully!");
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public void doValidateDocument(MultipartFile file, String docName, String docType) {

		if(file == null || org.thymeleaf.util.StringUtils.isEmpty(docName) || org.thymeleaf.util.StringUtils.isEmpty(docType)) {
			throw new DocumentStorageException("Unexpected argument, please check your arguments!");
		}
	
		
		String[] validType = {"PNG","JPG","JPEG","PDF","X","DOCX"};
		
		//if file size is greater than 5MB
		if(file != null && file.getSize() >= 5120000) {
			throw new DocumentStorageException("Unable to upload a document larger than 5 MB");
		}
		
		if(!Arrays.asList(validType).contains(org.thymeleaf.util.StringUtils.toUpperCase(docType, new Locale("tr-TR")))) {
			throw new DocumentStorageException("The document cannot be store because there is an invalid content type. Expected content type: "+StringUtils.arrayToDelimitedString(validType, ","));
		}
		
	}
	


	public String storeDocument(MultipartFile file, String docName, String docType) {

	 String fileName="";

		try {
			
			fileName = docName + "." + docType;

			Path fileStorageLocation = Paths.get(documentStoragePropery.getUploadDir()).toAbsolutePath().normalize();

			Files.createDirectories(fileStorageLocation);
			
			Path targetLocation = fileStorageLocation.resolve(fileName);

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return documentStoragePropery.getUploadDir();

		} catch (IOException ex) {
			throw new DocumentStorageException("Could not store document " + fileName, ex);

		}

	}

	@Override
	public ResponseByteArray getDocumentContent(Long id) {
		 Document doc = findDocumentById(id);
		 
		 ResponseByteArray response = new ResponseByteArray();
		 
		 if(doc !=null) {
			 		File file=null;
					try {
						file = ResourceUtils.getFile(documentStoragePropery.getUploadDir()+"/"+doc.getDocumentName()+"."+doc.getDocumentType());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//File is found
					System.out.println("File Found : " + file.exists());
					try {
						 response.setFileName(doc.getDocumentName());
					     response.setFile(Files.readAllBytes(file.toPath()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		  
				       
		 }
		return response;
	}
	
	

}

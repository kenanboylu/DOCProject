package com.api.docmann.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.api.docman.repository.DocumentRepository;
import com.api.docman.service.DocumentServiceImpl;
import com.api.docman.exception.DocumentStorageException;
import com.api.docman.exception.ResourceNotFoundException;
import com.api.docman.model.Document;
import com.api.docman.model.DocumentResponse;
import com.api.docman.property.DocumentStorageProperty;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
	
	@InjectMocks
	DocumentServiceImpl documentService;
	
	@Mock
	DocumentRepository documentRepo; 
	
	@Mock
	DocumentStorageProperty  documentStorageProperty;
	
	@Test
	public void findAllDocument_ifDocumentExistsInRepository_returnDocumentObjectList() {
		Mockito.when(documentRepo.findAll()).thenReturn(createDocumentList(1));
		List<Document> result=documentService.findAllDocument();
		
		assertEquals(1, result.size());
	}
	
	@Test
	public void findAllDocument_ifDocumentDoesntExistInRepository_throwResourceNotException(){
		try {
		Mockito.when(documentRepo.findAll()).thenReturn(createDocumentList(0));
	
		documentService.findAllDocument();
		} catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Document does not exist!");
        }

	}
	
	
	public static List<Document> createDocumentList(int count){
		List<Document> list = new ArrayList<Document>();
		
		for (int i = 0; i < count; i++) {
			Document doc = new Document();
			doc.setDocumentId(new Long(count));
			doc.setDocumentName("Document-"+count);
			doc.setDocumentType("PNG");
			doc.setDocumentSize(2L);

			list.add(doc);
		}
		
		return list;
	}
	
	@Test
	public void doValidateDocument_ifArgumentsAreNull_returnException() {
		try {
			documentService.doValidateDocument(null,"","");
			} catch (Exception e) {
	            assertThat(e)
	                    .isInstanceOf(DocumentStorageException.class)
	                    .hasMessage("Unexpected argument, please check your arguments!");
	        }
	}

	
	@Test
	public void doValidateDocument_ifDocumentContentTypeIsInvalid_returnException() {
		try {

			documentService.doValidateDocument(createMultipartFile("unit_test_file2","csv"),"unit_test_file2","csv");
			} catch (Exception e) {
	            assertThat(e)
	                    .isInstanceOf(DocumentStorageException.class)
	                    .hasMessage("The document cannot be store because there is an invalid content type. Expected content type: PNG,JPG,JPEG,PDF,X,DOCX");
	        }
	}
	
	@Test
	public void storeDocument_ifDocumentIsStoredIntoServer_returnDocumentPath() {
		DocumentServiceImpl spyService=Mockito.spy(documentService);
		Mockito.when(documentStorageProperty.getUploadDir()).thenReturn("documents_upload");
        String result=spyService.storeDocument(createMultipartFile("unit_test_file3","pdf"),"unit_test_file3.pdf", "pdf");
        
        assertEquals("documents_upload", result);
	}
	
	@Test
	public void saveDocument_ifDocumentIsSavedSuccesfully_returnSuccesfullMessageResponse() {
		DocumentServiceImpl spyService=Mockito.spy(documentService);
		Mockito.when(documentStorageProperty.getUploadDir()).thenReturn("documents_upload");
        DocumentResponse response=spyService.saveDocument("unit_test_file3.pdf", "pdf", createMultipartFile("unit_test_file3","pdf"));
        
        assertEquals("Document is saved successfully!", response.getMessage());
	}
	
	
	public static MultipartFile createMultipartFile(String fileName,String contentType) {
		File file=null;
		try {
			file = ResourceUtils.getFile("src/test/"+fileName+"."+contentType);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MultipartFile multipartFile= null;
		try {
			multipartFile = new MockMultipartFile(fileName, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return multipartFile;
	}

}

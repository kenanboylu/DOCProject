package com.api.docman.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.docman.exception.ResourceNotFoundException;
import com.api.docman.model.Document;
import com.api.docman.model.DocumentResponse;
import com.api.docman.model.ResponseByteArray;
import com.api.docman.service.DocumentService;

@RestController
@RequestMapping(path="docman")
public class DocumentController {
	
	@Autowired
	DocumentService documentService;
	
	@GetMapping(path="/all")
	public List<Document> getAllDocument() {
		return documentService.findAllDocument();
	}

	@GetMapping(path="/find")
	@ResponseBody
	public ResponseEntity<Document> getFileById(@RequestParam(name = "id") Long id) {
	   Document document = documentService.findDocumentById(id);
	   return new ResponseEntity<>(document, HttpStatus.OK);
	}
	
	@PostMapping("save")
	public ResponseEntity<DocumentResponse> saveDocument(@RequestParam(name="name") String name,@RequestParam(name="type") String type, @RequestPart("file") MultipartFile file){
		DocumentResponse response=documentService.saveDocument(name,type, file);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("update")
	@ResponseBody
	public ResponseEntity<DocumentResponse> updateDocument(@RequestParam(name = "id") Long id, @RequestPart("file") MultipartFile file){
		DocumentResponse response=documentService.updateDocument(id, file);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("remove")
	@ResponseBody
	public ResponseEntity<DocumentResponse> removeDocument(@RequestParam(name = "id") Long id){
		DocumentResponse response=documentService.removeDocument(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(path="/getContent")
	@ResponseBody
	public ResponseEntity<ResponseByteArray> getDocumentContent(@RequestParam(name = "id") Long id) {
		ResponseByteArray content = documentService.getDocumentContent(id);
	   return new ResponseEntity<>(content, HttpStatus.OK);
	}

}

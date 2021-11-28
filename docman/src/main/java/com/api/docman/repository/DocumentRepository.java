package com.api.docman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.docman.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long>{

}

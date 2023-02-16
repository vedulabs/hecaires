package com.hecaires.bigdata.repositories;

import com.hecaires.bigdata.documents.BulkDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BulkDocumentRepository extends MongoRepository<BulkDocument, String> {
    @Query(value="{'user_id' : ?0}", delete = true)
    void deleteByUser_id(String user_id);
}
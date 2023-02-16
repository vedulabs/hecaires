package com.hecaires.bigdata.services;

import com.hecaires.bigdata.documents.BulkDocument;
import com.hecaires.bigdata.repositories.BulkDocumentRepository;
import com.hecaires.rest.payloads.request.AddBulkDocumentRequest;
import com.hecaires.rest.security.CurrentUser;
import com.hecaires.rest.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkProcessingService {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    BulkDocumentRepository bulkDocumentRepository;

    public BulkDocument putBulkDocumentForCurrentAuthenticatedUser(AddBulkDocumentRequest addBulkDocumentRequest) {
        CurrentUser currentUser = userDetailsService.getCurrentUser();

        BulkDocument bulkDocument = new BulkDocument();
        bulkDocument.setUser_id(currentUser.getId());
        bulkDocument.setPayload(addBulkDocumentRequest.getPayload());

        return bulkDocumentRepository.save(bulkDocument);
    }
}
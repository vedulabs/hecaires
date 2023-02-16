package com.hecaires.bigdata.documents;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bulk")
@Data
public class BulkDocument {
    @Id
    private String id;

    @Size(min = 32, max = 32)
    private String user_id;

    private String payload;
}
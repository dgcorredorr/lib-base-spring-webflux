package com.meli.provider.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "coll_param")
public class ParamModel<T> {
    @Field("id")
    private String paramId;
    private String description;
    private Boolean status;
    private T values;
    @CreatedDate
    @Field(name = "createdAt")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Field(name = "updatedAt")
    private LocalDateTime updatedAt;
}
package com.sewage.springboot.service;

import org.apache.jena.rdf.model.Statement;

import java.util.List;

public interface GraphService {
    List<Statement> getEquipExceptionInference (String modelName, String subject, String predicate, String object);
}

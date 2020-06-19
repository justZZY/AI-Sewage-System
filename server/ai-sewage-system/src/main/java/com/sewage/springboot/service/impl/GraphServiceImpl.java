package com.sewage.springboot.service.impl;

import com.sewage.springboot.graph.TDBPersistence;
import com.sewage.springboot.service.GraphService;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {
    @Override
    public List<Statement> getEquipExceptionInference(String modelName, String subject, String predicate, String object) {
        TDBPersistence tdbPersistence = new TDBPersistence("IOT_Knowledge_Graph");
        return tdbPersistence.getTriplet(modelName, subject, predicate, object);
    }
}

package net.semwebprogramming.chapter2.HelloSemanticWeb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.mindswap.pellet.jena.PelletReasonerFactory;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;

public class HelloSemanticWeb {
    static String defaultNameSpace = "http://org.semwebprogramming/chapter2/people#";

    public static void main(String[] args) {
        // Load the RDF data from your ontology file
        Model model = loadAirpodsData();
        String query1 = "SELECT ?airpods ?type WHERE { ?airpods rdf:type ?type . }";
        
        String query2 = "SELECT ?airpods ?transparencyMode\n"
                + "WHERE {\n"
                + "  ?airpods <http://www.semanticweb.org/yosua/ontologies/2023/3/Airpods/Transparency_mode> ?transparencyMode .\n"
                + "  FILTER (?transparencyMode = true)\n"
                + "}\n"
                + "";
        
        String query3 = "SELECT ?airpods ?price\n"
                + "WHERE {\n"
                + "  ?airpods <http://www.semanticweb.org/yosua/ontologies/2023/3/Airpods/Price> ?price .\n"
                + "  FILTER (?price = \"129\")\n"
                + "}";
       
        String query4 = "SELECT ?airpods ?caseBattery\n"
                + "WHERE {\n"
                + "  ?airpods <http://www.semanticweb.org/yosua/ontologies/2023/3/Airpods/Case_Battery> ?caseBattery .\n"
                + "  FILTER (?caseBattery = \"Up to 30 hours\")\n"
                + "}\n"
                + "";


        System.out.println("first query to get all objects" );
        queryAirpods(model, query1);
        System.out.println("second query to get all airpods with transparency mode" );
        queryAirpods(model, query2);
        System.out.println("third query to get all airpods with price 129$" );
        queryAirpods(model, query3);
        System.out.println("first query to get all airpods with case battery up to 30 hours" );
        queryAirpods(model, query4);
        


        // Clean up and close the model
        model.close();
    }

    private static Model loadAirpodsData() {
        String ONTOLOGY_FILE = "Ontologies/Airpods_RDF.rdf";
        Model model = FileManager.get().loadModel(ONTOLOGY_FILE);
        System.out.println("Ontology loaded successfully.");
        return model;
    }

    private static void queryAirpods(Model model, String queryTarget) {
        // Define the prefix for the rdf namespace
        String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";

        // Define and execute your SPARQL query
        String queryString = prefix + queryTarget;
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode airpods = soln.get("airpods");
                RDFNode type = soln.get("type");
                System.out.println("AirPods: " + airpods + ", Type: " + type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the QueryExecution object
            qexec.close();
        }
    }
    
    
}

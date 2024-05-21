package fr.emse.master;

import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.lib.ShLib;
/**
 * This class made to validate the rdf with the shapes we have
 * BTW we did not write this code it is a copy paste from Apache Jena
 */
public class Validator {

    public static void main(String[] args) {
        String SHAPES = Main.shapeSHACL;
        String DATA = Main.rdf;
        Graph shapesGraph = RDFDataMgr.loadGraph(SHAPES);
        Graph dataGraph = RDFDataMgr.loadGraph(DATA);
        Shapes shapes = Shapes.parse(shapesGraph);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
        long totalTriples = dataGraph.size();
        long validTriples = report.conforms() ? totalTriples : 0;
        System.out.println("Total Triples: " + totalTriples);
        System.out.println("Total Valid Triples: " + validTriples);
        System.out.println("Total Tries: " + (totalTriples - validTriples));
        RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
    }
}
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.IRI;
import java.io.File;

public class MiniProjectChef {
    public static void main(String[] args) {

        String graphDbUrl = "http://localhost:7200";
        String repositoryName = "MiniProject";
        Repository database = new HTTPRepository(graphDbUrl, repositoryName);
        database.init();
        try (RepositoryConnection connection = database.getConnection()) {

            connection.clear();

            File ontologyFile = new File("doid.owl");
            File drugDataFile = new File("my_mapped_data.ttl");
            File diseaseDataFile = new File("my_diseases_data.ttl");

            connection.add(ontologyFile, "", RDFFormat.RDFXML);

            connection.add(drugDataFile, "", RDFFormat.TURTLE);

            connection.add(diseaseDataFile, "", RDFFormat.TURTLE);

            ValueFactory vf = database.getValueFactory();

            IRI drugIri = vf.createIRI("http://example.org/drug/Verteporfin");
            IRI propertyIri = vf.createIRI("http://example.org/ontology/contraindicatedFor");
            IRI diseaseIri = vf.createIRI("http://purl.obolibrary.org/obo/DOID-2340");
            connection.add(drugIri, propertyIri, diseaseIri);

        } catch (Exception e) {
            System.out.println("Fail" + e.getMessage());
        } finally {
            database.shutDown();
        }
    }
}
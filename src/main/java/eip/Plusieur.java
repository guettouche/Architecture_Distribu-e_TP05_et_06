/* By Guettouche */
package eip;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.log4j.BasicConfigurator;


import java.util.*;

public class Plusieur {

    public static void ExecuterPlusieursInstances() throws Exception {
        // Configure le logger par défaut
        BasicConfigurator.configure();

        // Contexte Camel par défaut
        CamelContext context = new DefaultCamelContext();

        // Recherche d'un animal via son nom
        RouteBuilder routeBuilderSwitch = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("direct:Guettouche")
                        .setHeader("recherche", simple("${body}"))
                        .setHeader(Exchange.HTTP_METHOD,constant("POST"))
                        .doTry()
                        .setBody(header("recherche"))
                        .to("http://127.0.0.1:8084/rest-service/zoo-manager/find/byName/")
                        .unmarshal().json(JsonLibrary.Jackson)
                        .log("${header.destinataire}")
                        .doCatch(HttpOperationFailedException.class)
                        .doTry()
                        .setBody(header("recherche"))
                        .to("http://127.0.0.1:8084/rest-service/zoo-manager/find/byName/")
                        .unmarshal().json(JsonLibrary.Jackson)
                        .log("${header.destinataire}")
                        .doCatch(HttpOperationFailedException.class)
                        .log("Animal introuvable")
                        .end()
                        .end()

                ;
            }
        };
        routeBuilderSwitch.addRoutesToCamelContext(context);


        // On démarre le contexte pour activer les routes
        context.start();

        //créer un producteur
        ProducerTemplate pt = context.createProducerTemplate();

        String nom;
        nom = null;

        Scanner sc = new Scanner(System.in);
        while (true) {
            
            System.out.println("Veuillez saisir le nom de l'animal : ");
            nom = sc.nextLine();
            if (nom.equals("exit")) break;
            pt.sendBody("direct:Guettouche", nom);
        }
    }

    public static void main(String[] args) throws Exception {
        ExecuterPlusieursInstances();
    }
}

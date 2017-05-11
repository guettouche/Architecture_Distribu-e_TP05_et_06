/* By Guettouche */
package eip;
import java.util.Scanner;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.BasicConfigurator;

public class ProducerConsumer {
    
        public static void main(String[] args) throws Exception {
            // Configure le logger par défaut
            BasicConfigurator.configure();

            // Contexte Camel par défaut
            CamelContext context = new DefaultCamelContext();
            
            // Crée une route contenant le consommateur
            RouteBuilder routeBuilder = new RouteBuilder() {

                @Override
                public void configure() throws Exception {
                    // On définit un consommateur 'consumer-1'
                    // qui va écrire le message
                    from("direct:consumer-1").to("log:affiche-1-log");
                    
                /*****************************************************************************************************/
                    /**
                    * Question 2_a
                    * Ajoutez un second consommateur consumer-2 qui écrit dans le endpoint file:messages.
                    */
                    from("direct:consumer-2").to("file:logs_02");
                
                /*****************************************************************************************************/    
                    /**
                    * Question 2_b et 2_c
                    * 2b°) Ajoutez une nouvelle route consumer-all qui va envoyer le message à consumer-2 
                    *   si l'entête destinataire contient « écrire » ou à  consumer-1 sinon.
                    * 2c°) Modifiez le producteur pour qu'il n'appelle que le consommateur consumer-all,
                    *   et pour qu'il ajoute l'entête destinataire avec la valeur « écrire » si le message
                    *   envoyé commence par la lettre « w ».
                    */
                    from("direct:consumer-all").choice().when(body().startsWith("w"))
			.when(header("destinataire").contains("ecrire")).to("direct:consumer-2")
                        .otherwise().to("direct:consumer-1");
                    
                /*****************************************************************************************************/
                    /**
                    * Get All
                    */
                    from("direct:Citymanager").setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .to("http://127.0.0.1:8084/rest-service/zoo-manager/animals").log("response received : ${body}");
                   
                /*****************************************************************************************************/
                    /***
                    * Get By Name : on a trouvé deux méthodes, la première utilise recipientList
                    * et la deuxième utilise setHeader 
                     */
                    String nameUrl = "${header.destinataire}";
                   
                     String url = "http://127.0.0.1:8084/rest-service/zoo-manager/find/byName/" + nameUrl;
                    from("direct:CitymanagerByName").setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .recipientList(simple(url))
                    .log("response received : ${body}");

                    from("direct:CitymanagerByName_1").setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .setHeader(Exchange.HTTP_PATH,simple(nameUrl))
                    .to("http://127.0.0.1:8084/rest-service/zoo-manager/find/byName/").log("response received : ${body}");
               
                /*****************************************************************************************************/
                    /**
                    * Geonames route
                    */
                    from("direct:Geonames").setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .recipientList(
			simple("http://api.geonames.org/search?username=guettouche&name=${header.destinataire}"))
			.log("response received : ${body}");

                    from("direct:Geonames_1").setHeader(Exchange.HTTP_METHOD, constant("GET"))
			.setHeader(Exchange.HTTP_PATH,simple("search?username=guettouche&name=${header.destinataire}"))
			.to("http://api.geonames.org/")
			.log("response received : ${body}");

		
                }

            };

            // On ajoute la route au contexte
            routeBuilder.addRoutesToCamelContext(context);
            // On démarre le contexte pour activer les routes
            context.start();

            // On crée un producteur
            ProducerTemplate pt = context.createProducerTemplate();
            // qui envoie un message au consommateur 'consumer-1'
            
            Scanner sc = new Scanner(System.in);
            System.out.println("Veuillez saisir un mot :");
            String str = sc.nextLine();
            int comparaison = str.compareTo("exit");
            /*Question 1_b: 
                on Arrête le programme lorsqu'on entre le mot-clé « exit »
            */
            while (comparaison != 0){
                /*Question 1_a: 
                    envoyer des messages au consommateur 'consumer-1 partir de l'entrée standard System.in.
                */
               // pt.sendBody("direct:consumer-1", str);
                /**
		* envoyer Body System.in et Header destinataire :
		*/
		//pt.sendBodyAndHeader("direct:consumer-all", str, "destinataire", str);
                
                /*Question 3: 
                    L'exemple suivant fait un appel avec la méthode GET au service publié à 
                    l'adresse 127.0.0.1 sur le port 8084 est qui affiche la liste des animaux détailler
                    trouvant dans la BDD
                */
               // pt.sendBody("direct:Citymanager", str);
                
                /*Question 3_a: 
                    L'exemple suivant fait un appel avec la méthode GET au service publié à 
                    l'adresse 127.0.0.1 sur le port 8084 est qui affiche les données dun animal passer en entrée standard
                    
                */
              // pt.sendBodyAndHeader("direct:CitymanagerByName", null, "destinataire", str);
               // pt.sendBodyAndHeader("direct:CitymanagerByName_1", null, "destinataire", str);
                
                /*Question 3_b: 
                    L'exemple suivant fait un appel avec la méthode GET au service publié à 
                    l'adresse 127.0.0.1 sur le port 8084 est  qu'il retourne la position géographique 
                    des zoos où se trouvent les animaux recherchés.

                */
                //pt.sendBodyAndHeader("direct:Geonames", null, "destinataire", str);
                pt.sendBodyAndHeader("direct:Geonames_1", null, "destinataire", str);
                
                
                
                
                System.out.println("Ce message a etre envoyer au consumer : " + str);

                sc = new Scanner(System.in);
                System.out.println("Veuillez saisir un mot : ");
                str = sc.nextLine();
                comparaison = str.compareTo("exit");
                 
            }
            sc.close();
            System.out.println("Fin de la connexion.");

        }
}
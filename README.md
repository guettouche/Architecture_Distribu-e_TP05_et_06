# Architecture_Distribu-e_TP05_et_06
Architecture distribuée TP 5 et 6 Réalisé par : GUETTOUCHE Islam Houmor Yacine M1GIL Gr03

#Objectifs

		Ce TP présente les EIP (Enterprise Integration Patterns) les plus communément rencontrés. Ce 
	système repose sur l'échange de messages (constitués d'un corps « body » et d'entêtes « headers »)
	entre des points d'accès (end points) « producteurs » et « consommateurs » à travers des « routes ». 
	
#Service Developper

	On a mit en place 2 classes "ProducerConsumer.java" et "Plusieur.java".
       
"ProducerConsumer.java"

        /*Question 1_a: 
                    envoyer des messages au consommateur 'consumer-1 partir de l'entrée standard System.in.
        */
         pt.sendBody("direct:consumer-1", str);
		 
        /**
		* envoyer Body System.in et Header destinataire :
		*/
		pt.sendBodyAndHeader("direct:consumer-all", str, "destinataire", str);
                
        /*Question 3: 
            L'exemple suivant fait un appel avec la méthode GET au service publié à 
            l'adresse 127.0.0.1 sur le port 8084 est qui affiche la liste des animaux détailler
            trouvant dans la BDD
        */
        pt.sendBody("direct:Citymanager", str);
                
        /*Question 3_a: 
            L'exemple suivant fait un appel avec la méthode GET au service publié à 
            l'adresse 127.0.0.1 sur le port 8084 est qui affiche les données dun animal passer en entrée standard
                    
        */
         pt.sendBodyAndHeader("direct:CitymanagerByName", null, "destinataire", str);
         pt.sendBodyAndHeader("direct:CitymanagerByName_1", null, "destinataire", str);
                
        /*Question 3_b: 
            L'exemple suivant fait un appel avec la méthode GET au service publié à 
            l'adresse 127.0.0.1 sur le port 8084 est  qu'il retourne la position géographique 
            des zoos où se trouvent les animaux recherchés.

        */
        pt.sendBodyAndHeader("direct:Geonames", null, "destinataire", str);
        pt.sendBodyAndHeader("direct:Geonames_1", null, "destinataire", str);
		
        Afin d'utiliser d'interargire avec cette classe il faut decommenter le service voulu et fair un Run:
              - Ecrire sur l'entrée standard.
              - Le système récupère cette chaine la traite et répond a cette requette.
              - Pour couper la connection il suffit de taper "exit".
			  
"Plusieur.java"
      
        On a implémenté cette classe pour exécuter plusieurs instances de Tomcat a la fois
		

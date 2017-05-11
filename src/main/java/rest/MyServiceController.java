package rest;

import model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@EnableAutoConfiguration
public class  MyServiceController {


    public static void main(String[] args) throws Exception {
        System.getProperties().put( "server.port", "8080");
        SpringApplication.run(MyServiceController.class, args);
    }
    private Center center;

    public MyServiceController() {
        // Creates a new center
        center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");
        // And fill it with some animals
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
                ))
        );

        center.getCages().addAll(Arrays.asList(usa, amazon));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // /animals
    @RequestMapping(path = "/animals", method = GET, produces = APPLICATION_JSON_VALUE)
    public Center getAnimals(){
        return center;
    }

    @RequestMapping(path = "/animals", method = POST, produces = APPLICATION_JSON_VALUE)
    public Center addAnimal(@RequestBody Animal animal) throws CageNotFoundException {
        boolean success = this.center.getCages()
                .stream()
                .filter(cage -> cage.getName().equals(animal.getCage()))
                .findFirst()
                .orElseThrow(CageNotFoundException::new)
                .getResidents()
                .add(animal);
        if(success)return this.center;
        throw new IllegalStateException("Failing to add the animal while the input was valid and it's cage was existing is not suppose to happen.");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // /animals/id/{id}
    @RequestMapping(path = "/animals/id/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalById(@PathVariable UUID id) throws AnimalNotFoundException {
        return center.findAnimalById(id);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // /animals/name/{name}
    @RequestMapping(path = "/animals/name/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalByName(@PathVariable String name) throws AnimalNotFoundException {
        return center.findAnimalByName(name);
    }


    // /animals/findByName
    @RequestMapping(path = "/animals/findByName", method = POST, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalByName1(@RequestBody String name) throws AnimalNotFoundException {
        return center.findAnimalByName(name);
    }

}

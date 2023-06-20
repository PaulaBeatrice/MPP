package triathlon.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triathlon.model.Participant;
import triathlon.model.Result;
import triathlon.persistence.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/triathlon/participants")
public class TriathlonParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;


    @RequestMapping( method= RequestMethod.GET)
    public Participant[] getAll(){
        System.out.println("Get all participants ...");
        List<Participant> participantList = new ArrayList<>();
        for (Participant r : participantRepository.getAll()) {
            participantList.add(r);
        }
        return participantList.toArray(new Participant[participantList.size()]);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        Participant participant= participantRepository.findOne(id);
        if (participant==null)
            return new ResponseEntity<String>("Participant not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Participant create(@RequestBody Participant participant){
        participantRepository.save(participant);
        System.out.println("Participant added");
        return participant;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Participant update(@RequestBody Participant participant) {
        System.out.println("Updating participant ...");
        participantRepository.update(participant);
        return participant;
    }

    @RequestMapping(value= "/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting participant ... "+ id);
        try {
            participantRepository.delete(id);
            return new ResponseEntity<Participant>(HttpStatus.OK);
        }catch (Exception ex){
            System.out.println("Ctrl Delete participant exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}


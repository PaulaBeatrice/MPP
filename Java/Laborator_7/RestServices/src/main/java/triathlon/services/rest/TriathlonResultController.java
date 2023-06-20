package triathlon.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triathlon.model.Result;
import triathlon.persistence.ResultRepository;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/triathlon/results")
public class TriathlonResultController {

    @Autowired
    private ResultRepository resultRepository;


    @RequestMapping( method= RequestMethod.GET)
    public Result[] getAll(){
        System.out.println("Get all results ...");
        List<Result> resultList = new ArrayList<>();
        for (Result r : resultRepository.getAll()) {
            resultList.add(r);
        }
        return resultList.toArray(new Result[resultList.size()]);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        Result result=resultRepository.findOne(id);
        if (result==null)
            return new ResponseEntity<String>("Result not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Result>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result create(@RequestBody Result result){
        resultRepository.save(result);
        System.out.println("Result added");
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Result result) {
        System.out.println("Updating result ...");
        resultRepository.update(result);
        return result;
    }

    @RequestMapping(value= "/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting result ... "+ id);
        try {
            resultRepository.delete(id);
            return new ResponseEntity<Result>(HttpStatus.OK);
        }catch (Exception ex){
            System.out.println("Ctrl Delete result exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}

package lecturesystem.LectureSystem.rest;

import lecturesystem.LectureSystem.service.RootService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootResource {
    private RootService rootService;

    public RootResource(RootService rootService){
        this.rootService = rootService;
    }
    @GetMapping("")
    public ResponseEntity<String> getRootContent(){
        String response = rootService.getRootMessage();
        return ResponseEntity.ok(response);
    }
}

package lecturesystem.LectureSystem.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("")
    public ResponseEntity<String> getRootContent(){
        String content = "Hello world from root";
        return ResponseEntity.ok(content);
    }
}

package lecturesystem.LectureSystem.service;

import org.springframework.stereotype.Service;

@Service
public class RootService {

    public String getRootMessage(){
        return "Hello world from root!";
    }

}

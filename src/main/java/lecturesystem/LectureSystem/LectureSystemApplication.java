package lecturesystem.LectureSystem;

import lecturesystem.LectureSystem.common.course.Course;
import lecturesystem.LectureSystem.common.course.TUMOnlineCourseQuery;
import lecturesystem.LectureSystem.common.course.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LectureSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LectureSystemApplication.class, args);
    }

    /* Clone courses from TUMOnline */
    @Bean
    CommandLineRunner commandLineRunner(CourseRepository courseRepository) {
        return args -> {
            Long startTime = System.currentTimeMillis();
            System.out.println("Start time: " + startTime);
            List<Course> courseList = TUMOnlineCourseQuery.getCourses();

			for(Course c : courseList){
                System.out.println("Saving Course " + c.getCourseTitle());
                //TODO Prevent doubles.
				courseRepository.save(c);
			}
            Long endTime = System.currentTimeMillis();
            System.out.println("End time: " + endTime);
            System.out.println("Done saving all Courses from TUMOnline.");
            System.out.println("It took: " + (endTime-startTime) + "ms.");
        };
    }
     /**/
}

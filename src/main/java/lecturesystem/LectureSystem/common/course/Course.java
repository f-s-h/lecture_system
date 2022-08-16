package lecturesystem.LectureSystem.common.course;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name="course")
public class Course {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            //How much does the sequence increase
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    @Column(nullable = true)
    private int tumId;
    @Column(nullable = false)
    private String courseTitle;

    @Column(nullable = false)
    private String courseType;

    @Column(nullable = false)
    private String courseLanguage;

    @Column(nullable = false, columnDefinition="TEXT")
    private String courseDescription;

    @Column(nullable = false)
    private String courseLecturer;

    @Column(nullable = false)
    private String semesterDesignation;

    public Course() {

    }

    public Course(int tumId, String courseTitle, String courseType, String courseLanguage, String courseDescription, String courseLecturer, String semesterDesignation) {
        this.tumId = tumId;
        this.courseTitle = courseTitle;
        this.courseType = courseType;
        this.courseLanguage = courseLanguage;
        this.courseDescription = courseDescription;
        this.courseLecturer = courseLecturer;
        this.semesterDesignation = semesterDesignation;
    }

    public Long getId() {
        return id;
    }

    public int getTumId() {
        return tumId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getCourseLecturer() {
        return courseLecturer;
    }

    public String getSemesterDesignation() {
        return semesterDesignation;
    }
}


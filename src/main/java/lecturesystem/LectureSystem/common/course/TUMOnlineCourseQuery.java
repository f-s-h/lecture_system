package lecturesystem.LectureSystem.common.course;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUMOnlineCourseQuery {
    private static final int PAGE_NUMBERS = 1;

    private static final int COURSES_PER_PAGE = 20;

    private static final String COURSE_SITE_BASE = "https://campus.tum.de/tumonline/ee/rest/slc.tm.cp/student/courses/";

    private static final String BASE_URL_INFORMATICS_COURSES = "https://campus.tum.de/tumonline/ee/rest/slc.tm.cp/student/courses?$filter=courseNormKey-eq=LVEAB;curriculumVersionId-eq=1304;orgId-eq=1;termId-eq=196&$orderBy=title=ascnf&$skip=";

    private static final String BASE_URL_ALL_COURSES = "https://campus.tum.de/tumonline/ee/rest/slc.tm.cp/student/courses?$filter=courseNormKey-eq=LVEAB;orgId-eq=1;termId-eq=196&$orderBy=title=ascnf&$skip=";

    private static final String URL_END = "&$top=";

    public static List<Course> getCourses(){
        String allCoursesString = getAllCoursesString();
        List<Course> courses = getCoursesFromString(allCoursesString);
        return courses;
    }

    private static String getAllCoursesString(){
        String coursesString = "";

        for(int currentSite = 0; currentSite < PAGE_NUMBERS; currentSite++) {
            URL url = createURL(BASE_URL_INFORMATICS_COURSES + currentSite * COURSES_PER_PAGE + URL_END + COURSES_PER_PAGE);
            coursesString += getSiteString(url);
        }

        return coursesString;
    }

    private static List<Course> getCoursesFromString(String courseString){
        List<Integer> courseIds = getCourseIds(courseString);
        List<Course> courseList = new ArrayList<>();

        for (int courseId : courseIds){
            Course course = getCourseById(courseId);
            courseList.add(course);
            System.out.println("Loaded course : " + course.getCourseTitle());
        }

        return courseList;
    }

    private static URL createURL(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }
        catch (MalformedURLException e){
            System.err.println("Something with the construction of the URL for the TUMOnline getRequest went wrong.");
            e.printStackTrace();
        }
        return url;
    }

    private static String getSiteString(URL url){

        HttpURLConnection connection = openConnection(url);
        BufferedReader bufferedReader = openBufferedReaderFromConnection(connection);

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = readLine(bufferedReader)) != null) {
            response.append(inputLine + "\n");
        }

        disconnectConnection(connection);
        closeBufferedReader(bufferedReader);

        return response.toString();
    }

    private static List<Integer> getCourseIds(String result){
        Scanner scanner = new Scanner(result);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }

        List<String> idListUnfiltered = new ArrayList<>();
        for(String l : lines ){
            if(l.contains("<id>")){
                idListUnfiltered.add(l);
            }
        }

        List<String> idStringList = new ArrayList<>();
        for(int i = 0; i < idListUnfiltered.size(); i+= 1){
            idStringList.add(idListUnfiltered.get(i));
        }

        List<String> idAsStringList = new ArrayList<>();

        for(String l : idStringList){
            if(charNumberInString(l, ' ') == 12){
                l = l.replaceAll(" ", "");
                l = l.replaceAll("<id>", "");
                l = l.replaceAll("</id>", "");
                idAsStringList.add(l);
            }
        }

        List<Integer> idList = new ArrayList<>();

        for ( String s : idAsStringList){
            try{
                idList.add(Integer.parseInt(s));
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                System.err.println("Couldn't cast an element of the idList from a String to an Integer");
            }
        }

        System.out.println("Done writing!");
        return idList;
    }

    private static Course getCourseById(int id){
        URL url = createURL(COURSE_SITE_BASE + id);
        String result = getSiteString(url);
        Course course = createCourseFromString(result, id);

        return course;
    }

    private static HttpURLConnection openConnection(URL url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Sending request to URL: " + url);
            System.out.println("Response Code : " + responseCode);

        } catch (IOException e) {
            System.err.println("Couldn't open the connection of the given url: " + url.toString());
            e.printStackTrace();
        }

        return connection;
    }

    private static void disconnectConnection(HttpURLConnection connection){
        connection.disconnect();
    }

    private static BufferedReader openBufferedReaderFromConnection(HttpURLConnection connection){
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.err.println("Couldn't open a BufferedReader on the given URL-connection: " + connection.getURL().toString());
            e.printStackTrace();
        }
        return bufferedReader;
    }

    public static void closeBufferedReader(BufferedReader bufferedReader){
        try {
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Couldn't close a BufferedReader.");
            e.printStackTrace();
        }
    }

    private static String readLine(BufferedReader bufferedReader){
        String lineString = "";
        try {
            lineString =  bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println("Couldn't read the next line with the bufferedReader");
            e.printStackTrace();
        }
        return lineString;
    }

    private static Course createCourseFromString(String courseString, int id){
        String courseTitle = getElementByObjectName(courseString, "courseTitle");
        String courseType = getElementByObjectName(courseString, "courseTypeName");
        String courseLanguage = getElementByObjectName(courseString, "courseLanguageDtos");
        String courseDescription = getElementByObjectName(courseString, "cpCourseDescriptionDto");
        String semesterDesignation = getElementByObjectName(courseString, "semesterDesignation");
        String courseLecturer = getLecturer(courseString);
        Course course = new Course(id, courseTitle, courseType, courseLanguage, courseDescription, courseLecturer, semesterDesignation);
        return course;
    }

    private static int charNumberInString(String string, char ch){
        int counter = 0;
        char[] charArr = string.toCharArray();

        for ( char c : charArr ){
            if(c == ch) counter++;
        }

        return counter;
    }

    private static String getElementByObjectName(String s, String objectName){
        String element = "";
        String[] splitString = s.split(objectName);
        if(splitString.length >= 2){
            splitString = splitString[1].split("value");
        }
        if(splitString.length >= 2) {
            element = splitString[1];
        }
        element = element.replaceAll("<", "");
        element = element.replaceAll(">", "");
        element = element.replaceAll("/", "");
        return element;
    }

    private static String getLecturer(String s){
        String lecturerName = "";
        String[] splitString = s.split("lectureships");
        if(splitString.length >= 2){
            String[] splitFirstNameString = s.split("firstName");
            String[] splitLastNameString = s.split("lastName");

            if(splitFirstNameString.length >= 2 && splitLastNameString.length >= 2){
                String firstName = splitFirstNameString[1].replaceAll(" ", "");
                firstName = firstName.replaceAll("</", "");
                firstName = firstName.replaceAll(">", "");
                String lastName = splitLastNameString[1].replaceAll(" ", "");
                lastName = lastName.replaceAll("</", "");
                lastName = lastName.replaceAll(">", "");

                lecturerName = firstName + " " + lastName;
            }

        }
        return lecturerName;
    }
}

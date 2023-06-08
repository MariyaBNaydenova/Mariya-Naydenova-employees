import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.nio.file.NoSuchFileException;

public class Main {

    public static void main(String[] args) {

        // filenames for testing purposes:
        // test.csv
        // nonexistingfile.csv
        // employees.csv
        // oneEmployeeFile.csv
        String FILE_NAME = "employees.csv";
        HashMap<Integer, List<Project>> employees = readEmployeesFromFile(FILE_NAME);

        if (employees != null || employees.size() > 0) {
            if(employees.size() > 2) {
                String result = findResult(employees);
                System.out.println(result);
            } else {
                System.out.println("File contains less than 2 employees.");
            }
        }
    }

    private static String findResult(HashMap<Integer, List<Project>> employees) {
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.addAll(employees.keySet());

        Integer firstEmployee = null;
        Integer secondEmployee = null;
        long daysTogether = 0;

        for(int i = 0; i < employeeIds.size()-1; i++) {
            for(int j = i+1; j < employeeIds.size(); j++) {
                long days = findNumberOfDaysTogether(employees.get(employeeIds.get(i)), employees.get(employeeIds.get(j)));

                if(days > daysTogether) {
                    firstEmployee = employeeIds.get(i);
                    secondEmployee = employeeIds.get(j);
                    daysTogether = days;
                }
            }
        }

        if(daysTogether == 0) return "No employees worked together on any project.";
        return firstEmployee.intValue() + ", " + secondEmployee.intValue() + ", " + daysTogether;
    }

    private static long findNumberOfDaysTogether(List<Project> projectsFirst, List<Project> projectsSecond) {
        long daysTogether = 0;
        Set<Integer> commonProjects = new HashSet<>();
        for(Project p1 : projectsFirst) {
            for(Project p2 : projectsSecond) {
                if(p1.getProjectId() == p2.getProjectId() && !commonProjects.contains(p1.getProjectId())) {
                    daysTogether += daysTogetherOnProject(p1, p2);
                    commonProjects.add(p1.getProjectId());
                }
            }
        }
        return daysTogether;
    }

    private static long daysTogetherOnProject(Project p1, Project p2) {
        int daysTogether = 0;
        LocalDateTime startDate;
        LocalDateTime endDate;

        if(p1.getFrom().isAfter(p2.getFrom())) startDate = p1.getFrom();
        else startDate = p2.getFrom();

        if(p1.getTo().isBefore(p2.getTo())) endDate = p1.getTo();
        else endDate = p2.getTo();

        if(startDate.isBefore(endDate))
            return ChronoUnit.DAYS.between(startDate, endDate);
        return 0;
    }

    public static HashMap<Integer, List<Project>> readEmployeesFromFile(String filePath) {
        HashMap<Integer, List<Project>> employees = new HashMap<>();
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            for (String line : allLines) {
                String[] data = line.split(", ");
                Integer employeeId = Integer.valueOf(data[0]);
                Project project = createProject(data);
                setProjectToEmployee(employeeId, project, employees);
            }
        } catch (NoSuchFileException e) {
            System.out.println("Requested file does not exist.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        System.out.println("File format is not as expected.");
        }
        return employees;
    }

    private static Project createProject(String[] data) {
        Project project = new Project();
        project.setProjectId(Integer.parseInt(data[1]));
        project.setFrom(convertStringToDate(data[2]));
        if(data[3].equals("NULL")) project.setTo(LocalDate.now().atStartOfDay());
        else project.setTo(convertStringToDate(data[3]));
        return project;
    }

    private static void setProjectToEmployee(Integer employeeId, Project project,  HashMap<Integer, List<Project>> employees) {
        if(employees.get(employeeId) != null) {
            List<Project> projects = employees.get(employeeId);
            projects.add(project);
        } else {
            List<Project> projects = new ArrayList<>();
            projects.add(project);
            employees.put(employeeId, projects);
        }
    }

    private static LocalDateTime convertStringToDate(String dateString) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, dtf);
        return localDate.atStartOfDay();
    }

}
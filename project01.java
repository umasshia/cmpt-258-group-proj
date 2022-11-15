//Giorgi Samushia & James Curry 
//CMPT 258 Project 1

package cmpt258.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Cmpt258Project {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File i = new File("C:\\Users\\giorg\\OneDrive\\Desktop\\first project Databases\\instructor.txt");
        File d = new File("C:\\Users\\giorg\\OneDrive\\Desktop\\first project Databases\\department.txt");
        ArrayList<Instructor> instructors = new ArrayList<>();
        ArrayList<Department> departments = new ArrayList<>();
        putInstructorsInList(instructors, i);
        putDepartmentsInList(departments, d);
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("\n1. Get instructor information.");
            System.out.println("2. Get department information.");
            System.out.println("3. Insert a record about a new instructor.");
            System.out.println("4. Delete a record about an instructor.");
            System.out.println("5. Exit");
            System.out.print("\nEnter your selection: ");
            String choice = input.nextLine();
            switch(choice) {
                case "1": 
                    System.out.print("Enter the instructor ID: ");
                    String instructor_id = input.nextLine();
                    getInstructorInfo(instructor_id, instructors, departments); 
                    break;
                case "2": 
                    System.out.print("Please enter the department name: ");
                    String department_name = input.nextLine();
                    getDepartmentInfo(department_name, departments, instructors);
                    break;
                case "3": 
                    System.out.print("Please enter the instructor ID: ");
                    instructor_id = input.nextLine();
                    System.out.print("Please enter the instructor's name: ");
                    String instructor_name = input.nextLine();
                    System.out.print("Please enter the affilitated department name: ");
                    department_name = input.nextLine();
                    addInstructor(i, instructors, departments, instructor_id, instructor_name, department_name);
                    break;
                case "4": 
                    System.out.print("Please enter the ID of the instructor you wish to delete: ");
                    instructor_id = input.nextLine();
                    deleteInstructor(i, instructors, departments, instructor_id);
                    break;
                case "5": 
                    System.out.println("Thank you and goodbye!");
                    return;
                default:
                    System.out.println("Invalid input, try again:");
                    break;
            }
        }
    }
    
    public static void putInstructorsInList(ArrayList<Instructor> instructors, File i) throws FileNotFoundException{
        Scanner in = new Scanner(i);
        while(in.hasNext()) {
            String str = in.nextLine();
            String instructor_id = "";
            String instructor_name = "";
            String department = "";
            String[] splitted = str.split(",");
            instructor_id = splitted[0];
            instructor_name = splitted[1];
            department = splitted[2];
            instructors.add(new Instructor(instructor_id, instructor_name, department));
        }
    } 
    
    public static void putDepartmentsInList(ArrayList<Department> departments, File d) throws FileNotFoundException {
        Scanner in = new Scanner(d);
        while(in.hasNext()) {
            String str = in.nextLine();
            String department_name = "";
            String location = "";
            String budget = "";
            String[] splitted = str.split(",");
            department_name = splitted[0];
            location = splitted[1];
            budget = splitted[2];
            departments.add(new Department(department_name, location, budget));
        }
    }
    
    public static void getInstructorInfo(String instructor_id, ArrayList<Instructor> instructors, ArrayList<Department> departments) {
        int instructorIndex = searchForInstructor(instructor_id, instructors);
        if(instructorIndex >= 0) {
            int departmentIndex = searchForDepartment(instructors.get(instructorIndex).getDepartment(), departments);
            System.out.println("Instructor information:");
            System.out.println("Name: " + instructors.get(instructorIndex).getInstructorName());
            System.out.println("Department: " + instructors.get(instructorIndex).getDepartment());
            System.out.println("Department Location: " + departments.get(departmentIndex).getLocation());
        }
        else 
            System.out.println("The ID doesnot appear in the file.");
    }
    
    public static void getDepartmentInfo(String department_name, ArrayList<Department> departments, ArrayList<Instructor> instructors) {
        int departmentIndex = searchForDepartment(department_name, departments);
        if(departmentIndex >= 0) {
            System.out.println("Department information:");
            System.out.println("Location: " + departments.get(departmentIndex).getLocation());
            System.out.println("Budget: $" + departments.get(departmentIndex).getBudget());
            System.out.println("Instructors working for this department:");
            for(int i = 0; i < instructors.size(); i++) {
                if(instructors.get(i).getDepartment().equals(department_name)) 
                    System.out.println(instructors.get(i).getInstructorName());
            }
        }
        else
            System.out.println("The department name does not appear in the file.");
    }
    
    public static int searchForInstructor(String instructor_id, ArrayList<Instructor> instructors) {
        for (int i = 0; i < instructors.size(); i++) {
            if(instructors.get(i).getInstructorID().equals(instructor_id))
                return i;
        }
        return -1;
    }
    
    public static int searchForDepartment(String department_name, ArrayList<Department> departments) {
        for (int i = 0; i < departments.size(); i++) {
            if(departments.get(i).getDepartmentName().equals(department_name))
                return i;
        }
        return -1;
    }
    
    public static void addInstructor(File i, ArrayList<Instructor> instructors, ArrayList<Department> departments, String instructor_id, String instructor_name, String department_name) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(i, true);
        PrintWriter write = new PrintWriter(fw);
        if(instructorExists(instructors, instructor_id))
            System.out.println("\nInstructor ID already exists in the file.");       
        else if(!departmentExists(departments, department_name)) 
            System.out.println("\nThe department does not exist and hence the instructor record cannot be added to the database.");
        else {
            instructors.add(new Instructor(instructor_id, instructor_name, department_name));
            overwrite(instructors, i);
            System.out.println("\nInstructor has been added.");
        }
    }
    
    public static void deleteInstructor(File i, ArrayList<Instructor> instructors, ArrayList<Department> departments, String instructor_id) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(i, true);
        PrintWriter write = new PrintWriter(fw);
        int instructorIndex = searchForInstructor(instructor_id, instructors);
        if(!instructorExists(instructors, instructor_id))
            System.out.println("\nThe ID does not appear in the file."); 
        else {
            instructors.remove(instructorIndex);     
            overwrite(instructors, i);
            System.out.println("\nInstructor has been deleted."); 
        }
    }
    
    public static void overwrite(ArrayList<Instructor> instructors, File i) throws FileNotFoundException, IOException{
        FileWriter overwrite = new FileWriter(i);
        PrintWriter write = new PrintWriter(overwrite);
        for(int j = 0; j <= instructors.size(); j++) {
            if (j == instructors.size())
                write.println("");
            else
                write.println(instructors.get(j).getInstructorID() + "," + instructors.get(j).getInstructorName() + "," + instructors.get(j).getDepartment());
        }
        write.close();
    }
    
    public static boolean departmentExists(ArrayList<Department> departments, String department_name) {
        for (int i = 0; i < departments.size(); i++) {
            if(departments.get(i).getDepartmentName().equals(department_name))
                return true;
        }
        return false;
    }
    
    public static boolean instructorExists(ArrayList<Instructor> instructors, String instructor_id) {
        for (int i = 0; i < instructors.size(); i++) {
            if(instructors.get(i).getInstructorID().equals(instructor_id))
                return true;
        }
        return false;
    }
}

class Instructor {
        private String instructor_id;
        private String instructor_name;
        private String department_name;
        public Instructor() {        
        }
        public Instructor(String instructor_id, String instructor_name, String department_name) {
            this.instructor_id = instructor_id;
            this.instructor_name = instructor_name;
            this.department_name = department_name;
        }
        public String getInstructorID() {
            return instructor_id;
        }
        public String getInstructorName() {
            return instructor_name;
        }
        public String getDepartment() {
            return department_name;
        }
        public void setInstructorID(String instructor_id) {
            this.instructor_id = instructor_id;
        }
        public void setInstructorName(String instructor_name) {
            this.instructor_name = instructor_name;
        }
        public void setDepartment(String department_name) {
            this.department_name = department_name;
        }
    }
    
    class Department {
        private String department_name;
        private String location;
        private String budget;
        public Department() {
        }
        public Department(String department_name, String location, String budget) {
            this.department_name = department_name;
            this.location = location;
            this.budget = budget;
        }
        public String getDepartmentName() {
            return department_name;
        }
        public String getLocation() {
            return location;
        }
        public String getBudget() {
            return budget;
        }
        public void setDepartmentName(String department_name) {
            this.department_name = department_name;
        }
        public void setLocation(String location) {
            this.location = location;
        }
        public void setBudget(String budget) {
            this.budget = budget;
        }
    }

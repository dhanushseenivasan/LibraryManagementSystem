package com.lms.login;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import com.lms.dao.DatabaseService;
import com.lms.dao.LoginDao;
import com.lms.service.BookService;
import com.lms.service.StudentService;

public class LoginService {
	
	Scanner sc = new Scanner(System.in);
	
	public void doLogin() throws ClassNotFoundException, SQLException{
		
		System.out.println("Please provide username");
		String userName = sc.nextLine();
		
		System.out.println("Please provide password");
		String password = sc.nextLine();
		
		try(Connection conn = DatabaseService.getConnection()){
			LoginDao loginDao = new LoginDao();
			
			String userType = loginDao.doLogin(conn, userName, password);
			
			if(userType == null) {
				System.out.println("Invalid User");
				return;
			}
			System.out.println("Login Success. You logged in as a " + userType +" . Please select from the options below : ");
			
			if(userType.equals("admin")) {
				displayAdminMenu(conn);
			}
			else {
				displayStudentMenu(conn);
			}
			
		}
	
	}
	
	public void displayAdminMenu(Connection conn) throws SQLException {
		
		BookService bookService = new BookService();
		StudentService studentService = new StudentService();
		int choice;
		
		do {
		System.out.println("=========================");
		System.out.println(" 1 . Search a Book. ");
		System.out.println(" 2 . Add new Book. ");
		System.out.println(" 3 . Upgrade Quantity of a Book. ");
		System.out.println(" 4 . Show All Books. ");
		System.out.println(" 5 . Register Student. ");
		System.out.println(" 6 . Show All Registered Students. ");
		System.out.println(" 7 . Exit From Application. ");
		System.out.println("=========================");

		System.out.println("Please enter your choice : ");
		choice = sc.nextInt();
		
		switch(choice) {
		case 1:
			searchBook(conn);
			break;
		case 2:
			bookService.addBook(conn);
			break;
		case 3:
			bookService.updateBookQty(conn);
			break;
		case 4:
			bookService.getAllBooks(conn);
			break;
		case 5:
			studentService.addStudent(conn);
			break;
		case 6:
			studentService.getAllStudents(conn);
			break;
		case 7:
			System.out.println("Thank you for using Library Management System");
			break;
		default:
			System.out.println("Please Enter valid Option");
		}
		}
		while(choice!=7);		
}
public void displayStudentMenu(Connection conn) throws SQLException {
		
		BookService bookService = new BookService();
		StudentService studentService = new StudentService();
		int choice;
		
		do {
		System.out.println("=========================");
		System.out.println(" 1 . Search a Book. ");
		System.out.println(" 2 . Check out Book. ");
		System.out.println(" 3 . Check in Book. ");
		System.out.println(" 4 . Exit From Application. ");
		System.out.println("=========================");

		System.out.println("Please enter your choice : ");
		choice = sc.nextInt();
		
		switch(choice) {
		case 1:
			searchBook(conn);
			break;
		case 2:
			bookService.checkOutBook(conn);
			break;
		case 3:
			bookService.checkInBook(conn);
			break;
		case 4:
			System.out.println("Thank you for using Library Management System");
			break;
		default:
			System.out.println("Please Enter valid Option");
		}	
		}
		while(choice!=4);		
}

	private void searchBook(Connection conn) throws SQLException {
		
		BookService bookService = new BookService();
		System.out.println(" 1 . Search with Book's Serial Number. ");
		System.out.println(" 2 . Search with Book's Author Name. ");
		
		System.out.println("Please enter your choice : ");
		int choice = sc.nextInt();
		
		switch(choice) {
		case 1:
			bookService.searchBySrNo(conn);
			break;
		case 2:
			bookService.searchByAuthorName(conn);
		}
	}
}

package com.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.lms.dao.BookDao;
import com.lms.dao.StudentDao;
import com.lms.dto.Book;

public class StudentService {
	Scanner sc = new Scanner(System.in);
	
	public void searchBySrNo(Connection conn) throws SQLException {
		System.out.println("Enter Serial Number of Book : ");
		int srNo = sc.nextInt();
		
		BookDao dao = new BookDao(); 
		Book book = dao.getBooksBySerialNumber(conn, srNo);
		
		if(book != null) {
			System.out.println("=== Book Details ===");
			System.out.println("Serial Number : "+ book.getSrNo()+" Book Name : "+book.getBookName()+" Author Name :"+book.getAuthorName());
		}
		else {
			System.out.println("No Book for Serial Number : "+srNo+" Found.");

		}
	}
	
	public void addStudent(Connection conn) throws SQLException {
		System.out.println("Enter Student Name: ");
		String studentName = sc.nextLine();
		
		System.out.println("Enter Registration Number: ");
		String regNo = sc.nextLine();
		
		StudentDao dao = new StudentDao();
		boolean isStdExist = dao.getStudentByRegNo(conn, regNo);
	
		if(isStdExist) {
			System.out.println("Student details exists in the system. Please save with other book");
			return;
		}
		
		dao.saveStudent(conn, studentName, regNo);
	}
	
	public void getAllStudents(Connection conn) throws SQLException {
		StudentDao dao = new StudentDao();
		dao.getAllStudents(conn);
	}
	
}

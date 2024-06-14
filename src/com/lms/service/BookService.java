package com.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.lms.dao.BookDao;
import com.lms.dao.StudentDao;
import com.lms.dto.Book;
import com.lms.dto.BookingDetails;

public class BookService {
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
	public void searchByAuthorName(Connection conn) throws SQLException {
		System.out.println("Enter Author Name of Book : ");
		
		String authorName = sc.nextLine();
		
		BookDao dao = new BookDao(); 
		Book book = dao.getBooksByAuthorName(conn, authorName);
		
		if(book != null) {
			System.out.println("=== Book Details ===");
			System.out.println("Serial Number : "+ book.getSrNo()+" Book Name : "+book.getBookName()+" Author Name :"+book.getAuthorName());
		}
		else {
			System.out.println("No Book for Author Name : "+authorName+" Found.");

		}
	}
	public void addBook(Connection conn) throws SQLException {
		System.out.println("Enter Serial Number of Book: ");
		int srNo = sc.nextInt();
		sc.nextLine();

		System.out.println("Enter Book Name: ");
		String bookName = sc.nextLine();
		
		System.out.println("Enter Author Name: ");
		String authorName = sc.nextLine();
		
		System.out.println("Enter Quantity of Book: ");
		int qty = sc.nextInt();
		
		BookDao dao = new BookDao();
		Book book = dao.getBooksBySnoOrBookName(conn, authorName, srNo);
	
		if(book != null) {
			System.out.println("Book details exists in the system. Please save with other book");
			return;
		}
		
		Book input =  new Book();
		input.setAuthorName(authorName);
		input.setBookName(bookName);
		input.setBookQty(qty);
		input.setSrNo(srNo);
		
		dao.saveBook(conn, input);
	}
	
	public void getAllBooks(Connection conn) throws SQLException {
		BookDao dao = new BookDao();
		List<Book> books = dao.getAllBooks(conn);
		
		System.out.println("+------------+--------------------+----------------------+------------+");
		System.out.println("| Book Sr No | Name               | Author Name          |  Quantity  |");
		System.out.println("+------------+--------------------+----------------------+------------+");
		
		for(Book book : books) {
			System.out.printf("| %-10s | %-18s | %-20s | %-10s | \n",book.getSrNo(),book.getBookName(),book.getAuthorName(),book.getBookQty());
			System.out.println("+------------+--------------------+----------------------+------------+");
			//System.out.println(book.getSrNo()+"           "+book.getBookName()+"             "+book.getAuthorName());
		}
	}
	public void updateBookQty(Connection conn) throws SQLException {
		System.out.println("Enter Serial Number of Book: ");
		int srNo = sc.nextInt();
		
		BookDao dao = new BookDao();
		Book book = dao.getBooksBySerialNumber(conn, srNo);
	
		if(book == null) {
			System.out.println("Book not available");
			return;
		}
		
		System.out.println("Enter number of books to be added: ");
		int qty = sc.nextInt();
		
		Book input =  new Book();
		
		input.setBookQty(book.getBookQty() + qty);
		input.setSrNo(srNo);
		
		dao.updateBookQuantity(conn, input);
	}
	public void checkOutBook(Connection conn) throws SQLException {
		StudentDao dao = new StudentDao();
		
		System.out.println("Enter Reg Number : ");
		String regNum = sc.nextLine();
		
		boolean isExist = dao.getStudentByRegNo(conn, regNum);
		
		if(!isExist) {
			System.out.println("Student is not registered. Get registered first. ");
			return;
		}
		getAllBooks(conn);
		
		System.out.println("Enter Serial No of Book to be checked out :");
		int srNo = sc.nextInt();
		
		
		BookDao bookDao = new BookDao();
		Book book =bookDao.getBooksBySerialNumber(conn, srNo);
		
		if(book == null) {
			System.out.println("Book is not available");
			return;
		}
		
		book.setBookQty(book.getBookQty() - 1);
		
		int id = dao.getStudentByRegNo_(conn, regNum);
		
		dao.saveBookingDetails(conn, id, book.getId(),1);
		
		bookDao.updateBookQuantity(conn, book);
	}
	
	public void checkInBook(Connection conn) throws SQLException {

		StudentDao dao = new StudentDao();
		
		System.out.println("Enter Reg Number : ");
		String regNum = sc.nextLine();
		
		boolean isExist = dao.getStudentByRegNo(conn, regNum);
		
		if(!isExist) {
			System.out.println("Student is not registered. Get registered first. ");
			return;
		}
		
		int id = dao.getStudentByRegNo_(conn, regNum);
		List<BookingDetails> bookingDetails =dao.getBookDetailsId(conn, id);
		
		bookingDetails.stream().forEach(b -> System.out.println(b.srNo + "\t\t\t" +b.bookName + " \t\t\t" +b.authorName));
	
		System.out.println("Enter the Serial Number to be checked in : ");
		int sNo = sc.nextInt();
	
		BookingDetails filterDetails = bookingDetails.stream().filter(b -> b.getSrNo() == sNo).findAny().orElse(null);
	
		BookDao bookDao = new BookDao();
		Book book = bookDao.getBooksBySerialNumber(conn, sNo);
		book.setBookQty(book.getBookQty() + 1);
		
		bookDao.updateBookQuantity(conn, book);
		dao.deleteBookingDetails(conn, filterDetails.getId());
	}
}

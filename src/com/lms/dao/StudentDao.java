package com.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lms.dto.Book;
import com.lms.dto.BookingDetails;

public class StudentDao {
	
	public boolean getStudentByRegNo(Connection conn, String regNo) throws SQLException {
		String query= "select * from students where reg_no =?";
		
		try(PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1, regNo);
			
			try(ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	public int getStudentByRegNo_(Connection conn, String regNo) throws SQLException {
		String query= "select * from students where reg_no =?";
		
		try(PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1, regNo);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}
	
	
	public void saveStudent(Connection conn, String studentName, String regNo) throws SQLException {
		
		String query = "insert into students(std_name, reg_no) values (?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1, studentName);
			ps.setString(2, regNo);
			
			int rows = ps.executeUpdate();
			
			if(rows > 0) {
				System.out.println("Student added successfully");
			}
			else {
				System.out.println("Failed to add student.");
			}
			
		}
	}
	public void getAllStudents(Connection conn) throws SQLException {
		String query= "select * from students";
		
		System.out.println("+------------+--------------------+----------------+");
		System.out.println("| Id         | Student Name       | Reg No         |");
		System.out.println("+------------+--------------------+----------------+");
		
		List<Book> books = new ArrayList<>();
		try(PreparedStatement ps = conn.prepareStatement(query)){
		
			try(ResultSet rs = ps.executeQuery()){
			
				while(rs.next()) {
					System.out.printf("| %-10s | %-18s | %-14s | \n", rs.getInt(1), rs.getString(2), rs.getString(3));
					System.out.println("+------------+--------------------+----------------+");
				}
			}
		}
	}
	public void updateBookQuantity(Connection conn, Book book) throws SQLException {
		String query = "update books set qty = ? where sr_no = ?";
		
		try(PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1,book.getBookQty());
			ps.setInt(2,book.getSrNo());
			
			int rows = ps.executeUpdate();
			
			if(rows > 0) {
				System.out.println("Book updated successfully");
			}
			else {
				System.out.println("Failed to update book.");
			}
		}
	}
public void saveBookingDetails(Connection conn, int stdId, int bookId, int qty) throws SQLException {
		
		String query = "insert into booking_details(std_id, book_id, qty) values (?, ?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, stdId);
			ps.setInt(2, bookId);
			ps.setInt(3, qty);
			
			int affectedrows = ps.executeUpdate();
			
			if(affectedrows > 0) {
				System.out.println("Booking details added successfully");
			}
			else {
				System.out.println("Failed to add Booking details.");
			}
			
		}
	}
	
	public List<BookingDetails> getBookDetailsId(Connection conn, int stdId) throws SQLException{
		
		String query = "select * from booking_details bd "
				+ "inner join books b on b.id = bd.book_id "
				+ "where bd.std_id = ?";
		
		List<BookingDetails> bookingDetails = new ArrayList<>();
		
		try(PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, stdId);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				BookingDetails bookingDetail = new BookingDetails();
				
				bookingDetail.setAuthorName(rs.getString("author_name"));
				bookingDetail.setBookId(rs.getInt("book_id"));
				bookingDetail.setBookName(rs.getString("name"));
				bookingDetail.setQty(rs.getInt("qty"));
				bookingDetail.setStdId(rs.getInt("std_id"));
				bookingDetail.setSrNo(rs.getInt("sr_no"));
				bookingDetail.setId(rs.getInt("id"));
			}
		}
		return bookingDetails;
}
	public void deleteBookingDetails(Connection conn, int id) throws SQLException {
		String query = "delete from booking_details where id = ?";
		
		try(PreparedStatement ps =conn.prepareStatement(query)){
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}


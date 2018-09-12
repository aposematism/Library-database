/*
 * LibraryModel.java
 * Author: Jordan Milburn
 * Created on:
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class LibraryModel {

    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    
    //Connection related fields.
    Connection connection = null;
    String url1 = "jdbc:postgresql:";
    String url2 = "//db.ecs.vuw.ac.nz:5432/";
    String url3 = "_jdbc";

    public LibraryModel(JFrame parent, String userid, String password) {
    	dialogParent = parent;
    	try {
    		Class.forName("org.postgresql.Driver");
		}
    	catch (ClassNotFoundException e) {
			System.out.println("We could not find the Postgres Driver!");
			e.printStackTrace();
    	}
    	try {
    		String url = url1+url2+userid+url3;
    		System.out.println(url);
    		connection = DriverManager.getConnection(url, userid, password);
    		connection.setAutoCommit(false);
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    		System.out.println("Can not connect to SQL database!");
    		System.out.println(e.getMessage());
    	}
    	if(connection == null) {
    		System.out.println("The Connection failed!");
    	}
    }
    
    /** 
     * Returns the book associated with a specific ISBN.
     * 
     * It should return the duplicates as well.
     * */
    public String bookLookup(int isbn) {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM BOOK WHERE isbn = "+isbn;
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(bookSelect);
			if(!rs.next()) {
				return "Some error occured, we could not find book associated with that ISBN!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int i = rs.getInt(1);
				String s = rs.getString(2);
				short z = rs.getShort(3);
				short noc = rs.getShort(4);
				short nl = rs.getShort(5);
				result += "ISBN: " + i + ", Due Date: " + s.toString() + ", CustomerID: " + z + ", Number of Copies: " + noc + " Number Left: " + nl + " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    /** 
     * Returns the catalogue of books.
     * */
    public String showCatalogue() {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM BOOK";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any items in the book catalogue!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int i = rs.getInt(1);
				String s = rs.getString(2);
				short z = rs.getShort(3);
				short noc = rs.getShort(4);
				short nl = rs.getShort(5);
				result += "ISBN: " + i + ", Due Date: " + s.toString() + ", CustomerID: " + z + ", Number of Copies: " + noc + " Number Left: " + nl + " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /** 
     * Returns the complete list of loaned books.
     * */
    public String showLoanedBooks() {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM Cust_Book";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any items in loaned books!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int i = rs.getInt(1);
				Date s = rs.getDate(2);
				int z = rs.getInt(3);
				result += "ISBN: " + i + ", Due Date: " + s.toString() + ", CustomerID: " + z +" \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    public String showAuthor(int authorID) {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM AUTHOR WHERE authorID = ?";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setInt(1, authorID);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any authors with that authorID!!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int aID = rs.getInt(1);
				String first = rs.getString(2);
				String surname = rs.getString(3);
				result += "AuthorID: " + aID + ", First: " + first + ", Surname: " + surname + " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    /** 
     * Returns all authors.
     * */
    public String showAllAuthors() {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM AUTHOR";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any authors!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int authorID = rs.getInt(1);
				String first = rs.getString(2);
				String surname = rs.getString(3);
				result += "AuthorID: " + authorID + ", First: " + first + ", Surname: " + surname + " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    public String showCustomer(int customerID) {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM CUSTOMER WHERE CustomerId = ?";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setInt(1, customerID);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any customers with that customer ID!!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int custID = rs.getInt(1);
				String first = rs.getString(2);
				String surname = rs.getString(3);
				String city = rs.getString(4);
				result += "CustomerID: " + custID + ", First: " + first + ", Surname: " + surname + "City: " + city+ " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    public String showAllCustomers() {
    	String result = "";
    	try {
			String bookSelect = "SELECT * FROM CUSTOMER";
			PreparedStatement stmt = connection.prepareStatement(bookSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return "Some error occured, we could not find any customers!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				int custID = rs.getInt(1);
				String first = rs.getString(2);
				String surname = rs.getString(3);
				String city = rs.getString(4);
				result += "CustomerID: " + custID + ", First: " + first + ", Surname: " + surname + "City: " + city+ " \n";
			}
			closeResultSet(rs);
			closeStatement(stmt);
		}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    /** 
     * This might seem a little excessive to include so much in this method.
     * It should update book then insert a new value into cust_book.
     * 
     * */
    public String borrowBook(int isbn, int customerID, int day, int month, int year) {
    	String result = ""; String result1 = "";
    	ResultSet customerSet = null; ResultSet bookSet = null;
    	String date = year + "-" + month + "-" + day;
    	String selectCustomer = "SELECT * FROM Customer WHERE CustomerId = " + customerID + "FOR UPDATE;";
		String selectBook = "SELECT * FROM Book WHERE ISBN = " + isbn + " FOR UPDATE;";
		String bookInsert = "INSERT INTO Cust_Book (CustomerId, DueDate, ISBN) VALUES (" + customerID + ",'" + date + "'," + isbn + ");";
		String updateBook = "UPDATE book SET numleft = NumLeft - 1 WHERE ISBN = " + isbn + ";";
		int nc = Integer.parseInt(getNumCopy(isbn));
		int nl = Integer.parseInt(getNumLeft(isbn));
		if(nc < 1 || nl < 1) {
			return "Book is unavailable as there are no copies available!";
		}
    	try {
    		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    		customerSet = stmt.executeQuery(selectCustomer);
    		customerSet.beforeFirst();
    		if(customerSet == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!customerSet.next()) {
    			return "We do not have any customer associated with that customerID!";
    		}
    		bookSet = stmt.executeQuery(selectBook);
    		if(bookSet == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!bookSet.next()) {
    			return "We do not have any book associated with that ISBN!";
    		}
    		connection.createStatement().executeUpdate(updateBook);
    		connection.createStatement().executeUpdate(bookInsert);
    		connection.commit();
    		closeResultSet(customerSet);
    		closeResultSet(bookSet);
			closeStatement(stmt);
    		return "Book Successfully borrowed!";
    	}
    	catch(SQLException e) {
    		try {
				connection.rollback();
				return "We have rolled back the commit due to SQLExceiption";
			} 
    		catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.getMessage();
    	}
    	return "Internal Error: Book failed to borrow!";
    }
    
    /** 
     * This returns the number of copies of a book.
     * */
    private String getNumCopy(int isbn) {
    	String result = "";
    	try {
    		String bookSelect = "SELECT * FROM BOOK WHERE isbn = "+isbn;
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(bookSelect);
			if(!rs.next()) {
				return "Some error occured, we could not find any books associated with that ISBN!";
			}
			rs.beforeFirst();
			while(rs.next()) {

				short noc = rs.getShort(4);
				result = String.valueOf(noc);
			}
			closeResultSet(rs);
			closeStatement(stmt);
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /** 
     * /This is used in borrow book to check the remaining number of books.
     * */
    private String getNumLeft(int isbn) {
    	String result = "";
    	try {
    		String bookSelect = "SELECT * FROM BOOK WHERE isbn = "+isbn;
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(bookSelect);
			if(!rs.next()) {
				return "Some error occured, we could not find any books associated with that ISBN!";
			}
			rs.beforeFirst();
			while(rs.next()) {
				short nl = rs.getShort(5);
				result = String.valueOf(nl);
			}
			closeResultSet(rs);
			closeStatement(stmt);
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    /** 
     * This method returns a book which has been loaned out.
     * */
    public String returnBook(int isbn, int customerID) {
    	String result = ""; String result1 = "";
    	ResultSet customerSet = null; ResultSet bookSet = null;
    	String selectCustomer = "SELECT * FROM Customer WHERE CustomerId = " + customerID + "FOR UPDATE;";
		String selectBook = "SELECT * FROM Book WHERE ISBN = " + isbn + " FOR UPDATE;";
		String bookDelete = "DELETE FROM Cust_Book WHERE ISBN = " + isbn + " AND CustomerID = " + customerID;
		String updateBook = "UPDATE book SET numleft = NumLeft + 1 WHERE ISBN = " + isbn + ";";
		int nc = Integer.parseInt(getNumCopy(isbn));
		int nl = Integer.parseInt(getNumLeft(isbn));
		if(nl+1 > nc) {
			return "You can't return a book to us when we haven't loaned it out!";
		}
    	try {
    		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    		customerSet = stmt.executeQuery(selectCustomer);
    		customerSet.beforeFirst();
    		if(customerSet == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!customerSet.next()) {
    			return "We do not have any customer associated with that customerID!";
    		}
    		bookSet = stmt.executeQuery(selectBook);
    		if(bookSet == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!bookSet.next()) {
    			return "We do not have any book associated with that ISBN!";
    		}
    		connection.createStatement().executeUpdate(updateBook);
    		connection.createStatement().executeUpdate(bookDelete);
    		connection.commit();
    		closeResultSet(customerSet);
    		closeResultSet(bookSet);
			closeStatement(stmt);
    		return "Book Successfully returned!";
    	}
    	catch(SQLException e) {
    		try {
				connection.rollback();
				return "We have rolled back the commit due to SQLExceiption";
			} 
    		catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.getMessage();
    	}
    	return "Internal Error: Book failed to borrow!";
    }

    /** 
     * This method closes down the database connection.
     * */
    public void closeDBConnection() {
    	if (connection != null) {
            try {
            	connection.close();
            } 
            catch (SQLException e) { 
            	e.printStackTrace();
            	System.out.println("System did not close properly!");
            }
        }
    }
    
    /** 
     * This method deletes a single customer from the customer table.
     * */
    public String deleteCus(int customerID) {
    	ResultSet customerSet = null;
    	String selectCustomer = "SELECT * FROM customer WHERE customerID = " + customerID + " FOR UPDATE";
    	String deleteCustomer = "DELETE FROM Customer WHERE CustomerId = " + customerID + ";";
    	try {
    		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    		customerSet = stmt.executeQuery(selectCustomer);
    		customerSet.beforeFirst();
    		if(customerSet == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!customerSet.next()) {
    			return "We do not have any customer associated with that customerID!";
    		}
    		connection.createStatement().executeUpdate(deleteCustomer);
    		connection.commit();
    		closeResultSet(customerSet);
			closeStatement(stmt);
    		return "Customer was successfully deleted";
    	}
    	catch(SQLException e) {
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.printStackTrace();
    	}
    	return "Delete Customer failed due to internal error!";
    }
    
    /** 
     * This method deletes a single author from the book author.
     * */
    public String deleteAuthor(int authorID) {
    	ResultSet authorCheck = null;
    	String selectAuthor = "SELECT * FROM author WHERE authorID = "+authorID;
    	String deleteAuthor = "DELETE FROM author WHERE authorId = " + authorID + ";";
    	try {
    		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    		authorCheck = stmt.executeQuery(selectAuthor);
    		authorCheck.beforeFirst();
    		if(authorCheck == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!authorCheck.next()) {
    			return "We do not have any author associated with that authorID!";
    		}
    		connection.createStatement().executeUpdate(deleteAuthor);
    		connection.commit();
    		closeResultSet(authorCheck);
			closeStatement(stmt);
    		return "Author was successfully deleted";
    	}
    	catch(SQLException e) {
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.printStackTrace();
    	}
    	return "Delete Author failed due to internal error!";
    }
    
    /** 
     * This method deletes a single book from the book table.
     * */
    public String deleteBook(int isbn) {
    	ResultSet bookCheck = null;
    	String selectBook = "SELECT * FROM book WHERE isbn = " + isbn + " FOR UPDATE";
    	String deleteBook = "DELETE FROM book WHERE isbn = " + isbn + ";";
    	try {
    		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    		bookCheck = stmt.executeQuery(selectBook);
    		bookCheck.beforeFirst();
    		if(bookCheck == null) {
    			System.out.println("Error during SQL processing.");
    		}
    		if(!bookCheck.next()) {
    			return "We do not have any book associated with that isbn!";
    		}
    		connection.createStatement().executeUpdate(deleteBook);
    		connection.commit();
    		closeResultSet(bookCheck);
			closeStatement(stmt);
    		return "Book was successfully deleted";
    	}
    	catch(SQLException e) {
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		e.printStackTrace();
    	}
    	return "Delete Book failed due to internal error!";
    }
    
    //This just closes the ResultSet, which is supposed to be best practice.
    private void closeResultSet(ResultSet resultSet) {
    	  if (resultSet != null) {
    	    try {
    	    	resultSet.close();
    	    }
    	    catch (SQLException ex) {
    	    	ex.getMessage();
    	    	ex.printStackTrace();
    	    }
    	    catch (Throwable ex) {
    	    	ex.getMessage();
    	    	ex.printStackTrace();
    	    }
    	  }
    }
    //This just closes the received statement, which is supposed to be best practice.
    private void closeStatement(Statement s) {
    	if(s != null) {
    		try {
    			s.close();
    		}
    		catch(SQLException ex) {
    			ex.getMessage();
    	    	ex.printStackTrace();
    		}
    		catch (Throwable ex) {
    	    	ex.getMessage();
    	    	ex.printStackTrace();
    	    }
    	}
    }
}
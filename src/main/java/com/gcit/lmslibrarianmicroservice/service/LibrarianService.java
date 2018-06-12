package com.gcit.lmslibrarianmicroservice.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lmslibrarianmicroservice.dao.BookDAO;
import com.gcit.lmslibrarianmicroservice.dao.LibraryBookCopiesDAO;
import com.gcit.lmslibrarianmicroservice.dao.LibraryBranchDAO;
import com.gcit.lmslibrarianmicroservice.entity.Book;
import com.gcit.lmslibrarianmicroservice.entity.LibraryBookCopies;
import com.gcit.lmslibrarianmicroservice.entity.LibraryBranch;

@RestController
@RequestMapping("/lmsspringboot/")
public class LibrarianService {

	@Autowired
	LibraryBranchDAO lbdao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	LibraryBookCopiesDAO lbcdao;
	
	private LibraryBranch getBranchByID(List<LibraryBranch> branches, int branchId) {
		for (LibraryBranch libraryBranch: branches) {
			if (libraryBranch.getBranchId() == branchId) {
				return libraryBranch;
			}
		}
		return null;
	}
	
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/librarians/branches/{branchId}", 
		method = RequestMethod.PATCH, 
		produces = "application/json")
	public LibraryBranch editBranch(@PathVariable int branchId, 
			@RequestParam(value="name", required=false) String branchName,
			@RequestParam(value="address", required=false) String branchAddress)
			throws SQLException {
		LibraryBranch branch = null;
		try {
			List<LibraryBranch> branches = lbdao.getAllBranches();
			LibraryBranch libraryBranch = this.getBranchByID(branches, branchId);
			if (branchName != null && branchName.trim().length() != 0) {
				lbdao.updateBranchName(libraryBranch, branchName);
			}
			if (branchAddress != null && branchAddress.trim().length() != 0) {
				lbdao.updateBranchAddress(libraryBranch, branchAddress);
			}
			branches = lbdao.getAllBranches();
			branch = this.getBranchByID(branches, branchId);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/librarians/branches/{branchId}/books/{bookId}/copies", 
		method = RequestMethod.PATCH, 
		produces = "application/json")
	public LibraryBookCopies updateCopiesOfBookInBranch(@RequestBody LibraryBookCopies lbc) throws SQLException{
		LibraryBookCopies lbco = null;
		try {
			lbcdao.updateBookCopies(lbc);
			lbco = lbcdao.getAllCopiesOfBookInBranch(lbc.getBranchId(), lbc.getBookId()).get(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lbco;
	}
	
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/librarians/branches/{branchId}/books", 
			method = RequestMethod.POST, 
			produces = "application/json")
	public LibraryBookCopies addNewBookToBranch(@RequestBody LibraryBookCopies lbc) throws SQLException{
		LibraryBookCopies lbco = new LibraryBookCopies();
		try {
			lbcdao.saveBranchBook(lbc);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lbco;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/librarians/branches/books", 
		method = RequestMethod.GET, 
		produces = "application/json")
	public List<Book> getAllBooksToAddToBranch() throws SQLException{
		List<Book> lb = new ArrayList<>();
		try {
			lb = bdao.readAllBooks();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lb;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/librarians/branches/{branchId}/books/{bookId}/copies", 
			method = RequestMethod.GET, 
			produces = "application/json")
	public LibraryBookCopies getCopiesOfBookInBranch(@PathVariable int branchId, 
			@PathVariable int bookId) throws SQLException{
		LibraryBookCopies lbco = null;
		try {
			List<LibraryBookCopies> libraryBookBranches = lbcdao.getAllCopiesOfBookInBranch(branchId, bookId);
			if (libraryBookBranches == null || libraryBookBranches.size() == 0) {
				lbco = new LibraryBookCopies();
				lbco.setNoOfCopies(0);
			} else {
				lbco = libraryBookBranches.get(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return lbco;
	}

}

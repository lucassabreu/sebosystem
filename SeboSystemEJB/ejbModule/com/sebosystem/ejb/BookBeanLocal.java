package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

@Local
public interface BookBeanLocal {

    public Book save(Book book) throws Exception;

    public Book getBookByOid(long oid);

    /**
     * Retrieves all the books in the database
     * 
     * @return
     */
    public List<Book> getAllBooks();

    /**
     * Get the books in the database based at the offset paramaters
     * 
     * @param offset
     * @param maxResults
     * @return
     */
    public List<Book> getAllBooks(int offset, int maxResults);

    /**
     * Retrieve the number of books in the system
     * 
     * @return
     */
    public long getAllBooksCount();

    /**
     * Retrieve books that begins with the <code>title</code> parameter
     * 
     * @param title
     *            Parameter for fitler
     * @param offset
     * @param maxResults
     * @return
     * 
     * @see Book#getTitle()
     */
    public List<Book> getBooksByTitle(String title, int offset, int maxResults);

    /**
     * Get the number of books where the title beggining with <code>title</code>
     * parameter
     * 
     * @param title
     * @return
     */
    public long getBooksByTitleCount(String title);

    /**
     * Retrieves all the books where the author name begins with the
     * <code>authorName</code> parameter
     * 
     * @param authorName
     * @param offset
     * @param maxResults
     * @return
     * 
     * @see Author#getName()
     */
    public List<Book> getBooksByAuthorName(String authorName, int offset, int maxResults);

    /**
     * Get the number of books where the its author's name begins with the
     * <code>authorName</code> parameter
     * 
     * @param authorName
     * @return
     */
    public long getBooksByAuthorNameCount(String authorName);

    /**
     * Retrieves all the books who was released at the year of the parameter (
     * <code>year</code>)
     * 
     * @param year
     * @param offset
     * @param maxResults
     * @return
     * 
     * @see Book#getYear()
     */
    public List<Book> getBooksByYear(int year, int offset, int maxResults);

    /**
     * Get the number of book that was relased at the parameter year
     * 
     * @param year
     * @return
     */
    public long getBooksByYearCount(int year);

    /**
     * Retrieves books that has the phrase of the paramater
     * <code>fragment</code> in a <code>Review</code>
     * 
     * @param fragment
     * @param offset
     * @param maxResults
     * @return
     * 
     * @see Review#getReview()
     */
    public List<Book> getBooksByReview(String fragment, int offset, int maxResults);

    /**
     * Get the number of books that has a Review with the <code>fragment</code>
     * 's parameter text
     * 
     * @param authorName
     * @return
     */
    public long getBooksByReviewCount(String fragment);

    /**
     * Retrieves books that has the phrase of the paramater
     * <code>fragment</code> in a <code>Excerpt</code>
     * 
     * @param fragment
     * @param offset
     * @param maxResults
     * @return
     * 
     * @see Excerpt#getExcerpt()
     */
    public List<Book> getBooksByExcerpt(String fragment, int offset, int maxResults);

    /**
     * Get the number of books that has a Exceprpt with the
     * <code>fragment</code>'s parameter text
     * 
     * @param authorName
     * @return
     */
    public long getBooksByExcerptCount(String fragment);

    public Book remove(Book book);

    public List<Book> getBooksByAuthor(Author author);

    public Copy getCopyByUserAndBook(User user, Book model);

    public Copy addBookToUser(Book model, User principal) throws Exception;

    public Copy removeBookOfUser(Book model, User principal) throws Exception;

    public List<Review> getReviewsOfBook(Book model);

    public List<Excerpt> getExcerptsOfBook(Book model);

    public Review getReviewByOid(long oid);

    public Excerpt getExcerptByOid(long oid);

    public Book rateBook(Book book, User user, int rating) throws Exception;

}

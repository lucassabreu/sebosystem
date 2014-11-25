package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.RequestType;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;
import com.sebosystem.exception.SeboException;

@Local
public interface BookBeanLocal {

    /**
     * Save and validate the book
     * 
     * @param book
     * @return
     * @throws Exception
     */
    public Book save(Book book) throws SeboException;

    /**
     * Remove the book
     * 
     * @param book
     * @return
     */
    public Book remove(Book book);

    /**
     * Create a <code>{@link RequestType#BookDuplicated}</code> </code>
     * {@link Request}</code> for the </code>book</code> parameter
     * 
     * @param book
     * @return The new {@link Request} created
     * @throws SeboException
     * 
     * @see Request
     * @see RequestType
     * @see Book
     */
    public Request reportDuplicated(Book book) throws SeboException;

    /**
     * Merge all the entries into {@code booksToMerge} parameter into one
     * {@link Book} with all the relatioships
     * 
     * @param booksToMerge
     * @return
     */
    public Book merge(List<Book> booksToMerge);

    /**
     * Save the <code>rating</code> of a <code>user</code> about a
     * <code>book</code>
     * 
     * @param book
     * @param user
     * @param rating
     * @return
     * @throws Exception
     * 
     * @see {@link BookBeanLocal#getCopyByUserAndBook(User, Book)}
     */
    public Book rateBook(Book book, User user, int rating) throws Exception;

    /**
     * {@link CopyBeanLocal#addBookToUser(Book, User)}
     * 
     * @param model
     * @param principal
     * @return
     * @throws Exception
     * 
     */
    public Copy addBookToUser(Book model, User principal) throws Exception;

    /**
     * {@link CopyBeanLocal#removeBookOfUser(Book, User)}
     * 
     * @param model
     * @param principal
     * @return
     * @throws Exception
     */
    public Copy removeBookOfUser(Book model, User principal) throws Exception;

    /**
     * {@link CopyBeanLocal#getCopyByUserAndBook(User, Book)}
     * 
     * @param user
     * @param model
     * @return
     */
    public Copy getCopyByUserAndBook(User user, Book model);

    /**
     * Retrieve the book with the <code>oid</code>'s parameter value
     * 
     * @param oid
     * @return
     */
    public Book getBookByOid(long oid);

    /**
     * {@link ReviewBeanLocal#getReviewByOid(long)}
     * 
     * @param oid
     * @return
     * 
     */
    public Review getReviewByOid(long oid);

    /**
     * {@link ReviewBeanLocal#getReviewsOfBook(Book)}
     * 
     * @param model
     * @return
     */
    public List<Review> getReviewsOfBook(Book model);

    /**
     * {@link ExcerptBeanLocal#getExcerptByOid(long)}
     * 
     * @param oid
     * @return
     */
    public Excerpt getExcerptByOid(long oid);

    /**
     * {@link ExcerptBeanLocal#getExcerptsOfBook(Book)}
     * 
     * @param model
     * @return
     */
    public List<Excerpt> getExcerptsOfBook(Book model);

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

    /**
     * Retrives books based in the parameter <code>author</code>
     * 
     * @param author
     * @return
     */
    public List<Book> getBooksByAuthor(Author author);

    /**
     * Do actions over the {@link Book} when it was {@link Request} was canceled
     * 
     * @param request
     * @throws SeboException
     */
    public void cancelBookDuplicated(Request request) throws SeboException;

    /**
     * Do actions over the books related into the {@link Request}
     * 
     * @param request
     * @throws Exception 
     */
    public void acceptBookDuplicated(Request request) throws Exception;

}

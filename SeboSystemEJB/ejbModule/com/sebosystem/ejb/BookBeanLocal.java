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

    public List<Book> getAllBooks();

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

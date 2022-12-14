/**
 * Title: BookDao.java
 * Abstract: contains all the SQL statements for auto generated Java functions for BookDatabase.java
 * Author: Jalen Banks
 * ID:1012
 * Date of Completion: 12/08/22
 */
package edu.csumb.bank1435.pr3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface BookDao
{
    @Insert
    void insert(Book... books);

    @Insert
    void insert(Account... accounts);

    @Insert
    void insert(Log Log);

    @Update
    void update(Book book);

    @Update
    void update(Account account);

    @Update
    void update(List<Book> books);

    @Update
    void updateAllAccounts(List<Account> account);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM Books")
    List<Book> getAll();

    @Query("SELECT * FROM Books Where title = :title")
    List<Book> searchbyTitle(String title);

    @Query("SELECT * FROM Accounts Where name = :n")
    List<Account> accountSearchByName(String n);

    @Query("SELECT * FROM Books Where author = :a")
    List<Book> searchByAuthor(String a);

    @Query("SELECT * FROM Accounts")
    List<Account> getAllAccounts();

    @Query("SELECT * FROM Books Where avaliable = :ava")
    List<Book> getAllAvaliableBooks(boolean ava);

    @Query("SELECT * FROM Accounts Where hasHold = :hh")
    List<Account> getAccountsHolds(boolean hh);

    @Query("SELECT * FROM Books Where rentedBy = :rb")
    List<Book> getRentedBooksName(String rb);

    @Query("SELECT * FROM Accounts Where isActive = :act")
    List<Account> getActiveAccount(String act);

    @Query("SELECT * FROM Logs")
    List<Log> getLogs();
}
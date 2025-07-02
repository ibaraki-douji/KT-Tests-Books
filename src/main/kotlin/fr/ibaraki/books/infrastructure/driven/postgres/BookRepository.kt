package fr.ibaraki.books.infrastructure.driven.postgres

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookRepository(val sql: NamedParameterJdbcTemplate) : IBookRepository {


    override fun listBooks(): List<Book> {
        return sql.query("SELECT * FROM books") { rs, _ ->
            Book(rs.getString("title"), rs.getString("author"))
        }
    }

    override fun save(book: Book) {
        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
            throw IllegalArgumentException("Title and author must not be empty")
        }
        sql.update(
            "INSERT INTO books (title, author) VALUES (:title, :author)",
            mapOf("title" to book.getTitle(), "author" to book.getAuthor())
        )
    }

    override fun clear() {
        sql.update("DELETE FROM books", emptyMap<String, Any>())
    }
}
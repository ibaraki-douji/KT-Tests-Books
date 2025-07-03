package fr.ibaraki.books.infrastructure.driven.postgres

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookRepository(val sql: NamedParameterJdbcTemplate) : IBookRepository {


    override fun listBooks(): List<Book> {
        return sql.query("SELECT * FROM books") { rs, _ ->
            Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getBoolean("reserved")
            )
        }
    }

    override fun save(book: Book) {
        require(book.getTitle().isNotEmpty() && book.getAuthor().isNotEmpty()) {
            "Title and author must not be empty"
        }
        sql.update(
            "INSERT INTO books (title, author) VALUES (:title, :author)",
            mapOf("title" to book.getTitle(), "author" to book.getAuthor())
        )
    }

    override fun reserveBook(bookId: Long): Book? {
        val book = (sql.query(
            "SELECT * FROM books WHERE id = :id AND reserved IS FALSE",
            mapOf(
                "id" to bookId
            )
        ) { rs, _ ->
            Book(rs.getString("title"), rs.getString("author")).apply {
                setId(rs.getLong("id"))
                setReserved(true)
            }
        }).firstOrNull()

        if (book != null) {
            sql.update(
                "UPDATE books SET reserved = true WHERE id = :id",
                mapOf("id" to bookId)
            )
        } else {
            return null
        }

        return book
    }

    override fun clear() {
        sql.update("DELETE FROM books", emptyMap<String, Any>())
    }
}

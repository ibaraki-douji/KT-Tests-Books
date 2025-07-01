package fr.ibaraki.books.infrastructure.driven.postgres

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import org.springframework.stereotype.Service

@Service
class BookRepository : IBookRepository {
    val books: MutableList<Book> = mutableListOf()


    override fun listBooks(): List<Book> {
        return books
    }

    override fun save(book: Book) {
        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
            throw IllegalArgumentException("Title and author must not be empty")
        }
        books.add(book)
    }
}
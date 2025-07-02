package fr.ibaraki.books.domain.usecase

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: IBookRepository) {

    fun createBook(title: String, author: String): Book {
        require(title.isNotEmpty() && author.isNotEmpty()) {
            "Title and author must not be empty"
        }

        val book = Book(title, author)
        bookRepository.save(book)
        return book
    }

    fun listBooks(): List<Book> {
        return bookRepository.listBooks()
            .sortedBy { e -> e.getTitle() }
    }

}

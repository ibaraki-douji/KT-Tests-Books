package fr.ibaraki.books.domain.port

import fr.ibaraki.books.domain.model.Book
import org.springframework.stereotype.Service

@Service
interface IBookRepository {

    fun listBooks(): List<Book>;
    fun save(book: Book);

}
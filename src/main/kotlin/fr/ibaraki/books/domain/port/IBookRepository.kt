package fr.ibaraki.books.domain.port

import fr.ibaraki.books.domain.model.Book

interface IBookRepository {

    fun listBooks(): List<Book>;
    fun save(book: Book);

}
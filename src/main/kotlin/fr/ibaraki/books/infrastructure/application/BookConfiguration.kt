package fr.ibaraki.books.infrastructure.application

import fr.ibaraki.books.domain.port.IBookRepository
import fr.ibaraki.books.domain.usecase.BookService
import fr.ibaraki.books.infrastructure.driven.postgres.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class BookConfiguration {

    @Bean
    fun bookRepository(): IBookRepository? {
        return BookRepository()
    }

    @Bean
    fun bookService(): BookService? {
        return BookService()
    }

}
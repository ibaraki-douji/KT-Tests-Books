package fr.ibaraki.books.infrastructure.application

import fr.ibaraki.books.domain.port.IBookRepository
import fr.ibaraki.books.domain.usecase.BookService
import fr.ibaraki.books.infrastructure.driven.postgres.BookRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


@Configuration
class BookConfiguration {


}
package fr.ibaraki.books.infrastructure.driving.controller

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.usecase.BookService
import fr.ibaraki.books.infrastructure.driving.controller.dto.BookDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

    @GetMapping("")
    fun getBooks(): List<Book> {
        return bookService.listBooks()
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody dto: BookDTO): ResponseEntity<Book> {
        if (dto.title.isEmpty() || dto.author.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        bookService.createBook(dto.title, dto.author)

        return ResponseEntity(HttpStatus.CREATED)
    }

}

package fr.ibaraki.books.infrastructure.driving.controller

import com.ninjasquad.springmockk.MockkBean
import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import fr.ibaraki.books.domain.usecase.BookService
import fr.ibaraki.books.infrastructure.driven.postgres.BookRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [BookController::class])
@ContextConfiguration(classes = [BookController::class, BookService::class, BookRepository::class])
class BookControllerTests : FunSpec() {

    @MockkBean
    private lateinit var bookRepository: IBookRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    init {
        extension(SpringExtension)

        test("getBooks should return list of books from service") {
            val books = listOf(
                Book("Kotlin in Action", "Dmitry Jemerov"),
                Book("Clean Code", "Robert Martin")
            )
            every { bookRepository.listBooks() } returns books
            mockMvc.get("/books").andExpect {
                status { isOk() }
                jsonPath("$[0].title") { value("Clean Code") }
                jsonPath("$[0].author") { value("Robert Martin") }
                jsonPath("$[1].title") { value("Kotlin in Action") }
                jsonPath("$[1].author") { value("Dmitry Jemerov") }

                jsonPath("$") { isArray() }
            }.andReturn()
        }

        test("Should add a book and return 201") {
            val book = Book("Effective Java", "Joshua Bloch")
            every { bookRepository.save(book) } returns Unit

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "${book.getTitle()}", "author": "${book.getAuthor()}"}"""
            }.andExpect {
                status { isCreated() }
            }
        }

        test("Should return 400 when book title or author is empty") {
            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "", "author": "Author"}"""
            }.andExpect {
                status { isBadRequest() }
            }

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "Title", "author": ""}"""
            }.andExpect {
                status { isBadRequest() }
            }
        }

    }
}
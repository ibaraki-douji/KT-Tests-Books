package fr.ibaraki.books.infrastructure.driven.postgres

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
@ActiveProfiles("testIntegration")
class BookRepositoryTests : FunSpec() {

    @Autowired
    private lateinit var bookRepository: IBookRepository

    init {
        extension(SpringExtension)

        beforeTest {
            val books = listOf(
                "Kotlin in Action" to "Dmitry Jemerov",
                "Clean Code" to "Robert Martin"
            )

            books.forEach { (title, author) ->
                bookRepository.save(Book(title, author))
            }
        }

        test("Should return list of Books from service") {
            val books = bookRepository.listBooks()
            books shouldHaveSize 2
            books[0].getTitle() shouldBe "Kotlin in Action"
            books[0].getAuthor() shouldBe "Dmitry Jemerov"
            books[1].getTitle() shouldBe "Clean Code"
            books[1].getAuthor() shouldBe "Robert Martin"
        }

        test("Should save a new book") {
            // Given
            val newBook = Book("Effective Java", "Joshua Bloch")

            // When
            bookRepository.save(newBook)

            // Then
            val books = bookRepository.listBooks()
            books shouldHaveSize 3

            val savedBook = books.find { it.getTitle() == "Effective Java" }
            savedBook shouldNotBe null
            savedBook?.getAuthor() shouldBe "Joshua Bloch"
        }

        test("Should save multiple books") {
            // Given
            val book1 = Book("Design Patterns", "Gang of Four")
            val book2 = Book("Refactoring", "Martin Fowler")

            // When
            bookRepository.save(book1)
            bookRepository.save(book2)

            // Then
            val books = bookRepository.listBooks()
            books shouldHaveSize 4

            val designPatternsBook = books.find { it.getTitle() == "Design Patterns" }
            val refactoringBook = books.find { it.getTitle() == "Refactoring" }

            designPatternsBook shouldNotBe null
            designPatternsBook?.getAuthor() shouldBe "Gang of Four"

            refactoringBook shouldNotBe null
            refactoringBook?.getAuthor() shouldBe "Martin Fowler"
        }

        test("Should clear all books") {
            // Given - books are already added in beforeTest
            val initialBooks = bookRepository.listBooks()
            initialBooks shouldHaveSize 2

            // When
            bookRepository.clear()

            // Then
            val booksAfterClear = bookRepository.listBooks()
            booksAfterClear.shouldBeEmpty()
        }

        test("Should return empty list when no books exist") {
            // Given
            bookRepository.clear()

            // When
            val books = bookRepository.listBooks()

            // Then
            books.shouldBeEmpty()
        }

        test("Should save book with special characters in title and author") {
            // Given
            val bookWithSpecialChars = Book("L'Étranger", "Albert Camus")

            // When
            bookRepository.save(bookWithSpecialChars)

            // Then
            val books = bookRepository.listBooks()
            val savedBook = books.find { it.getTitle() == "L'Étranger" }
            savedBook shouldNotBe null
            savedBook?.getAuthor() shouldBe "Albert Camus"
        }

        test("Should save book with long title and author") {
            // Given
            val longTitle = "A Very Long Title That Contains Many Words And Should Test The Database Field Limits"
            val longAuthor = "An Author With A Very Long Name That Should Also Test Database Limits"
            val bookWithLongFields = Book(longTitle, longAuthor)

            // When
            bookRepository.save(bookWithLongFields)

            // Then
            val books = bookRepository.listBooks()
            val savedBook = books.find { it.getTitle() == longTitle }
            savedBook shouldNotBe null
            savedBook?.getAuthor() shouldBe longAuthor
        }

        test("Should handle multiple clear operations") {
            // Given
            bookRepository.clear()
            bookRepository.listBooks().shouldBeEmpty()

            // When - calling clear again on empty repository
            bookRepository.clear()

            // Then - should still be empty without error
            bookRepository.listBooks().shouldBeEmpty()
        }

        afterTest {
            bookRepository.clear()
        }

        afterSpec {
            container.stop()
        }
    }
    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:16-alpine")
        init {
            container.start()
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }
    }


}
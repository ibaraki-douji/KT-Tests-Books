package fr.ibaraki.books.domain.usecase

import fr.ibaraki.books.domain.model.Book
import fr.ibaraki.books.domain.port.IBookRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BookServiceTests : FunSpec() {

    val mockRepository = mockk<IBookRepository>(relaxed = true)
    val bookService = BookService(mockRepository)

    init {

            val bookListArb = Arb.list(
                Arb.bind(
                    Arb.string(minSize = 1, maxSize = 50), // Title
                    Arb.string(minSize = 1, maxSize = 50)  // Author
                ) { title, author -> Book(title, author) },
                1..50
            )

            test("should add a book successfully") {
                val book = Book("Title", "Author")
                every { mockRepository.save(book) } returns Unit

                val result = bookService.createBook("Title", "Author")

                result.getTitle() shouldBe "Title"
                result.getAuthor() shouldBe "Author"
                verify { mockRepository.save(book) }
            }

            test("should throw exception if title or author is empty") {
                shouldThrow<IllegalArgumentException> { bookService.createBook("", "Author") }
                shouldThrow<IllegalArgumentException> { bookService.createBook("Title", "") }
            }

            test("should list all books in alphabetical order by title") {
                val books = listOf(
                    Book("Zebra", "Author1"),
                    Book("Apple", "Author2"),
                    Book("Monkey", "Author3")
                )
                every { mockRepository.listBooks() } returns books

                val result = bookService.listBooks()

                result shouldBe books.sortedBy { it.getTitle() }
            }

            test("should return all books when listing") {
                checkAll (bookListArb) { books ->
                    every { mockRepository.listBooks() } returns books

                    val result = bookService.listBooks()

                    result.size shouldBe books.size
                }
            }

        }

}
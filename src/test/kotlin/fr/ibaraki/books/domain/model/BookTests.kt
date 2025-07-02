package fr.ibaraki.books.domain.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class BookTests : FunSpec() {

    init {
        test("Book properties should be set and retrieved correctly") {
            val book = Book("1984", "George Orwell")
            book.getTitle() shouldBe "1984"
            book.getAuthor() shouldBe "George Orwell"

            book.setTitle("Animal Farm")
            book.setAuthor("Orwell")
            book.getTitle() shouldBe "Animal Farm"
            book.getAuthor() shouldBe "Orwell"
        }

        test("Books should be comparable by their string representation") {
            val book1 = Book("1984", "George Orwell")
            val book2 = Book("Animal Farm", "George Orwell")
            book1.compareTo(book2) shouldBe book1.toString().compareTo(book2.toString())
        }

        test("Books should be equal if their string representations are equal") {
            val book1 = Book("1984", "George Orwell")
            val book2 = Book("1984", "George Orwell")
            book1.equals(book2) shouldBe true
        }

        test("Book toString should return the correct format") {
            val book = Book("1984", "George Orwell")
            book.toString() shouldBe "Book(title='1984', author='George Orwell')"
        }

        test("should throw when no title is provided") {
            val exception = shouldThrow<IllegalArgumentException> {
                Book("", "George Orwell")
            }
            exception.message shouldBe "Title cannot be empty"
        }

        test("should throw when setting an empty title") {
            val book = Book("1984", "George Orwell")
            val exception = shouldThrow<IllegalArgumentException> {
                book.setTitle("")
            }
            exception.message shouldBe "Title cannot be empty"
        }

        test("should throw when setting an empty author") {
            val book = Book("1984", "George Orwell")
            val exception = shouldThrow<IllegalArgumentException> {
                book.setAuthor("")
            }
            exception.message shouldBe "Author cannot be empty"
        }
    }
}
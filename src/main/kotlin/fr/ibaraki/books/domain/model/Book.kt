package fr.ibaraki.books.domain.model

class Book : Comparable<Book> {

    private var title: String = ""
    private var author: String = ""

    constructor(title: String, author: String) {
        if (title.isEmpty()) {
            throw IllegalArgumentException("Title cannot be empty")
        }

        if (author.isEmpty()) {
            throw IllegalArgumentException("Author cannot be empty")
        }

        this.title = title
        this.author = author
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        if (title.isEmpty()) {
            throw IllegalArgumentException("Title cannot be empty")
        }
        this.title = title
    }

    fun getAuthor(): String {
        return author
    }

    fun setAuthor(author: String) {
        if (author.isEmpty()) {
            throw IllegalArgumentException("Author cannot be empty")
        }
        this.author = author
    }

    override fun compareTo(other: Book): Int {
        return this.toString().compareTo(other.toString())
    }

    override fun equals(other: Any?): Boolean {
        return this.toString().equals(other.toString())
    }

    override fun toString(): String {
        return "Book(title='$title', author='$author')"
    }
}

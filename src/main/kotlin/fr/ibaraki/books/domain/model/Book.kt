package fr.ibaraki.books.domain.model

class Book : Comparable<Book> {

    private var id: Long = 0L
    private var title: String = ""
    private var author: String = ""
    private var reserved: Boolean = false

    constructor(id: Long, title: String, author: String, reserved: Boolean = false) {
        require(title.isNotEmpty()) { "Title cannot be empty" }
        require(author.isNotEmpty()) { "Author cannot be empty" }


        this.id = id
        this.title = title
        this.author = author
        this.reserved = reserved
    }

    constructor(title: String, author: String, reserved: Boolean = false) : this(0L, title, author, reserved)

    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        require(id >= 0) { "ID cannot be negative" }
        this.id = id
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        require(title.isNotEmpty()) { "Title cannot be empty" }
        this.title = title
    }

    fun getAuthor(): String {
        return author
    }

    fun setAuthor(author: String) {
        require(author.isNotEmpty()) { "Author cannot be empty" }
        this.author = author
    }

    fun isReserved(): Boolean {
        return reserved
    }

    fun setReserved(reserved: Boolean) {
        this.reserved = reserved
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

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }
}

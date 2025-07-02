package fr.ibaraki.books.infrastructure.driving.controller.dto

data class BookDTO(var title: String = "", var author: String = "") {

    override fun toString(): String {
        return "BookDTO(title='$title', author='$author')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookDTO) return false

        if (title != other.title) return false
        if (author != other.author) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }

}

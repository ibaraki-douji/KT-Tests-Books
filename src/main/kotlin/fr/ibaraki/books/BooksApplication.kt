package fr.ibaraki.books

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Suppress("EmptyClassBlock")
class BooksApplication {}

fun main(args: Array<String>) {
	runApplication<BooksApplication>(*args)
}


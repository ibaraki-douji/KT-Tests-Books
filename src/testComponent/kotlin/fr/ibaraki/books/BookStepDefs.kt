package fr.ibaraki.books

import io.cucumber.java.Before
import io.cucumber.java.PendingException
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.datatable.DataTable
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.springframework.boot.test.web.server.LocalServerPort
import org.hamcrest.Matchers.*

class BookStepDefs {

    @LocalServerPort
    private var port: Int? = 0

    private lateinit var response: Response

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Given("the user creates the book {string} from {string}")
    @Throws(Exception::class)
    fun createData(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                {
                "title": "$title",
                "author": "$author"
                }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("the user retrieves all books")
    @Throws(Exception::class)
    fun getAllDatas() {
        response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
            .extract()
            .response()
    }

    @When("the user reserves the book {string}")
    @Throws(Exception::class)
    fun reserveBook(title: String) {
        this.getAllDatas()

        val bookId = response.jsonPath().getLong("find { it.title == '$title' }.id")

        response = given()
            .contentType(ContentType.JSON)
            .`when`()
            .post("/books/" + bookId + "/reserve")
            .then()
            .statusCode(200)
            .extract()
            .response()
    }

    @Then("the list of books should contain")
    @Throws(Exception::class)
    fun verifyBooks(dataTable: DataTable) {
        val expectedBooks = dataTable.asMaps(String::class.java, String::class.java)

        expectedBooks.forEach { expectedBook ->
            val title = expectedBook["title"]
            val author = expectedBook["author"]

            response.then()
                .body("find { it.title == '$title' }.author", equalTo(author))
        }
    }

    @Then("the book {string} should be reserved")
    @Throws(Exception::class)
    fun verifyBookReserved(title: String) {
        response.then()
            .body("title", equalTo(title))
            .body("reserved", equalTo(true))
    }

}
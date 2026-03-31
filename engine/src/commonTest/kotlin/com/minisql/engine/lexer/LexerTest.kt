package com.minisql.engine.lexer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerTest {

    val lexer = Lexer()

    fun List<Token>.verifyTokens(predicates: List<(Token) -> Boolean>) {
        assertEquals(predicates.size, size)

        for ((index, token) in this.withIndex()) {
            val predicate = predicates[index]
            assertTrue(predicate(token), "Token at index $index does not match predicate")
        }
    }

    @Test
    fun `Given a create table expression When tokenizing Then generate a list of tokens`() {
        // Given
        val expression = "CREATE TABLE users (id INT, balance INT);"

        // When
        val result = lexer.tokenize(expression)

        // Then
        assertEquals(12, result.size)
        result.verifyTokens(
            listOf(
                { it is CreateToken },
                { it is TableToken },
                { it is IdentifierToken && it.identifier == "users" },
                { it is LParToken },
                { it is IdentifierToken && it.identifier == "id" },
                { it is TypeToken && it.type == "INT" },
                { it is CommaToken },
                { it is IdentifierToken && it.identifier == "balance" },
                { it is TypeToken && it.type == "INT" },
                { it is RParToken },
                { it is SemiColonToken },
                { it is EOFToken }
            )
        )
    }
}
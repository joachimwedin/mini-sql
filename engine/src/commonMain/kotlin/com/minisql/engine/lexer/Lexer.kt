package com.minisql.engine.lexer

import com.minisql.engine.lexer.gen.StateName

class Lexer {
    fun tokenize(value: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val chars = ArrayDeque(value.toList())

        while (true) {
            when (val token = nextToken(chars)) {
                is ErrorToken -> throw Exception("Invalid input: $token")

                is EOFToken -> {
                    tokens.add(token)
                    return tokens
                }

                else -> tokens.add(token)
            }
        }
    }

    private fun nextToken(chars: ArrayDeque<Char>): Token {
        var currentState = StateName.START.ordinal
        val buffer = StringBuilder()

        while (true) {
            val char = chars.removeFirstOrNull()

            if (char == null) {
                if (currentState == StateName.START.ordinal) {
                    return EOFToken()
                }
                return createToken(currentState, buffer.toString(), "Unexpected end of input at: $buffer")
            }

            val nextState = LEXER_TABLE[currentState][char.code]

            if (nextState == StateName.ERROR.ordinal) {
                chars.addFirst(char)
                return createToken(currentState, buffer.toString(), "Unexpected character '$char' at: $buffer")
            }

            if (!char.isWhitespace()) {
                buffer.append(char)
            }
            currentState = nextState
        }
    }

    private fun createToken(state: Int, value: String, errorMessage: String): Token =
        TOKEN_ACTIONS[state]?.invoke(value) ?: ErrorToken(errorMessage)
}
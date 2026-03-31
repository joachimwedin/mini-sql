package com.minisql.engine.lexer.gen

import com.minisql.engine.lexer.CommaToken
import com.minisql.engine.lexer.CreateToken
import com.minisql.engine.lexer.IdentifierToken
import com.minisql.engine.lexer.LParToken
import com.minisql.engine.lexer.SemiColonToken
import com.minisql.engine.lexer.TableToken
import com.minisql.engine.lexer.Token
import com.minisql.engine.lexer.TypeToken
import kotlin.collections.iterator

fun main() {
    val debug = false

    val result = StringBuilder()
    val ind = "    "

    result.append("package com.minisql.engine.lexer\n\n")
    result.append("/*\n")
    result.append(" * Generated file — do not edit manually.\n")
    result.append(" * Run the generateLexerTable Gradle task to regenerate.\n")
    result.append(" */\n\n")

    result.append("val LEXER_TABLE = arrayOf(\n")
    for (state in states) {
        result.append("${ind}arrayOf(")

        val transitions: MutableList<Pair<String, Int>> = mutableListOf()
        for (charIndex in 0..255) {
            val char = Char(charIndex)
            var matched = false
            for ((regex, nextState) in state.transitions) {
                if (regex.matches(char.toString())) {
                    check(!matched) {
                        "Multiple transitions matched for character '$char' in state ${state.name}"
                    }
                    matched = true

                    transitions.add("$char" to nextState.ordinal)
                }
            }
            if (!matched) {
                transitions.add("$char" to StateName.ERROR.ordinal)
            }
        }

        val transitionString = transitions.joinToString(", ") {
            val comment = if (debug) {
                " /* ${it.first} --> ${StateName.entries[it.second].name} */"
            } else {
                ""
            }
            "${it.second}$comment"
        }
        result.append("${transitionString}),\n")
    }
    result.append(")\n\n")

    result.append("val TOKEN_ACTIONS = mapOf(\n")
    for (state in states) {
        state.token?.let {
            val tokenCode = state.tokenSource
            result.append("${ind}${state.name.ordinal} to $tokenCode,\n")
        }
    }
    result.append(")\n\n")

    println(result)
}

class LexerState(
    val name: StateName,
    val token: ((String?) -> Token)?,
    val tokenSource: String?,
    val transitions: Map<Regex, StateName>
)

val states = listOf(
    LexerState(
        name = StateName.ERROR,
        token = null,
        tokenSource = null,
        transitions = mapOf(
        )
    ),

    LexerState(
        name = StateName.START,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("\\s+") to StateName.START,
            Regex("C") to StateName.S1_1,
            Regex("T") to StateName.S2_1,
            Regex("[a-z]") to StateName.IDENTIFIER,
            Regex("\\(") to StateName.LPAR,
            Regex("\\)") to StateName.RPAR,
            Regex(",") to StateName.COMMA,
            Regex(";") to StateName.SEMICOLON,
            Regex("I") to StateName.S6_1,
        )
    ),


    LexerState(
        name = StateName.S1_1,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("R") to StateName.S1_2,
        )
    ),
    LexerState(
        name = StateName.S1_2,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("E") to StateName.S1_3,
        )
    ),
    LexerState(
        name = StateName.S1_3,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("A") to StateName.S1_4,
        )
    ),
    LexerState(
        name = StateName.S1_4,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("T") to StateName.S1_5,
        )
    ),
    LexerState(
        name = StateName.S1_5,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("E") to StateName.CREATE,
        )
    ),
    LexerState(
        name = StateName.CREATE,
        token = { CreateToken() },
        tokenSource = "{ CreateToken() }",
        transitions = mapOf(
        )
    ),

    LexerState(
        name = StateName.S2_1,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("A") to StateName.S2_2,
        )
    ),
    LexerState(
        name = StateName.S2_2,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("B") to StateName.S2_3,
        )
    ),
    LexerState(
        name = StateName.S2_3,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("L") to StateName.S2_4,
        )
    ),
    LexerState(
        name = StateName.S2_4,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("E") to StateName.TABLE,
        )
    ),
    LexerState(
        name = StateName.TABLE,
        token = { TableToken() },
        tokenSource = "{ TableToken() }",
        transitions = mapOf(
        )
    ),

    LexerState(
        name = StateName.IDENTIFIER,
        token = {
            IdentifierToken(requireNotNull(it) {
                "Identifier token must have a value"
            })
        },
        tokenSource = "{ value: String? -> IdentifierToken(requireNotNull(value) { \"Identifier token must have a value\" }) }",
        transitions = mapOf(
            Regex("[a-z]") to StateName.IDENTIFIER,
        )
    ),

    LexerState(
        name = StateName.LPAR,
        token = { LParToken() },
        tokenSource = "{ LParToken() }",
        transitions = mapOf(
        )
    ),
    LexerState(
        name = StateName.RPAR,
        token = { LParToken() },
        tokenSource = "{ RParToken() }",
        transitions = mapOf(
        )
    ),
    LexerState(
        name = StateName.COMMA,
        token = { CommaToken() },
        tokenSource = "{ CommaToken() }",
        transitions = mapOf(
        )
    ),
    LexerState(
        name = StateName.SEMICOLON,
        token = { SemiColonToken() },
        tokenSource = "{ SemiColonToken() }",
        transitions = mapOf(
        )
    ),

    LexerState(
        name = StateName.S6_1,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("N") to StateName.S6_2,
        )
    ),
    LexerState(
        name = StateName.S6_2,
        token = null,
        tokenSource = null,
        transitions = mapOf(
            Regex("T") to StateName.TYPE,
        )
    ),
    LexerState(
        name = StateName.TYPE,
        token = {
            TypeToken(requireNotNull(it) {
                "Type token must have a value"
            })
        },
        tokenSource = "{ value: String? -> TypeToken(requireNotNull(value) { \"Type token must have a value\" }) }",
        transitions = mapOf(
        )
    ),
)

enum class StateName {
    ERROR,
    START,

    // CREATE
    S1_1,
    S1_2,
    S1_3,
    S1_4,
    S1_5,
    CREATE,

    S2_1,
    S2_2,
    S2_3,
    S2_4,
    TABLE,

    IDENTIFIER,

    LPAR,
    RPAR,
    COMMA,
    SEMICOLON,

    S6_1,
    S6_2,
    TYPE,
}
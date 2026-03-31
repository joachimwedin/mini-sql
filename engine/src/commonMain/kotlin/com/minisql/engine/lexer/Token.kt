package com.minisql.engine.lexer

sealed interface Token

class CreateToken: Token {
    override fun toString(): String {
        return "CREATE"
    }
}

class TableToken: Token {
    override fun toString(): String {
        return "TABLE"
    }
}

class LParToken: Token {
    override fun toString(): String {
        return "LPAR"
    }
}

class RParToken: Token {
    override fun toString(): String {
        return "RPAR"
    }
}

class CommaToken: Token {
    override fun toString(): String {
        return "COMMA"
    }
}

class SemiColonToken: Token {
    override fun toString(): String {
        return "SEMICOLON"
    }
}

class TypeToken(
    val type: String
): Token {
    override fun toString(): String {
        return "TYPE(\"${type}\")"
    }
}

class SelectToken: Token {
    override fun toString(): String {
        return "SELECT"
    }
}

class AsteriskToken: Token {
    override fun toString(): String {
        return "ASTERISK"
    }
}

class FromToken: Token {
    override fun toString(): String {
        return "FROM"
    }
}

class EOFToken: Token {
    override fun toString(): String {
        return "EOF"
    }
}

class ErrorToken(
    val message: String
): Token

class IdentifierToken(
    val identifier: String
): Token {
    override fun toString(): String {
        return "IDENTIFIER(\"$identifier\")"
    }
}

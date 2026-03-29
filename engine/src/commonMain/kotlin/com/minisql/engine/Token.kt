package com.minisql.com.minisql.engine

sealed interface Token

class CreateToken: Token

class TableToken: Token

class LParToken: Token

class RParToken: Token

class CommaToken: Token

class SemiColonToken: Token

class TypeToken(
    val type: String
): Token

class SelectToken: Token

class AsteriskToken: Token

class FromToken: Token

class IdentifierToken(
    val identifier: String
): Token
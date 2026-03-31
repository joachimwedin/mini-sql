package com.minisql.com.minisql.engine

import com.minisql.engine.lexer.Lexer
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

val lexer = Lexer()

@OptIn(ExperimentalJsExport::class)
@JsExport
fun helloWorld(): String = lexer.tokenize("CREATE TABLE users (id INT, balance INT);").joinToString(separator = "\n")

package com.minisql.wasm

import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
fun helloWorld() = "${Engine.helloWorld()}"
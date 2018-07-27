package main

import java.io.BufferedReader
import java.io.InputStreamReader

fun exec(vararg cmd: String): String {
    return exec(cmd.toList())
}

fun exec(cmd: List<String>): String {
    val p = ProcessBuilder(cmd.toList()).apply {
        redirectErrorStream(true)
    }.start()
    val s = BufferedReader(InputStreamReader(p.inputStream))
    val res = s.readText()
    s.close()
    return res
}
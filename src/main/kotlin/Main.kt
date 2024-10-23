package net.riedel

import java.io.FileOutputStream
import java.io.PrintWriter
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.listDirectoryEntries

val path = Path("LevelData")
val level = 1
val levelPath = path.resolve("level_${level}")
val extension = "in"
val filePrefix = "level${level}"

fun unzip() {
    levelPath.createDirectories()
    path.listDirectoryEntries("$filePrefix*.zip").map { it.toFile() }.forEach {
        val proc = ProcessBuilder("unzip", it.absolutePath)
            .directory(levelPath.toFile())
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        proc.start().waitFor()
        it.delete()
    }
}

fun main() {
    if (true) {
        unzip()
        return
    }
    //levelPath.listDirectoryEntries("${filePrefix}_x.$extension").map { it.toFile() }.forEach {
    levelPath.listDirectoryEntries("${filePrefix}_*.$extension").map { it.toFile() }.forEach {
        PrintWriter(FileOutputStream(it.path + ".result")).use { writer ->
            it.useLines { lines ->
                println("working on ${it.name}")
                work(lines.iterator(), writer)
            }
        }
    }
}

fun Iterator<String>.takeUntilEmpty(): List<String> =
    buildList {
        while (hasNext()) {
            val line = next()
            if (line.isEmpty()) break
            add(line)
        }
    }

fun work(lines: Iterator<String>, writer: PrintWriter) {
    val count = lines.next().toInt()
    lines.next()
    writer.println(count)
    repeat(count) { itemNo ->
        writer.println()
        println("working on item $itemNo =========================================================")
        val linesList = lines.takeUntilEmpty()
        val res = workItem(linesList, writer)
        writer.println(res.toString())
    }
}

fun workItem(lines: List<String>, writer: PrintWriter): Int {
    return 0
}
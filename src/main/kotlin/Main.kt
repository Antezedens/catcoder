package net.riedel

import java.io.FileOutputStream
import java.io.PrintWriter
import java.nio.file.Path
import kotlin.io.path.*

val path = Path("LevelData")
const val extension = "in"

fun unzip(): Boolean {
    return path.listDirectoryEntries("*.zip").map { it.toFile() }.map {
        println("unzipped $it -> ${it.nameWithoutExtension}")
        val levelPath = path.resolve(it.nameWithoutExtension)
        levelPath.createDirectories()
        val proc = ProcessBuilder("unzip", it.absolutePath)
            .directory(levelPath.toFile())
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        proc.start().waitFor()
        it.delete()
    }.isNotEmpty()
}

fun zip(levelPath: Path) {
    val solution = levelPath.resolve("solution.zip")
    solution.deleteIfExists()
    val proc = ProcessBuilder(
        "zip", "-r", solution.absolutePathString(), "src", "build.gradle.kts",
        "settings.gradle.kts",
        "gradle.properties",
    )
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
    proc.start().waitFor()
}

fun main() {
    if (unzip()) {
        return
    }
    val levelPath =
        path.listDirectoryEntries("level*").filter { it.isDirectory() }.sortedWith(NOC.pathComparator).last()
    //levelPath.listDirectoryEntries("${filePrefix}_x.$extension").map { it.toFile() }.forEach {
    levelPath.listDirectoryEntries("*_*.$extension").sorted().map { it.toFile() }.forEach {
        PrintWriter(FileOutputStream(it.path + ".result")).use { writer ->
            it.useLines { lines ->
                println("working on ${it.name}")
                work(lines.iterator(), writer)
            }
        }
    }
    zip(levelPath)
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
    repeat(count) { itemNo ->
        val string = lines.next()
        println("working on item $itemNo ($string) =========================================================")
        //val linesList = lines.takeUntilEmpty()
        val result = workItem(itemNo, string, writer)
        writer.println(result)
        writer.println()

        writer.flush()
    }
}

fun workItem(itemNo: Int, lines: String, writer: PrintWriter, tryTranspose: Boolean = false): Array2d {
    return Array2d(1, 1)
}



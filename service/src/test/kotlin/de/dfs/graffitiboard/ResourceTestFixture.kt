package de.dfs.graffitiboard

class ResourceTestFixture {
    fun load(resourceFile: String): String {
        val fileText = this::class.java.classLoader.getResource(resourceFile)?.readText()
        return requireNotNull(fileText) { "fileText should not be null" }
    }
}

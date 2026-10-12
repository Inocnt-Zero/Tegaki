package eu.kanade.domain.manga.model

import tachiyomi.domain.chapter.model.Chapter

data class ScanlatorFilter(
    val scanlator: String?,
    val priority: Int,
    val excluded: Boolean,
)

/**
 * The order given to scanlators the user has not ranked themselves: most chapters first, then most
 * recent upload, so the most prolific and most current group lands nearest the top.
 *
 * Shared by chapter sync, which ranks newly discovered groups with it, and the filter dialog's
 * Reset, so that resetting reproduces exactly the order a fresh entry would get.
 */
fun Iterable<String?>.sortedByOutput(chapters: List<Chapter>): List<String?> {
    val chaptersByScanlator = chapters.groupBy { it.scanlator?.takeUnless(String::isEmpty) }
    return sortedWith(
        compareByDescending<String?> { chaptersByScanlator[it]?.size ?: 0 }
            .thenByDescending { chaptersByScanlator[it]?.maxOfOrNull(Chapter::dateUpload) ?: 0L },
    )
}

package com.myapps.imdb

data class FindResponse(
    val companyResults: CompanyResults? = CompanyResults(),
    val findPageMeta: FindPageMeta? = FindPageMeta(),
    val keywordResults: KeywordResults? = KeywordResults(),
    val nameResults: NameResults? = NameResults(),
    val resultsSectionOrder: List<String?>? = listOf(),
    val titleResults: TitleResults? = TitleResults()
) {
    data class CompanyResults(
        val results: List<Any?>? = listOf()
    )

    data class FindPageMeta(
        val includeAdult: Boolean? = false,
        val isExactMatch: Boolean? = false,
        val searchTerm: String? = ""
    )

    data class KeywordResults(
        val results: List<Any?>? = listOf()
    )

    data class NameResults(
        val hasExactMatches: Boolean? = false,
        val nextCursor: String? = "",
        val results: List<Result?>? = listOf()
    ) {
        data class Result(
            val avatarImageModel: AvatarImageModel? = AvatarImageModel(),
            val displayNameText: String? = "",
            val id: String? = "",
            val knownForJobCategory: String? = "",
            val knownForTitleText: String? = "",
            val knownForTitleYear: String? = ""
        ) {
            data class AvatarImageModel(
                val caption: String? = "",
                val maxHeight: Int? = 0,
                val maxWidth: Int? = 0,
                val url: String? = ""
            )
        }
    }

    data class TitleResults(
        val hasExactMatches: Boolean? = false,
        val nextCursor: String? = "",
        val results: List<Result> = listOf()
    ) {
        data class Result(
            val id: String? = "",
            val imageType: String? = "",
            val titleNameText: String? = "",
            val titlePosterImageModel: TitlePosterImageModel? = TitlePosterImageModel(),
            val titleReleaseText: String? = "",
            val titleTypeText: String? = "",
            val topCredits: List<String> = listOf()
        ) {
            data class TitlePosterImageModel(
                val caption: String? = "",
                val maxHeight: Int? = 0,
                val maxWidth: Int? = 0,
                val url: String? = ""
            )
        }
    }
}
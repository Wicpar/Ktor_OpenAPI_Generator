package com.papsign.ktor.openapigen.parameters.parsers.builders.query.deepobject

import com.papsign.ktor.openapigen.parameters.QueryParamStyle
import com.papsign.ktor.openapigen.parameters.parsers.builders.Builder
import com.papsign.ktor.openapigen.parameters.parsers.builders.BuilderParameters
import com.papsign.ktor.openapigen.parameters.parsers.builders.startsWithMatching
import com.papsign.ktor.openapigen.parameters.util.ListToArray
import kotlin.reflect.KType

abstract class CollectionDeepBuilder(type: KType) : DeepBuilder {

    private val contentType = ListToArray.arrayComponentKType(type)

    private val builder: Builder<QueryParamStyle> by lazy {
        DeepBuilderFactory.buildBuilderForced(contentType, explode)
    }

    abstract fun transform(lst: List<Any?>): Any?

    override fun build(key: String, parameters: BuilderParameters): Any? {
        val names = parameters.filterKeys { !it.str.equals(key, it.ignoreCase) && it.startsWithMatching(key) }
        return transform(if (names.isEmpty()) {
            listOf()
        } else {
            val indices =
                names.entries.groupBy { (k, _) -> k.str.substring(key.length + 1, k.str.indexOf("]", key.length)).toInt() }
            indices.entries.fold(
                ArrayList<Any?>((0..(indices.keys.max() ?: 0)).map { null })
            ) { acc, (idx, params) ->
                acc[idx] = builder.build("$key[$idx]", params.associate { (key, value) -> key to value })
                acc
            }
        })
    }
}

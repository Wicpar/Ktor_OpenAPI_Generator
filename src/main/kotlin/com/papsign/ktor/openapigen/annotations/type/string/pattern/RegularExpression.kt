package com.papsign.ktor.openapigen.annotations.type.string.pattern

import com.papsign.ktor.openapigen.schema.processor.SchemaProcessorAnnotation
import com.papsign.ktor.openapigen.validation.ValidatorAnnotation

@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
@SchemaProcessorAnnotation(RegularExpressionProcessor::class)
@ValidatorAnnotation(RegularExpressionProcessor::class)
annotation class RegularExpression(val pattern: String, val message: String = "")
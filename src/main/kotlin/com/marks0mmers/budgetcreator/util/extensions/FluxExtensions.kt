package com.marks0mmers.budgetcreator.util.extensions

import reactor.core.publisher.Flux
import java.util.function.Predicate

fun <T> Flux<T>.find(predicate: Predicate<T>) = this.filter(predicate).next()
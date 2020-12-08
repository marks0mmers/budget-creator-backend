package com.marks0mmers.budgetcreator.util

import reactor.core.publisher.Flux
import reactor.util.function.Tuple2
import java.util.function.Predicate

fun <T> Flux<T>.find(predicate: Predicate<T>) = this.filter(predicate).next()

operator fun <T1, T2> Tuple2<T1, T2>.component1() = this.t1
operator fun <T1, T2> Tuple2<T1, T2>.component2() = this.t2
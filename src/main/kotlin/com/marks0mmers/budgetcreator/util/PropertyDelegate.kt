package com.marks0mmers.budgetcreator.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.io.ClassPathResource
import org.springframework.core.env.Environment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A property delegate that lets you read from spring's [Environment] object without the @[Value] annotation
 *
 * Example:
 *
 * ```
 * class Foo {
 *     val prop: Long by PropertyValue("spring.data") { it?.toLong() ?: 0 }
 * }
 * ```
 *
 * @param T The target type of where the delegate is attaching to
 * @param TRet The output type of what the data you are reading is
 * @property basePath The base path of the configuration variable within the spring configuration
 * @property transformer The function that maps the string value to the [TRet]
 * @see ReadOnlyProperty
 * @author Mark Sommers
 */
class PropertyValue<T, TRet>(private val basePath: String, private val transformer: (String?) -> TRet) {
    /**
     * The operator fun that allows you to use `by` for property delegation
     *
     * @param thisRef The reference to the class that encompasses the property delegate
     * @param property The reflected property value that is being delegated
     * @return The returned type of the spring config variable
     */
    operator fun getValue(thisRef: T, property: KProperty<*>): TRet {
        val props = YamlPropertiesFactoryBean().let {
            it.setResources(ClassPathResource("/application.yml"))
            it.`object`
        }
        return props?.getProperty("$basePath.${property.name}").run(transformer)
    }
}

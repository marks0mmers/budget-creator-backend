package com.marks0mmers.budgetcreator.util

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.io.ClassPathResource
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class PropertyValue<T, TRet>(private val basePath: String, private val transformer: (String?) -> TRet) : ReadOnlyProperty<T, TRet> {
    private val props = YamlPropertiesFactoryBean().let {
        it.setResources(ClassPathResource("/application.yml"))
        it.`object`
    }

    override fun getValue(thisRef: T, property: KProperty<*>): TRet {
        return props?.getProperty("$basePath.${property.name}").run(transformer)
    }
}

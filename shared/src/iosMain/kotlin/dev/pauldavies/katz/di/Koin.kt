package dev.pauldavies.katz.di

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.parameter.parametersOf

fun startKoin(): KoinApplication {
    return initKoin()
}

fun Koin.get(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null)
}

fun Koin.get(objCClass: ObjCClass, parameters: List<Any>): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(*parameters.toTypedArray()) }
}

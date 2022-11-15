package dev.pauldavies.katz.cache

import kotlinx.coroutines.flow.flow

class CacheMissException : Exception()

internal fun <DomainType, ServiceType> cacheThenNetwork(
    getCacheCall: () -> Result<DomainType>,
    putCacheCall: (DomainType) -> Unit,
    networkCall: suspend () -> Result<ServiceType>,
    networkToDomainMapping: (ServiceType) -> DomainType
) = flow {
    val cacheResult = getCacheCall()
    if (cacheResult.isSuccess) emit(cacheResult)

    val serviceResult = networkCall()
    if (serviceResult.isSuccess && serviceResult.getOrNull() != null) {
        val serviceValue = networkToDomainMapping(serviceResult.getOrNull()!!)
        emit(Result.success(serviceValue))
        putCacheCall(serviceValue)
    } else if (serviceResult.isFailure && cacheResult.isFailure) {
        emit(Result.failure(serviceResult.exceptionOrNull() ?: Exception()))
    }
}


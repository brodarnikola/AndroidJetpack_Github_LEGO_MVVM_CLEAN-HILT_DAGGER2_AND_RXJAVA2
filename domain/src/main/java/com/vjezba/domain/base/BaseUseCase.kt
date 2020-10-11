package com.vjezba.domain.base

import com.vjezba.domain.model.Result

interface BaseUseCase<T : Any, R: Any> {
  suspend operator fun invoke(param: T): Result<R>
}
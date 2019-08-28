package com.idanatz.oneadapter.internal.validator

internal class MissingConfigArgumentException(msg: String) : Throwable(msg)
internal class MissingModuleDefinitionException(msg: String) : Throwable(msg)
internal class MultipleHolderConflictException(msg: String) : Throwable(msg)
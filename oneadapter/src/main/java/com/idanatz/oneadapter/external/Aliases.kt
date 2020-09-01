package com.idanatz.oneadapter.external

import android.graphics.Canvas
import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.holders.ViewBinder

// ItemModule
typealias OnCreated = (viewBinder: ViewBinder) -> Unit
typealias OnModelBinded<M> = (model: M, viewBinder: ViewBinder, metadata: Metadata) -> Unit
typealias OnModelUnbinded<M> = (model: M, viewBinder: ViewBinder, metadata: Metadata) -> Unit

typealias OnBinded = (viewBinder: ViewBinder, metadata: Metadata) -> Unit
typealias OnUnbinded = (viewBinder: ViewBinder, metadata: Metadata) -> Unit

// PagingModule
typealias OnMoreLoaded = (currentPage: Int) -> Unit

// ItemSelectionModule
typealias OnSelectionStarted = () -> Unit
typealias OnSelectionEnded = () -> Unit
typealias OnSelectionUpdated = (selectedCount: Int) -> Unit

// Hooks
typealias OnClicked<M> = (model: M, viewBinder: ViewBinder, metadata: Metadata) -> Unit
typealias OnSwiped = (canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) -> Unit
typealias OnSwipedCompleted<M> = (model: M, viewBinder: ViewBinder, metadata: Metadata) -> Unit

// States
typealias OnSelected<M> = (model: M, selected: Boolean) -> Unit
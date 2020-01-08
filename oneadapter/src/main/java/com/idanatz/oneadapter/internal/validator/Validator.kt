package com.idanatz.oneadapter.internal.validator

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.holders.ExternalHolderModel
import java.lang.NullPointerException
import kotlin.contracts.contract

internal class Validator {

    companion object {

        fun validateItemsAgainstRegisteredModules(itemModulesMap: MutableMap<Class<*>, ItemModule<*>>, items: List<Diffable>) {
            items.filterNot { it is ExternalHolderModel }.find { !itemModulesMap.containsKey(it.javaClass) }?.let {
                throw MissingModuleDefinitionException("did you forget to attach ItemModule? (model: ${it.javaClass})")
            }
        }

        fun validateLayoutExists(context: Context, layoutId: Int) {
            try {
                context.resources?.getResourceEntryName(layoutId) ?: throw NullPointerException()
            } catch (e: Exception) {
                throw MissingConfigArgumentException("Layout resource id not found")
            }
        }

        fun validateItemModuleAgainstRegisteredModules(itemModulesMap: MutableMap<Class<*>, ItemModule<*>>, dataClass: Class<*>) {
            if (itemModulesMap.containsKey(dataClass)) {
                throw MultipleModuleConflictException("ItemModule with model class ${dataClass.simpleName} already attached")
            }
        }

        fun validateLayoutManagerExists(recyclerView: RecyclerView): RecyclerView.LayoutManager {
            return recyclerView.layoutManager ?: throw MissingLayoutManagerException("RecyclerView's Layout Manager must be configured")
        }
    }
}
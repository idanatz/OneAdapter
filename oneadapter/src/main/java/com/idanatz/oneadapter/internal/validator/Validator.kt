package com.idanatz.oneadapter.internal.validator

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.*
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.holders.OneInternalHolderModel

import java.lang.NullPointerException

internal class Validator {

    companion object {

        fun validateItemsAgainstRegisteredModules(itemModulesMap: MutableMap<Class<*>, ItemModule<*>>, items: List<Diffable>) {
            items.filterNot { it is OneInternalHolderModel }.find { !itemModulesMap.containsKey(it.javaClass) }?.let {
                throw MissingModuleDefinitionException("Did you forget to attach an ItemModule? (model: ${it.javaClass.simpleName})")
            }
        }

        fun validateLayoutExists(context: Context, clazz: Class<*>, layoutId: Int?) {
            if (layoutId == null) {
                throw MissingConfigArgumentException("Layout resource is null - Layout resource is mandatory for the creation of an ${clazz.simpleName}")
            }
            try {
                context.resources?.getResourceEntryName(layoutId) ?: throw NullPointerException()
            } catch (e: Exception) {
                throw MissingConfigArgumentException("Layout resource id not found - Layout resource is mandatory for the creation of an ${clazz.simpleName}")
            }
        }

        fun validateItemModuleAgainstRegisteredModules(itemModulesMap: MutableMap<Class<*>, ItemModule<*>>, modelClass: Class<*>) {
            if (itemModulesMap.containsKey(modelClass)) {
                throw MultipleModuleConflictException("ItemModule with model class ${modelClass.simpleName} already attached")
            }
        }

        fun validateLayoutManagerExists(recyclerView: RecyclerView): RecyclerView.LayoutManager {
            return recyclerView.layoutManager ?: throw MissingLayoutManagerException("RecyclerView's Layout Manager must be configured")
        }

        fun validateModelClassIsDiffable(clazz: Class<*>) {
            if (!Diffable::class.java.isAssignableFrom(clazz)) {
                throw UnsupportedClassException("${clazz.simpleName} must implement Diffable interface")
            }
        }
    }
}
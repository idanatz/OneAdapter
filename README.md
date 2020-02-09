![Logo](DEV/logo.png)

# OneAdapter 
[ ![Download](https://api.bintray.com/packages/ironsource-aura/OneAdapter/oneadapter/images/download.svg) ](https://bintray.com/ironsource-aura/OneAdapter/oneadapter/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OneAdapter-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7824)
[![Android Weekly CN](https://img.shields.io/badge/Android%20Weekly%20CN-242-orange.svg)](https://androidweekly.cn/android-dev-weekly-issue-242/)

OneAdapter is made to simplify and enhance the use of the RecyclerView's Adapter while preventing common mistakes. 
With multiple modules and hooks, you don't have to think about writing an adapter anymore, and just focus on what matters. 

For better understanding what drove me to write this library and what use cases it solves best, please refer to my Medium post:
https://medium.com/@idanatsmon/adapting-your-recyclerview-the-2019-approach-e47edf2fc4f3

## Features:
- Modular approach for more reusable and testable code
- Built-in support for DiffUtil (using [Diffable](#2-implement-diffable))
- Optimized performance - internal processing done on a background thread
- 100% written in Kotlin
- [Modules:](#modules)
  - [Item Module](#basic-usage)
  - [Paging Module](#paging-module)
  - [Emptiness Module](#emptiness-module)
  - [Selection Module](#selection-module)
- [Event Hooks:](#event-hooks)
  - [Click Event Hook](#click-event-hook)
  - [Swipe Event Hook](#swipe-event-hook)
- [Others:](#others)
  - [First Bind Animation](#first-bind-animation)
  - [Data Binding](#data-binding)


# Include in your project
```groovy
dependencies {
  implementation "com.ironsource.aura.oneadapter:oneadapter:${LATEST_VERSION}"
}
```

Note that library interfaces and API may change slightly while the library design matures.<br/>
Please see the changes in the CHANGELOG file before upgrading.


# Preview
## Example
You can try out the [example project](https://github.com/idanatz/OneAdapter/tree/develop/sample) that includes basic and advanced usage both in Java and Kotlin.
## Screenshots
<img src="DEV/screenshot_1.jpg" width="210" height="420"> <img src="DEV/screenshot_2.jpg" width="210" height="420"> <img src="DEV/screenshot_3.jpg" width="210" height="420"> <img src="DEV/screenshot_4.jpg" width="210" height="420">

<br/><br/>
# Basic Usage
### 1. Implement Item Module
Item Modules are used for the creation and binding of all ViewHolders for you. In the onBind method, you will receive as a parameter the model associated with this view and a ViewBinder class that lets you find (and cache) the views defined in the associated layout file.

```kotlin
class MessageModule : ItemModule<MessageModel>() {
    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.message_model
    }

    override fun onBind(item: Item<MessageModel>, viewBinder: ViewBinder) {
        val title = viewBinder.findViewById<TextView>(R.id.title)
        title.text = item.model.title
    }

    override fun onUnbind(item: Item<MessageModel>, viewBinder: ViewBinder) {
        // unbind logic like stop animation, release webview resources, etc.
    }
}
```
### 2. Implement Diffable
The Adapter is calculating the difference between its current data and the modified data on a background thread and posting the result to the main thread. In order for this magic to work without writing tons of DiffUtil.Callback, your models need to implement one simple interface:
```kotlin
class MessageModel : Diffable {
    private val id: Int = 0
    private val title: String? = null

    override fun getUniqueIdentifier(): Long = id.toLong()
    override fun areContentTheSame(other: Any): Boolean = other is MessageModel && title == other.title
}
```
### 3. Attach To OneAdapter & Use
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachItemModule(MessageModule())
    
oneAdapter.setItems(...) 
```

<br/><br/>
# Advanced Usage
## Modules
### Multiple Types
Have more than one view type? not a problem, just create another ItemModule and attach it to OneAdapter in the same way.
#### 1. Implement Multiple Item Modules
```kotlin
class MessageModule : ItemModule<MessageModel> { ... }
class StoryModule : ItemModule<StoryModel> { ... }
```
#### 2. Attach To OneAdapter
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachItemModule(MessageModule())
    .attachItemModule(StoryModule())
    ...
```

<br/><br/>
### Paging Module
Paging Module is used for creating and binding a specific ViewHolder at the end of the list when the Adapter reaches a load more state. The visible threshold configuration is used to indicate how many items before the end of the list the onLoadMore callback should be invoked.
<br/></br>
<img src="https://media1.tenor.com/images/275a6daed242e8f0c09b5020d602a2eb/tenor.gif?itemid=15858072" width="210" height="420">
<br/>
#### 1. Implement Paging Modules
```kotlin
class PagingModuleImpl : PagingModule() {
    override fun provideModuleConfig() = object : PagingModuleConfig() {
        override fun withLayoutResource() = R.layout.load_more // can be some spinner animation
        override fun withVisibleThreshold() = 3 // invoke onLoadMore 3 items before the end
    }

    override fun onLoadMore(currentPage: Int) {
        // place your load more logic here, like asking the ViewModel to load the next page of data.
    }
}
```
#### 2. Attach To OneAdapter
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachPagingModule(PagingModuleImpl())
    ...
```

<br/><br/>
### Emptiness Module
Emptiness Module is used for creating and binding a specific ViewHolder when the Adapter has no data to render.
<br/></br>
<img src="https://media1.tenor.com/images/c653010777465c1c5d115210b7e349e8/tenor.gif?itemid=15858077" width="210" height="420">
<br/>
#### 1. Implement Emptiness Modules
```kotlin
class EmptinessModuleImpl : EmptinessModule() {
    override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
        override fun withLayoutResource() = R.layout.empty_state
    }

    override fun onBind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) { ... }

    override fun onUnbind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) { ... }
}
```
#### 2. Attach To OneAdapter
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachEmptinessModule(EmptinessModuleImpl())
    ...
```

<br/><br/>
### Selection Module
Selection Module is used for enabling single or multiple selection on Items.
<br/></br>
<img src="https://media.tenor.com/images/2d6fdae647c6c774ceadeb79d7640e42/tenor.gif" width="210" height="420">
<br/>
#### 1. Implement Selection Modules
```kotlin
class ItemSelectionModuleImpl : ItemSelectionModule() {
    override fun provideModuleConfig(): ItemSelectionModuleConfig = object : ItemSelectionModuleConfig() {
        override fun withSelectionType() = SelectionType.Multiple // Or SelectionType.Single
    }

    override fun onSelectionUpdated(selectedCount: Int) {
        // place your general selection logic here, like changing the toolbar text to indicate the selected count.
    }
}
```
#### 2. Implement Selection State
```kotlin
class SelectionStateImpl : SelectionState<MessageModel>() {
    override fun isSelectionEnabled(model: MessageModel) = true

    override fun onSelected(model: MessageModel, selected: Boolean) {
        // insert your selected logic here. 
        // right after this call you will receive an onBind call in order to reflect your changes on the relevant Item Module.
    }
}
```
#### 3. Attach To ItemModule & OneAdapter
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachItemModule(MessageModule()).addState(SelectionStateImpl())
    .attachItemSelectionModule(ItemSelectionModuleImpl())
    ...
```

<br/><br/><br/>
## Event Hooks
Item Modules can easily be enhanced with event hooks to get access to common events like clicking or swiping on an item.

### Click Event Hook
Click Hook can be attached in order to recieve click events on an item.
<br/></br>
<img src="https://media1.tenor.com/images/6c6b218f27b250d1c72d98fc64ba895d/tenor.gif?itemid=15858123" width="210" height="420">
<br/>
#### 1. Implement Click Event Hook
```kotlin
class MessageClickEvent : ClickEventHook<MessageModel>() {
    override fun onClick(model: MessageModel, viewBinder: ViewBinder) {
        // place your on click logic here.
    }
}
```
#### 2. Attach To ItemModule
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachItemModule(MessageModule()).addEventHook(MessageClickEvent())
    ...
```

<br/><br/>
### Swipe Event Hook
Swipe Hook can be attached in order to receive swiping (during and when completed) events on an item.
<br/></br>
<img src="https://media1.tenor.com/images/6628708db23f76d528f451b1d1d8fd4c/tenor.gif?itemid=15858132" width="210" height="420">
<br/>
#### 1. Implement Swipe Event Hook
```kotlin
class MessageSwipeEvent : SwipeEventHook<MessageModel>() {
    override fun provideHookConfig(): SwipeEventHookConfig = object : SwipeEventHookConfig() {
        override fun withSwipeDirection() = listOf(SwipeDirection.Left, SwipeDirection.Right)
    }

    override fun onSwipe(canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) {
        // draw your swipe UI here.
        // like painting the canvas red with a delete icon.
    }

    override fun onSwipeComplete(model: MessageModel, direction: SwipeDirection, viewBinder: ViewBinder) {
        // place your swipe logic here.
        // like removing an item after it was swiped right.
    }
}
```
#### 2. Attach To ItemModule
```kotlin
val oneAdapter = OneAdapter(recyclerView)
    .attachItemModule(MessageModule()).addEventHook(MessageSwipeEvent())
    ...
```

<br/><br/><br/>
## Others

### First Bind Animation
The provided Animator will be animated on the first bind of the corresponding ItemModule's models. 
<br/></br>
<img src="https://media.tenor.com/images/9e9a9a6ad634157460e779fb59fd59eb/tenor.gif" width="210" height="420">
<br/>
```kotlin
class MessageModule : ItemModule<MessageModel>() {
    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.message_model
        
        override fun withFirstBindAnimation(): Animator {
            // or can be implemented by constructing ObjectAnimator
            return ObjectAnimator().apply {
                 propertyName = "translationX"
                 setFloatValues(-1080f, 0f)
                 duration = 750
            }
        }
    }

    override fun onBind(item: Item<MessageModel>, viewBinder: ViewBinder) { ... }
}
```

### Data Binding
Built in support for Android Data Binding (https://developer.android.com/topic/libraries/data-binding)
Full example is provided in the example project.
```kotlin
class MessageModule : ItemModule<ObservableMessageModel>() {
    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.message_model_data_binding
    }

    override fun onBind(item: Item<ObservableMessageModel>, viewBinder: ViewBinder) {
        viewBinder.dataBinding?.run {
            setVariable(BR.messageModel, item.model)
            lifecycleOwner = this@DataBindingActivity
            executePendingBindings()
        }
    }
}
```

<br/><br/>
# License
Copyright (c) 2019 Idan Atsmon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

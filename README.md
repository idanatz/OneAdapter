![Logo](DEV/logo.png)

# OneAdapter [ ![Download](https://api.bintray.com/packages/ironsource-aura/OneAdapter/oneadapter/images/download.svg) ](https://bintray.com/ironsource-aura/OneAdapter/oneadapter/_latestVersion)
OneAdapter is made to simplify and enhance the use of the RecyclerView's Adapter while preventing common mistakes. 
With multiple modules and hooks, you don't have to think about writing an adapter anymore, and just focus on what matters. 

For better understanding what drove me to write this library and what use cases it solves best, please refer to my Medium post:
https://medium.com/@idanatsmon/adapting-your-recyclerview-the-2019-approach-e47edf2fc4f3

## Features:
- Modular approach for more reusable and testable code
- Built-in support for DiffUtil (using [Diffable](#2-implement-diffable))
- Optimized performance - internal processing done on a background thread
- 100% written in Kotlin
- Modules:
  - [Item Module](#basic-usage)
  - [Paging Module](#paging-module)
  - [Emptiness Module](#emptiness-module)
  - [Selection Module](#selection-module)
- Event Hooks:
  - [Click Event Hook](#click-event-hook)

# Include in your project
```groovy
dependencies {
  implementation "com.ironsource.aura.oneadapter:oneadapter:${LATEST_VERSION}"
}
```

<br/><br/>
# Preview
## Example
You can try out the [example project](https://github.com/idanatz/OneAdapter/tree/develop/sample) that includes basic and advanced usage both in Java and Kotlin.
## Screenshots
<img src="DEV/screenshot_1.jpg" width="210" height="420"> <img src="DEV/screenshot_2.jpg" width="210" height="420"> <img src="DEV/screenshot_3.jpg" width="210" height="420"> <img src="DEV/screenshot_4.jpg" width="210" height="420">

<br/><br/>
# Usage
## Basic Usage
### 1. Implement Item Module
Item Modules are used for the creation and binding of all ViewHolders for you. In the onBind method, you will receive as a parameter the model associated with this view and a ViewBinder class that lets you find (and cache) the views defined in the associated layout file.
##### Java
```java
class MessageModule extends ItemModule<MessageModel> {
      @NotNull @Override
      public ItemModuleConfig provideModuleConfig() {
          return new ItemModuleConfig() {
              @Override
              public int withLayoutResource() { return R.layout.message_model; }
          };
      }

      @Override
      public void onBind(@NotNull MessageModel model, @NotNull ViewBinder viewBinder) {
          TextView title = viewBinder.findViewById(R.id.title);
          title.setText(model.title);
      }
    
     @Override
      public void onUnbind(@NotNull ViewBinder viewBinder) {
          // unbind logic like stop animation, release webview resources, etc.
      }
}
```
##### Kotlin
```kotlin
class MessageModule : ItemModule<MessageModel>() {
    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.message_model
    }

    override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
        val title = viewBinder.findViewById<TextView>(R.id.title)
        title.text = model.title
    }

    override fun onUnbind(viewBinder: ViewBinder) {
        // unbind logic like stop animation, release webview resources, etc.
    }
}
```
### 2. Implement Diffable
The Adapter is calculating the difference between its current data and the modified data on a background thread and posting the result to the main thread. In order for this magic to work without writing tons of DiffUtil.Callback, your models need to implement one simple interface:
##### Java
```java
public class MessageModel implements Diffable {
    private int id;
    private String title;

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object other) {
        return other instanceof MessageModel && title.equals(((MessageModel) other).title);
    }
}
```
##### Kotlin
```kotlin
class MessageModel : Diffable {
    private val id: Int = 0
    private val title: String? = null

    override fun getUniqueIdentifier(): Long = id.toLong()
    override fun areContentTheSame(other: Any): Boolean = other is MessageModel && title == other.title
}
```
### 3. Attach To OneAdapter & Use
##### Java
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachItemModule(new MessageModule())
    .attachTo(recyclerView);
    
oneAdapter.setItems(...)  
```
##### Kotlin
```kotlin
val oneAdapter = OneAdapter()
    .attachItemModule(MessageModule())
    .attachTo(recyclerView);
    
oneAdapter.setItems(...) 
```

<br/><br/>
## Advanced Usage
### Multiple Types
Have more than one view type? not a problem, just create another ItemModule and attach it to OneAdapter in the same way.
#### 1. Implement Multiple Item Modules
```java
class MessageModule extends ItemModule<MessageModel> { ... }
class StoryModule extends ItemModule<StoryModel> { ... }
```
#### 2. Attach To OneAdapter
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachItemModule(new MessageModule())
    .attachItemModule(new StoryModule())
    ...
```

<br/><br/>
### Paging Module
Paging Module is used for creating and binding a specific ViewHolder at the end of the list when the Adapter reaches a load more state. The visible threshold configuration is used to indicate how many items before the end of the list the onLoadMore callback should be invoked.
#### 1. Implement Paging Modules
##### Java
```java
class PagingModuleImpl extends PagingModule {
    @NotNull @Override
    public PagingModuleConfig provideModuleConfig() {
        return new PagingModuleConfig() {
            @Override
            public int withLayoutResource() { return R.layout.load_more; } // can be some spinner animation.

            @Override
            public int withVisibleThreshold() { return 3; } // invoke onLoadMore 3 items before the end.
        };
    }

    @Override
    public void onLoadMore(int currentPage) {
        // place your load more logic here, like asking the ViewModel to load the next page of data.
    }
}
```
##### Kotlin
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
##### Java
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachPagingModule(new PagingModuleImpl())
    ...
```
##### Kotlin
```kotlin
val oneAdapter = OneAdapter()
    .attachPagingModule(PagingModuleImpl())
    ...
```

<br/><br/>
### Emptiness Module
Emptiness Module is used for creating and binding a specific ViewHolder when the Adapter has no data to render.
#### 1. Implement Emptiness Modules
##### Java
```java
class EmptinessModuleImpl extends EmptinessModule {
    @NotNull @Override
    public EmptinessModuleConfig provideModuleConfig() {
        return new EmptinessModuleConfig() {
            @Override
            public int withLayoutResource() { return R.layout.empty_state; }
        };
    }

    @Override
    public void onBind(@NotNull ViewBinder viewBinder) { ... }

    @Override
    public void onUnbind(@NotNull ViewBinder viewBinder) { ... }
}
```
##### Kotlin
```kotlin
class EmptinessModuleImpl : EmptinessModule() {
    override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
        override fun withLayoutResource() = R.layout.empty_state
    }

    override fun onBind(viewBinder: ViewBinder) { ... }

    override fun onUnbind(viewBinder: ViewBinder) { ... }
}
```
#### 2. Attach To OneAdapter
##### Java
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachEmptinessModule(new EmptinessModuleImpl())
    ...
```
##### Kotlin
```kotlin
val oneAdapter = OneAdapter()
    .attachEmptinessModule(EmptinessModuleImpl())
    ...
```

<br/><br/>
### Selection Module
Selection Module is used for enabling single or multiple selection on Items.
#### 1. Implement Selection Modules
##### Java
```java
class ItemSelectionModuleImpl extends ItemSelectionModule {
    @NotNull @Override
    public ItemSelectionModuleConfig provideModuleConfig() {
        return new ItemSelectionModuleConfig() {
            @NotNull @Override
            public SelectionType withSelectionType() { return SelectionType.Multiple; } // Or SelectionType.Single.
        };
    }

    @Override
    public void onSelectionUpdated(int selectedCount) {
       // place your general selection logic here, like changing the toolbar text to indicate the selected count.
    }
}
```
##### Kotlin
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
##### Java
```java
class SelectionStateImpl extends SelectionState<MessageModel> {
    @Override
    public boolean selectionEnabled(@NonNull MessageModel model) {
        return true;
    }

    @Override
    public void onSelected(@NonNull MessageModel model, boolean selected) {
        // update your model here. 
        // right after this call you will receive an onBind call in order to reflect your changes on the relevant Item Module.
        model.isSelected = selected;
    }
}
```
##### Kotlin
```kotlin
class SelectionStateImpl : SelectionState<MessageModel>() {
    override fun selectionEnabled(model: MessageModel) = true

    override fun onSelected(model: MessageModel, selected: Boolean) {
        // update your model here. 
        // right after this call you will receive an onBind call in order to reflect your changes on the relevant Item Module.
        model.isSelected = selected;
    }
}
```
#### 3. Attach To ItemModule & OneAdapter
##### Java
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachItemModule(new MessageModule()).addState(new SelectionStateImpl())
    .attachItemSelectionModule(new ItemSelectionModuleImpl())
    ...
```
##### Kotlin
```kotlin
val oneAdapter = OneAdapter()
    .attachItemModule(MessageModule()).addState(SelectionStateImpl())
    .attachItemSelectionModule(ItemSelectionModuleImpl())
    ...
```

<br/><br/>
### Click Event Hook
Item Modules can easily be enhanced with event hooks, for instance, ClickEventHook which let you bind a click listener for the entire view.
#### 1. Implement Click Event Hook
##### Java
```java
class MessageClickEvent extends ClickEventHook<MessageModel> {
    @Override
    public void onClick(@NonNull MessageModel model, @NonNull ViewBinder viewBinder) { 
        // place your on click logic here.
    }
}
```
##### Kotlin
```kotlin
class MessageClickEvent : ClickEventHook<MessageModel>() {
    override fun onClick(model: MessageModel, viewBinder: ViewBinder) {
        // place your on click logic here.
    }
}
```
#### 2. Attach To ItemModule
##### Java
```java
OneAdapter oneAdapter = new OneAdapter()
    .attachItemModule(new MessageModule()).addEventHook(new MessageClickEvent())
    ...
```
##### Kotlin
```kotlin
val oneAdapter = OneAdapter()
    .attachItemModule(MessageModule()).addEventHook(MessageClickEvent())
    ...
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

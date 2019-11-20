Changelog
==========

Version 1.4.0
----------------------------
* Added support for ButterKnife and DataBinding
* Added more utility functions like item visibility
* Added examples for nested RecyclerView, ButterKnife and DataBinding in the sample project
* Fixed Bug: Sync issues with background and ui threads when using nested RecyclerViews with OneAdapter
* Breaking Change: ViewBinder getRootView function changed to a property
* Breaking Change: added HookConfig for SwipeEventHook to specify the supported swipe directions


Version 1.3.0
----------------------------
* Added First Bind Animation Config
* Improved threading mechanism
* Added common UI tests
* Breaking Change: RecyclerView is now a constructor parameter instead of an attachment function
* Breaking Change: OnUnbind functions now includes the model as a parameter


Version 1.2.0
----------------------------
* Change: Diffable is now mandatory when working with OneAdapter
* Bug Fixes:
    * Paging Module not working properly with some layout manager
    * onUnbind call now called when the view is recycled instead of detached from window


Version 1.1.1
----------------------------
* Removed the res folder to prevent conflicts


Version 1.1.0
----------------------------
* Event Hooks:
	* Swipe Event Hook
* Bug fixes related to the diffing mechanism
* Removed unnecessary lines from manifest


Version 1.0.0
----------------------------
First Release Includes:
* Modules:
	* Item Module
	* Emptiness Module
	* Paging Module
	* Selection Module
* Event Hooks:
	* Click Event Hook
* Diffable Interface

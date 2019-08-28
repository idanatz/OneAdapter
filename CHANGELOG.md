Changelog
==========


Version 1.3.0
----------------------------
* RecyclerView is now a constructor parameter instead of an attachment function
* OnUnbind functions now includes the model as a parameter
* Added common UI tests


Version 1.2.0
----------------------------
* Diffable is now mandatory when working with OneAdapter
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

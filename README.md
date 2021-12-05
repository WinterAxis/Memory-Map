<h1 align="center">Memory Map</h1>

<p align="center">An app that allows you to upload maps, add pins of different colors, shapes, and descriptions to the map, and save the map. Once maps have been saved, you can 
view all of the maps, change the name of them, and even delete them.</p>

## Links

- [Repo](https://github.com/WinterAxis/Memory-Map "Memory Map Repo")

## Screenshots

![Home Page](https://user-images.githubusercontent.com/71357023/144724946-bfe88c04-242d-4f49-a55c-10f5b1958da9.png "Home Page")

![Edit Map Page](https://user-images.githubusercontent.com/71357023/144725122-6d3f0771-3d14-4e2d-8a26-bb8a1149daee.png "Edit Map Page")

![View Saved Maps Page](https://user-images.githubusercontent.com/71357023/144725193-c92c6b90-6090-47e0-b3cd-c4bf35dc2713.png "View Saved Maps Page")

## Built With

- Java
- XML
- Android Studio

## Java Files

MainActivity Methods: 
    
    onCreate:
        
        -Sets the Next_Map_Id in the “maps_pref” shared preference file to a starting value of 1 if it does not already exist.
        
        -Sets up the NavController with the navHostFragment.
        
        -Sets up the ActionBar with the NavController
        
    onSupportNavigateUp:
    
        -Allows the navigate up functionality
        
HomeFragment Methods:  

    onCreateView:
    
        -Inflaters the fragment_home layout

        -Sets the new_map_button onClick listener to call the new map method

        -Sets the list_map_button onClick listener to navigate to the MapListFragment

    newMap:
    
        -Sets the map_id String based on the Next_Map_Id from the maps_pref file

        -Increments Next_Map_Id by 1 

        -Adds map_id to the set stored in the maps_pref file

        -Navigates to the MapEditorFragment with the map_id as an argument saved in a bundle

MapEditorFragment Methods:

    onCreate:
        -Retrieves map_id from the Bundle passed through the arguments.

        -Gets the shared preferences file for the Map_Id

    onCreateView:
    
        -Inflates the fragment_map_editor layout

        -Sets the DragListen on the map_image_view
        
        -Sets up the Next_Pin_Id and Pins set for the preferences file if they do not exist
    
        -Gets the map image URI if one is saved and calls setImg.

        -Calls loadPins.

    onCreateOptionsMenu:
    
        -Sets up the options menu and binds some on listeners.

    onOptionsItemSelected:
    
        -Method for when a menu item is click.

        -action_set_map calles mGetImageContent.

        -action_set_pin_icon calls showIconPopup.

        -action_set_pin_color calls showColorPopup.

    onClickPinPopup
    
        -Uses the tag attach the pins image view to get the title and description of the pin from the maps shared preference file.

        -Creates a popup window using the pin_popup_window layout with the title and description of the pin.

        -Sets text change listeners to update the shared preference file.

    showIconPopup:
    
        -Creates a popup window from icon_popup_window layout with onClick listeners that change the menus draggable pin icon based on what icon is clicked

    showColorPopup:
    
        -Creates a popup window from color_popup_windowlayout with onClick listeners that change the menus draggable pin color based on what color is clicked

    setImg:
    
        -Sets the map image based on the URI passed 

    addPin:
    
        -Adds a pin to the stored set and its other pieces of information to the shared preference file

    loadPin:
    
        -Gets the set of pin from pref file.

        -Removes all previously loaded pins from the parentLayout.

        -For each pin in the set, it creates a new imageview and adds it to the parentLayout using the pins stored information.

MapListFragment Methods:

## Layout Files

-activity_main: contains the design code associated with the home page. Has two buttons that when clicked will navigate to other pages. 

-color_popup_window: pops up when the color selection tool is clicked on the map edit page. Contains twelve ImageViews.

-fragment_map_editor: contains the design code associated with the edit page. The page is an image view for a map to be inserted. 

-icon_popup_window: pops up when the icon selection tool is clicked on the map edit page. Contains twelve ImageViews. 

-list_item_map: the design layout for the elements that are displayed in the RecycleView. Contains a TextView and two ImageButtons. 

-name_popup_window: pops up when the change name button is pressed on the list page. Contains an EditText. 

-pin_popup_window: pops up once a pin has been placed and then clicked. Contains two EditTexts. 

-sample_action_view: an ImageView template for the action bar. 

## Menu Files

-appbar_menu: the app bar that appears on the edit map page. Contains three buttons that have the functions of changing the shape of the pin, changing the color of the pin, and a pin that can be dragged onto the map. 

## Navigation Files

-nav_graph: is the navigation functionality between the pages. 

## Future Updates

- [ ] New ways to implement map images.
- [ ] Delete and edit tool to pins once they have been added to a map.

## Authors

**Kirsten Alden**

- [Profile](https://github.com/LilacSkys "Kirsten Alden")
- [Email](mailto:knalde3950@ung.edu?subject=Hi "Hi!")

**Nate Alden**

- [Profile](https://github.com/WinterAxis "Nate Alden")
- [Email](mailto:naalde3055@ung.edu?subject=Hi "Hi!")

**Cathryn Allen**

- [Profile](https://github.com/CathrynAllen24 "Cathryn Allen")
- [Email](mailto:cyalle7394@ung.edu?subject=Hi "Hi!")

**Zach Barge**

- [Profile](https://github.com/deCryptid "Zach Barge")
- [Email](mailto:zmbarg3303@ung.edu?subject=Hi "Hi!")

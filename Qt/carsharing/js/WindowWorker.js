//Minden ablakváltás itt van megvalósítva
//Parameterkent mehet atadando ertek amennyiben kell

function openNewWindow(openWindowLink,rootWindowID, parameterErteke)
{
    var component = Qt.createComponent(openWindowLink);//pl.:"qrc:/Window_Main.qml"
    var window    = component.createObject(rootWindowID,{parameter: parameterErteke});//pl.: (id:) mainWindow - mire nyiljon meg
    window.show()
}

//Fo oldal
    function openMainWindow(userJSON)
    {
        openNewWindow("qrc:/Window_Main.qml", loginWindow, userJSON);
        loginWindow.opacity = 0;
    }
//Fo oldal bezarasa
    function closeMainWindow()
    {
        mainWindow.close()
        loginWindow.opacity = 1;
    }
//CAR - Minden kocsi megtekintése
    function openCarAllWindow(root)
    {
        openNewWindow("qrc:/car/Window_Car_All.qml", mainWindow);
    }
    //Kocsik - Minden kocsi megtekintése
    function openCarAddWindow(root)
    {
        openNewWindow("qrc:/car/Window_Car_Add.qml", mainWindow);
    }
    //Kocsik - Minden kocsi megtekintése
    function openCarRemoveWindow(root)
    {
        openNewWindow("qrc:/car/Window_Car_Remove.qml", mainWindow);
    }
//CAR

//USER - Regisztracios oldal megnyitasa
    function openRegistration(id)
    {
        openNewWindow("qrc:/user/Window_User_Add.qml", id);
    }

    function openUserUpdate(id,userObj)
    {
        openNewWindow("qrc:/user/Window_User_Update.qml", id, userObj);
    }
//USER

//CATEGORY
    function openCategoryAllWindow()
    {
        openNewWindow("qrc:/category/Window_Category_All.qml", mainWindow);
    }
    function openCategoryAddWindow()
    {
        openNewWindow("qrc:/category/Window_Category_Add.qml", mainWindow);
    }
    function openCategoryRemoveWindow(root)
    {
        openNewWindow("qrc:/category/Window_Category_Remove.qml", mainWindow);
    }
//CATEGORY

//SITES
    function openSitesAllWindow()
    {
        openNewWindow("qrc:/site/Window_Site_All.qml", mainWindow);
    }
    function openSitesAddWindow()
    {
        openNewWindow("qrc:/site/Window_Site_Add.qml", mainWindow);
    }
    function openSitesRemoveWindow()
    {
        openNewWindow("qrc:/site/Window_Site_Remove.qml", mainWindow);
    }
//SITES

//RENTAL
    function openRentalAllWindow(userData)
    {
        openNewWindow("qrc:/rental/Window_Rental_All.qml", mainWindow,userData);
    }
    function openRentalAddWindow(userData)
    {
        openNewWindow("qrc:/rental/Window_Rental_Add.qml", mainWindow,userData);
    }
    function openRentalRemoveWindow(userData)
    {
        openNewWindow("qrc:/rental/Window_Rental_Remove.qml", mainWindow,userData);
    }
//RENTAL

//ABOUT
    function openAboutWindow()
    {
        openNewWindow("qrc:/Window_About.qml", mainWindow);
    }
//ABOUT

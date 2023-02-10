import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "js/WindowWorker.js" as WindowWorker
import "js/APiWorker.js" as API

//A program foablaka miutan a felhasznalo bejelentkezett
ApplicationWindow {
    id: mainWindow
    title: qsTr("Banger - Car Sharing")
    width: 800
    height: 480
    visible: true
    DialogBox{id: dialogBox}
    property var parameter;
    property var ertekek: [];

    property var kocsiAdatok: [];
    property var kocsiAdatok_Datumok: [];

    onClosing:
    {
        WindowWorker.closeMainWindow();
        API.get("/api/users/logout");
    }

    function loadData()
    {
        API.get("/api/categories/", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
                //dialogBox.prompt("Hiba!", "Nem érvényes adatok!");
            }
            else
            {
                console.log("Visszatert___"+visszateresErtek);
                ertekek = API.jsonParserForCategories(visszateresErtek);
                var osszes = ertekek.length;
                for(var i = 0;i<osszes;i++)
                {
                    pieSeries.append(ertekek[i].name+" - "+((1/osszes)*100)+"%", (1/osszes));
                }


            }

        });

    }
    onActiveChanged: {
        console.log("FOABLAK VALTOZOTT");
        pieSeries.clear();
        loadData();
    }

    menuBar: MenuBar {
        Menu {
            id: egyesMenu
            title: qsTr("&Car Sharing")
            MenuItem {
                text: qsTr("&Profil")
                onTriggered: {WindowWorker.openUserUpdate(mainWindow, parameter);}
            }
            MenuItem {
                text: qsTr("&About")
                onTriggered: {WindowWorker.openAboutWindow();}
            }
            MenuItem {
                text: qsTr("K&ilépés")
                onTriggered: {WindowWorker.closeMainWindow();}
            }
        }
        Menu {
            id: kettesMenu
            title: qsTr("&Kocsik")

            MenuItem {
                text: qsTr("&Megtekintés")
                //enabled: (mainWindow.parameter.role === "admin") ? true : false;
                onTriggered: {WindowWorker.openCarAllWindow(mainWindow);}
            }

            MenuItem {
                text: qsTr("&Hozzáadás")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openCarAddWindow(mainWindow);}
            }
            MenuItem {
                text: qsTr("Törlés")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openCarRemoveWindow(mainWindow);}
            }
        }
        Menu {
            id: otosMenu
            title: qsTr("&Kategóriák")

            MenuItem {
                text: qsTr("&Megtekintés")
                onTriggered: {WindowWorker.openCategoryAllWindow();}
            }

            MenuItem {
                text: qsTr("&Hozzáadás")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openCategoryAddWindow();}
            }
            MenuItem {
                text: qsTr("Törlés")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openCategoryRemoveWindow(mainWindow);}
            }

        }
        Menu {
            id: harmasMenu
            title: qsTr("&Telephelyek")
            MenuItem {
                text: qsTr("&Megtekintés")
                onTriggered: {WindowWorker.openSitesAllWindow();}
            }

            MenuItem {
                text: qsTr("&Hozzáadás")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openSitesAddWindow();}
            }
            MenuItem {
                text: qsTr("Törlés")
                enabled: (mainWindow.parameter.admin === true) ? true : false;
                onTriggered: {WindowWorker.openSitesRemoveWindow();}
            }
        }
        Menu {
            id: negyesMenu
            title: qsTr("&Bérlés")
            MenuItem {
                text: qsTr("&Korábbi bérlések megtekintés")
                onTriggered: {WindowWorker.openRentalAllWindow(parameter);}
            }

            MenuItem {
                text: qsTr("&Új bérlés")
                onTriggered: {WindowWorker.openRentalAddWindow(parameter);}
            }
            MenuItem {
                text: qsTr("Bérlés befejezése")
                onTriggered: {WindowWorker.openRentalRemoveWindow(parameter);}
            }
        }
    }



    Rectangle
    {
        color:  "transparent"
        width: 800
        height: 480
        anchors.centerIn: parent
        ColumnLayout
        {
            Rectangle
            {
                color:  "transparent"
                width: 800
                height: 100
                Text {
                    font.pointSize: 24
                    anchors.centerIn: parent
                    text: qsTr("Üdv "+mainWindow.parameter.name)
                }
            }
            RowLayout
            {
                Layout.alignment: Qt.AlignHCenter
                width: 800
                Rectangle
                {
                    color:  "transparent"
                    width: 400
                    Layout.alignment: Qt.AlignHCenter
                    height: 241
                    //TODO: eltuntetni ha nincs elerheto adat!
                    ChartView {
                        Layout.alignment: Qt.AlignHCenter
                        visible: (ertekek.length !== 0) ? true : false
                        width: 400
                        height: 240
                        theme: ChartView.ChartThemeBlueCerulean
                        antialiasing: true
                        PieSeries {
                            id: pieSeries
                        }
                    }
                }

            }

        }
    }


}

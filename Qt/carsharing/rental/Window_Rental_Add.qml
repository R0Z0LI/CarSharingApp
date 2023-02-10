import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API

ApplicationWindow {
    id: rentalAddWindow
    title: qsTr("Banger - Car Sharing - Új bérlés")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog

    property var parameter;
    property var selectedCarID;
    //Elerheto nem foglalt kocsik kiirasa
    Component.onCompleted: {
        API.get("/api/cars/available", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                //console.log("Hiba volt betoltes kozben");
            }
            else
            {
                API.jsonParserForCars(visszateresErtek, listview);
            }

        });
    }

    ListModel
    {
        id: listModel
    }

    Rectangle
    {

        color:  "transparent"
        width: 480
        height: 380
        anchors.centerIn: parent

        ColumnLayout
        {
            anchors.fill: parent
            RowLayout
            {

                Text
                {
                   Layout.alignment: Qt.AlignLeft

                   font.family: "Helvetica"
                   font.pointSize: 24
                   color: "purple"
                   text: qsTr("Új bérlés")
                }
            }
            Rectangle
            {
                color:  "transparent"
                width: 480
                height: 300
                ListView
                {
                    id: listview
                    width: 480
                    model: listModel
                    anchors.fill: parent
                    focus: true
                    delegate: Rectangle
                    {
                        color:  "whitesmoke"
                        width: 480
                        height: 40
                        RowLayout {
                        anchors.fill: parent
                        spacing: 2
                        width: 480; height: 40
                        Text
                        {
                            text: carData.licensePlate
                            Layout.preferredWidth: 50
                            Layout.alignment: Qt.AlignHCenter
                        }
                        Button
                        {
                            Layout.alignment: Qt.AlignHCenter
                            Layout.preferredWidth: 100
                            text: "További adatok"
                            onClicked: showPopUp(carData)
                        }
                        Button
                        {
                            Layout.alignment: Qt.AlignRight
                            Layout.preferredWidth: 100
                            text: "Bérlés"
                            onClicked: {registerRental(carData);}
                        }
                    }
                  }
                }
              }
            }
        }


    Text {
        visible: (listview.model.count === 0) ? true : false
        anchors.centerIn: parent
        text: "Nincs elérhető kocsi!"
    }

    function showPopUp(carData)
    {
        popupManufacturerText.text = "Gyártó: "+carData.manufacturer
        popupModelText.text = "Model: "+carData.model
        popupyearProducedText.text = "Gyártás éve: "+carData.yearProduced

        popupCategoryNameText.text = "Kategória: "+carData.category.name
        popupCategoryPriceText.text = "Percdíj: "+carData.category.pricePerHour+" Ft/min"

        if(carData.renter !== null)
            popupRenterText.text = "Jelenlegi bérlő: "+carData.renter.name
        else
            popupRenterText.text = "Jelenlegi bérlő: Nincs"
        popupSpaceText.text = "Utasok száma max.: "+carData.space

        popup.open()
    }
    Popup {
            id: popup
            x: parent.width/2-100
            y: parent.height/2-100
            width: 200
            height: 200
            modal: true
            focus: true
            closePolicy: Popup.CloseOnEscape | Popup.CloseOnPressOutsideParent
            contentItem: Column{
                Row{
                    Text
                    {   id: popupManufacturerText
                        text: ""
                    }
                    Text
                    {   id: popupModelText
                        text: ""
                    }
                }
                Text
                {   id: popupyearProducedText
                    text: ""
                }
                Text
                {   id: popupCategoryNameText
                    text: ""
                }
                Text
                {   id: popupCategoryPriceText
                    text: ""
                }
                Text
                {   id: popupRenterText
                    text: ""
                }
                Text
                {   id: popupSpaceText
                    text: ""
                }

            }
        }


    function registerRental(selectedCar)
    {
        rentalAddWindow.selectedCarID = selectedCar.licensePlate;
        //console.log(rentalAddWindow.parameter.id+" szemely szeretne elvinni ezt a kocsit: "+rentalAddWindow.selectedCarID);
        if(typeof rentalAddWindow.selectedCarID !== "undefined")
        {
            //console.log("minden meg van adva!");
            API.post("/api/rentals/add?carId="+rentalAddWindow.selectedCarID+"&userId="+rentalAddWindow.parameter.id);
            torlesListabol(rentalAddWindow.selectedCarID);
        }
        else
        {
            dialogBox.prompt("Hiányos adatok!", "Kérlek adj meg minden információt!");
        }
    }
    //Miutan felvettek a kosit torlodjon, hogy tobbszor ne lehessen
    function torlesListabol(id)
    {
        for (var i = 0; i < listview.model.count; i++) {
               if (listview.model.get(i).carData.licensePlate === id) {
                   var spliced = listview.model.remove(i);
               }
           }
    }


}

import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API

ApplicationWindow {
    id: rentalRemoveWindow
    title: qsTr("Banger - Car Sharing - Bérlés befejezése")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog

    property var parameter;
    property var selectedCarID;

    property var telephelyNevek: [];
    property var telephelyAdatok: [];


    Component.onCompleted:
    {
        API.get("/api/rentals/active?userId="+parameter.id, function (visszateresErtek){

            if(visszateresErtek === null)
            {
                //console.log("Hiba volt API hivas utan");
            }
            else
            {
                API.jsonParserForRentals(visszateresErtek, listview);
            }

        });

        //COMBOX ADATOK
        API.get("/api/sites", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                //console.log("Hiba volt API hivas utan");
            }
            else
            {
                telephelyAdatok = API.jsonParserForSites_Value(visszateresErtek);
                telephelyNevek = API.jsonParserForSites_Name(visszateresErtek);
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
                ColumnLayout
                {

                    Text
                    {
                       Layout.alignment: Qt.AlignLeft

                       font.family: "Helvetica"
                       font.pointSize: 24
                       color: "purple"
                       text: qsTr("Bérlés befejezése")
                    }

                    RowLayout
                    {
                        Text {

                            text: qsTr("Leadás helye: ")
                        }
                        ComboBox {
                            id: comboBoxTelephelyID
                            model: telephelyNevek
                            Layout.alignment: Qt.AlignRight
                            Layout.rightMargin:  20
                            Layout.preferredWidth: 300
                        }
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
                            //anchors.bottom: parent
                            RowLayout {
                            anchors.fill: parent
                            spacing: 2
                            width: 480; height: 40
                            Text
                            {
                                text: rentalData.car.licensePlate
                                Layout.preferredWidth: 50
                                //Layout.fillHeight: true
                                Layout.alignment: Qt.AlignHCenter
                                //Layout.alignment: Qt.AlignLeft
                            }
                            Button
                            {
                                Layout.alignment: Qt.AlignHCenter
                                Layout.preferredWidth: 100
                                text: "További adatok"
                                onClicked: showPopUp(rentalData.car)
                            }

                            Button
                            {
                                Layout.alignment: Qt.AlignRight
                                Layout.preferredWidth: 100
                                text: "Leadás"
                                onClicked: {leadasRental(rentalData);}
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
         text: "Nincs elérhető bérlésed!"
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



    function checkName(valami) {
      return valami === comboBoxTelephelyID.currentText;
    }

    function leadasRental(selectedCar)
    {
        //console.log("IDE AKARJA LERAKNI: "+comboBoxTelephelyID.currentText);
        var poz = telephelyNevek.findIndex(checkName);
        rentalRemoveWindow.selectedCarID = selectedCar.id;
        //console.log(rentalRemoveWindow.parameter.id+" szemely szeretne leadni ezt a kocsit: "+rentalRemoveWindow.selectedCarID);
        if(typeof rentalRemoveWindow.selectedCarID !== "undefined")
        {
            API.post("/api/rentals/endRental?rentalId="+rentalRemoveWindow.selectedCarID+"&siteId="+telephelyAdatok[poz].id.toString());
            torlesListabol(selectedCar.car.licensePlate);
        }
        else
        {
            dialogBox.prompt("Hiányos adatok!", "Kérlek adj meg minden információt!");
        }
    }

    function torlesListabol(plate)
    {
        for (var i = 0; i < listview.model.count; i++) {
               if (listview.model.get(i).rentalData.car.licensePlate === plate) {
                   var spliced = listview.model.remove(i);
               }
           }
    }


}

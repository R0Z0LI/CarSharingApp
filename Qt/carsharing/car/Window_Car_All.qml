import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API

ApplicationWindow {
    id: carAllWindow
    title: qsTr("Banger - Car Sharing - Elérhető flotta")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog
    //Betoltesnel azonnal toltse le szerverrol
    Component.onCompleted: {
        API.get("/api/cars", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
                //dialogBox.prompt("Hiba!", "Nem érvényes adatok!");
            }
            else
            {
                console.log("ITT VAGYOK___"+visszateresErtek);
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


        ColumnLayout {
                anchors.fill: parent
                Text
                {
                   Layout.alignment: Qt.AlignLeft

                   font.family: "Helvetica"
                   font.pointSize: 24
                   color: "blue"
                   text: qsTr("Elérhető flotta")
                }
                Rectangle
                {
                    color:  "transparent"
                    width: 480
                    height: 300

                    ListView
                    {
                        id: listview
                        spacing: 10
                        implicitWidth: parent.width
                        model: listModel
                        anchors.fill: parent
                        focus: true
                        delegate:
                        Rectangle
                        {
                            color:  "whitesmoke"
                            width: 480
                            height: 40
                            RowLayout
                            {
                                anchors.fill: parent
                                spacing: 2
                                width: 480; height: 40
                                Text
                                {
                                    Layout.preferredWidth: 100
                                    text: carData.licensePlate
                                }
                                Button
                                {
                                    Layout.alignment: Qt.AlignHCenter
                                    Layout.preferredWidth: 100
                                    text: "További adatok"
                                    onClicked: showPopUp(carData)
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

    //PopUp ablak ertekeinek beallitasa
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
    Popup
    {
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
             Row
             {
                 Text
                 {   id: popupPosXText
                     text: ""
                 }
                 Text
                 {   id: popupPosYText
                     text: ""
                 }
             }
             Text
             {   id: popupSpaceText
                 text: ""
             }
             Text
             {   id: popupFuelText
                 text: ""
             }


         }
     }
}

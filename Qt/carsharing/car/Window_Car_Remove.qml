import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API

ApplicationWindow {
    id: carRemoveWindow
    title: qsTr("Banger - Car Sharing - Autó törlése")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog

    onClosing:{}
    Component.onCompleted: {
        API.get("/api/cars", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                //console.log("ITT VAGYOK___"+visszateresErtek);
                API.jsonParserForCars(visszateresErtek, listviewTorles);
                //console.log(listviewTorles.model.count);
            }

        });
    }
    ListModel
    {
        id: listModelTorles
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
            Text
            {
               Layout.alignment: Qt.AlignLeft

               font.family: "Helvetica"
               font.pointSize: 24
               color: "blue"
               text: qsTr("Kocsi törlése")
            }
            Rectangle
            {
                color:  "transparent"
                width: 480
                height: 300
                ListView
                {
                    id: listviewTorles
                    model: listModelTorles
                    anchors.fill: parent
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
                            Image
                            {
                                source: "../assets/closeicon.png"
                                Layout.preferredWidth: 20
                                Layout.preferredHeight: 20
                                Layout.alignment: Qt.AlignHCenter
                                fillMode:Image.PreserveAspectFit; clip:true
                                MouseArea
                                {
                                    anchors.fill: parent
                                    onClicked:
                                    {
                                        // do what you want here
                                        removeCar(carData)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //akkor jelenik meg ha nincs autó a rendszerben
    Text {
        visible: (listviewTorles.model.count === 0) ? true : false
        anchors.centerIn: parent
        text: "Nincs elérhető kocsi!"
    }


    function removeCar(carData)
    {
        var torlendo = carData.licensePlate;
        API.get("/api/cars/admin/delete/"+carData.licensePlate, function (visszateresErtek){
            //console.log("Kocsi torles visszateres tipusa: "+typeof visszateresErtek);
            torlesListabol(torlendo);
        });
    }
    //Torles utana kivenni a listabol az adott kocsit
    function torlesListabol(plate)
    {
        for (var i = 0; i < listviewTorles.model.count; i++) {
               if (listviewTorles.model.get(i).carData.licensePlate === plate) {
                   var spliced = listviewTorles.model.remove(i);
               }
           }
    }
}

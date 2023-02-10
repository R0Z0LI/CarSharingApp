import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API


ApplicationWindow {
    id: categoryRemoveWindow
    title: qsTr("Banger - Car Sharing - Kategória törlése")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog
    Component.onCompleted: {
        API.get("/api/categories/", function (visszateresErtek)
        {

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                API.jsonParserForCategoriesList(visszateresErtek, listviewTorles);
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
               color: "red"
               text: qsTr("Kategória törlése")
            }
            Rectangle
            {
                color:  "transparent"
                width: 480
                height: 300

                ListView
                {
                    id: listviewTorles
                    spacing: 10
                    implicitWidth: parent.width
                    model: listModelTorles
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
                            Text {
                                Layout.preferredWidth: 100
                                text: categoryData.name
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
                                        removeCar(categoryData)
                                    }
                                }

                            }

                        }
                    }
                }
            }
        }
    }

    Text {
        visible: (listviewTorles.model.count === 0) ? true : false
        anchors.centerIn: parent
        text: "Nincs elérhető kategória!"
    }


    //Kategória törlés API hívás
    function removeCar(categoryData)
    {
        var torlendo = categoryData.id;
        API.get("/api/categories/admin/delete/"+categoryData.id, function (visszateresErtek){
            //console.log("Kocsi torles visszateres tipusa: "+typeof visszateresErtek);
            torlesListabol(torlendo);
        });
    }

    //Törli a listából amennyiben sikeres a DB törlés
    function torlesListabol(id)
    {
        for (var i = 0; i < listviewTorles.model.count; i++)
        {
           if (listviewTorles.model.get(i).categoryData.id === id)
           {
             var spliced = listviewTorles.model.remove(i);
           }
        }
    }
}

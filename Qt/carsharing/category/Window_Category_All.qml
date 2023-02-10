import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API


ApplicationWindow {
    id: categoryAllWindow
    title: qsTr("Banger - Car Sharing - Elérhető kategóriák")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog
    Component.onCompleted: {
        API.get("/api/categories/", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                //console.log("ITT VAGYOK___"+visszateresErtek);
                API.jsonParserForCategoriesList(visszateresErtek, listview);
            }

        });
    }
    ListModel
    {
        id: listModel
    }

    //Listázza az összes elérhető kategóriát
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
               text: qsTr("Elérhető kategóriák")
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
                                text: categoryData.name
                            }
                            Text
                            {
                                Layout.preferredWidth: 100
                                text: categoryData.pricePerHour+"  Ft/perc"
                            }
                        }
                    }
                }
            }
        }
    }

    //megjelenik ha nincs elerheto kategoria
    Text {
        visible: (listview.model.count === 0) ? true : false
        anchors.centerIn: parent
        text: "Nincs elérhető kategória!"
    }



}

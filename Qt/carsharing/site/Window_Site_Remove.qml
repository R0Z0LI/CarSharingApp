import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API


ApplicationWindow {
    id: siteRemoveWindow
    title: qsTr("Banger - Car Sharing - Telephely törlése")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog

    //elerheto telephelyek lekeredezese torleshez
    Component.onCompleted: {
        API.get("/api/sites", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                API.jsonParserForSites(visszateresErtek, listviewTorles);
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


        ColumnLayout {
                anchors.fill: parent
                Text
                {
                   Layout.alignment: Qt.AlignLeft

                   font.family: "Helvetica"
                   font.pointSize: 24
                   color: "brown"
                   text: qsTr("Telephely törlése")
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
                                    text: siteData.address}

                                Image {
                                    source: "../assets/closeicon.png"
                                    Layout.preferredWidth: 20
                                    Layout.preferredHeight: 20
                                    Layout.alignment: Qt.AlignHCenter
                                    fillMode:Image.PreserveAspectFit; clip:true
                                    MouseArea {
                                            anchors.fill: parent
                                            onClicked: {
                                                // do what you want here
                                                removeSite(siteData)
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
        text: "Nincs elérhető telephely!"
        anchors.centerIn: parent
    }





    function removeSite(siteData)
    {
        var torlendo = siteData.id;
        API.get("/api/sites/admin/delete/"+siteData.id, function (visszateresErtek){
            //console.log("Kocsi torles visszateres tipusa: "+typeof visszateresErtek);
            torlesListabol(torlendo);
        });
    }

    function torlesListabol(id)
    {
        for (var i = 0; i < listviewTorles.model.count; i++)
        {
           if (listviewTorles.model.get(i).siteData.id === id)
           {
               var spliced = listviewTorles.model.remove(i);
           }
        }
    }
}

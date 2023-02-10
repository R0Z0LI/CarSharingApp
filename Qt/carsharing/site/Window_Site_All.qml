import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "../js/APiWorker.js" as API

ApplicationWindow {
    id: carAllWindow
    title: qsTr("Banger - Car Sharing - Telephelyek")
    width: 480
    height: 380
    visible: true
    modality: Qt.ApplicationModal
    flags: Qt.Dialog
    //Elerheto telepjelyek lekredezese
    Component.onCompleted: {
        API.get("/api/sites", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                console.log("ITT VAGYOK___"+visszateresErtek);
                API.jsonParserForSites(visszateresErtek, listview);
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
                   color: "brown"
                   text: qsTr("Telephelyek")
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
                                Text {
                                    Layout.preferredWidth: 100
                                    text: siteData.address}
                                Button
                                {
                                    Layout.preferredWidth: 100
                                    text: "További adatok"
                                    onClicked: showPopUp(siteData)
                                }
                            }
                        }
                    }
                }
        }
    }




    Text {
        visible: (listview.model.count === 0) ? true : false
        text: "Nincs elérhető telephely!"
        anchors.centerIn: parent
    }

    function showPopUp(siteData)
    {

        popupAddressText.text = "Cím: "+siteData.address
        popupEmailText.text = "E-mail: "+siteData.email
        popupPhoneText.text = "Telefonszám: "+siteData.phone
        popupKocsikText.text = "Telephelyen lévő kocsik száma: "+siteData.availableCars.length
        popup.open()
    }
    Popup {
            id: popup
            x: parent.width/2 - 150
            y: parent.height/2 -150
            width: 300
            height: 300
            modal: true
            focus: true
            closePolicy: Popup.CloseOnEscape | Popup.CloseOnPressOutsideParent
            contentItem: Column{

                Text
                {   id: popupAddressText
                    text: ""
                }
                Text
                {   id: popupEmailText
                    text: ""
                }
                Text
                {   id: popupPhoneText
                    text: ""
                }
                Text
                {   id: popupKocsikText
                    text: ""
                }
            }
        }
}

import QtQuick 2.0
import QtQuick.Dialogs
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
//Hiba eseten ez az ablak ugrik fel
//Header és Body parameteresen megadhato
Dialog
{
        id: dialog
        title: "titleText"
        anchors.centerIn: parent
        standardButtons: Dialog.Ok | Dialog.Cancel

        onAccepted: console.log("Ok clicked")
        onRejected: console.log("Cancel clicked")
        //visible: true
        contentItem: Rectangle {
            color: "transparent"
            implicitWidth: 400
            implicitHeight: 100
            Text {
                id: messageText
                text: ""
                color: "navy"
                anchors.centerIn: parent
            }
        }
        function torles()
        {
            dialog.close();
        }

        function show()
        {
            dialog.open();
        }
        function prompt(header, message)
        {
            dialog.title = header;
            messageText.text = message;
            dialog.show(qsTr(header));

        }
}

import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Layouts 1.1
import QtCharts
import "js/WindowWorker.js" as WindowWorker
import "js/APiWorker.js" as API

//About oldal
ApplicationWindow {
    id: mainWindow
    title: qsTr("Banger - About")
    width: 320
    height: 180
    visible: true
    DialogBox{id: dialogBox}


    Rectangle
    {
        color:  "transparent"
        width: 320
        height: 180
        anchors.centerIn: parent
        ColumnLayout
        {

            Text{
                Layout.preferredWidth: 300
                Layout.alignment: Qt.AlignHCenter
                text: qsTr("Készítette: \nKiss Dániel Márk\nRőhringer Zoltán\nSeres Soma")
            }

            Text{
                Layout.preferredWidth: 300
                Layout.alignment: Qt.AlignHCenter
                text: qsTr("Minden jog fenntartva! - Banger 2022")
            }

        }
    }


}

//semmi
/*import QtQuick 2.4
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.1*/
import QtQuick 2.4
import QtQuick.Controls 1.3
import QtQuick.Window 2.2
import QtQuick.Dialogs 1.2
import QtQuick.Layouts 1.1

//Desktop Qt 5.12.12!!!!
Item {
    width: 640
    height: 480


    property alias button3: button3
    property alias button2: button2
    property alias button1: button1

    RowLayout {
        anchors.centerIn: parent

        Button {

            id: button1
            visible: true
            text: qsTr("Press Me 1")
        }

        Button {
            id: button2
            text: qsTr("Press Me 2")
        }

        Button {
            id: button3
            text: qsTr("Press Me 3")
        }
    }
}

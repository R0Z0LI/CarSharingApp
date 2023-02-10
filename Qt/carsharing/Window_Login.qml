import QtQuick 2.4
import QtQuick.Controls
import QtQuick.Window 2.2
import QtQuick.Dialogs
import QtQuick.Layouts 1.1
import "js/WindowWorker.js" as WindowWorker
import "js/APiWorker.js" as API

//Main app starts from here
ApplicationWindow {
    id: loginWindow
    title: qsTr("Banger - Car Sharing")
    width: 640
    height: 480
    visible: true
    DialogBox{id: dialogBox}

    Rectangle
    {

        color:  "transparent"
        width: 300
        height: 350
        anchors.centerIn: parent

        ColumnLayout {
                anchors.fill: parent
                Image {
                    source: "assets/car.png"
                    Layout.preferredWidth: 100
                    Layout.preferredHeight: 100
                    Layout.alignment: Qt.AlignHCenter
                    fillMode:Image.PreserveAspectFit; clip:true
                }
                Text
                {
                   Layout.alignment: Qt.AlignHCenter
                   font.family: "Helvetica"
                   font.pointSize: 24
                   color: "green"
                   text: qsTr("Bejelentkezés")
                }
                Text
                {
                   Layout.leftMargin: 20
                   text: qsTr("Felhasználónév")
                }

                TextField
                {
                    id: userNameTextField
                    placeholderText: qsTr("Add meg a felhasználónevedet...")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true

                }

                Text
                {
                   Layout.leftMargin: 20
                   text: qsTr("Jelszó")
                }

                TextField
                {
                    id: passwordTextField
                    placeholderText: qsTr("Add meg a jelszavadat...")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    echoMode: TextInput.Password
                    selectByMouse: true
                }

                Button
                {
                    Layout.alignment: Qt.AlignHCenter
                    Layout.preferredWidth: 150
                    text: "Bejelentkezés"
                    onClicked: login()
                }

                Button
                {
                    Layout.preferredWidth: 300
                    text: "Regisztráció"
                    onClicked: WindowWorker.openRegistration(loginWindow)
                }
            }
    }

    function login()
    {
        if(userNameTextField.text.toString() === "" || passwordTextField.text.toString() === "")
        {
            dialogBox.prompt("Hiányos adatok!", "Kérlek adj meg minden információt!");
        }
        else
        {
            var dataToSend = {
                "password": passwordTextField.text.toString(),
                "username": userNameTextField.text.toString(),
            };
            API.post("/api/users/login", dataToSend, function (visszateresErtek){

                //console.log("VISSZA TIPU: "+typeof visszateresErtek);
                if (typeof visszateresErtek === "undefined")
                {
                    //console.log("LOGIN: visszteresErtek undefined volt");
                }
                else if(typeof visszateresErtek !== "object")
                {
                    //console.log("HIBA: "+visszateresErtek);
                    dialogBox.prompt("Hiba!", "Nem érvényes adatok!");
                }
                else
                {
                    dialogBox.torles();
                    //console.log("Belepes elott___:"+visszateresErtek.name);
                    WindowWorker.openMainWindow(visszateresErtek);
                }

            });
        }
    }

}

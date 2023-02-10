import QtQuick 2.4
import QtQuick.Controls 2.2
import QtQuick.Window 2.2
import QtQuick.Dialogs
import QtQuick.Layouts 1.1
import "../js/APiWorker.js" as API
import "../js/WindowWorker.js" as WindowWorker
import 'qrc:/../'

//https://interest.qt-project.narkive.com/kjkvNzC8/qml-popup-window-closing
ApplicationWindow {
    id:windowCustomerUpdate
    title: qsTr("Banger - Car Sharing - Felhasználó adatainak frissítése")
    width: 500
    height: 600
    flags: Qt.Dialog
    modality: Qt.ApplicationModal
    DialogBox{id: dialogBox}
    property var parameter;
    property var user;

    Component.onCompleted: {
        console.log("betoltott");
        API.get("/api/users/"+windowCustomerUpdate.parameter.id, function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
            }
            else
            {
                var obj = JSON.parse(visszateresErtek);
                windowCustomerUpdate.user = obj;
            }

        });
    }

    Rectangle{
        color:  "transparent"
        width: 300
        height: 600
        anchors.centerIn: parent

        ColumnLayout {
                anchors.fill: parent
                Text {
                   Layout.alignment: Qt.AlignHCenter
                   font.family: "Helvetica"
                   font.pointSize: 24
                   color: "orange"
                   text: qsTr("Adatok frissítése")
                }
                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Név")
                }
                TextField {
                    id: nameTextField
                    text: qsTr(windowCustomerUpdate.user.name)
                    placeholderText: qsTr("Add meg a nevedet . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }


                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Felhasználónév")
                }
                TextField {
                    id: userNameTextField
                    placeholderText: qsTr("Add meg a felhasználónevedet . . .")
                    anchors.bottomMargin: 30
                    text: qsTr(windowCustomerUpdate.user.username)
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }
                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Jelszó")
                }

                TextField {
                    id: passwordTextField
                    placeholderText: qsTr("Add meg a jelszavadat . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }

                Text {
                   Layout.leftMargin: 20
                   text: qsTr("E-mail cím")
                }

                TextField {
                    id: emailTextField
                    placeholderText: qsTr("Add meg az e-mail címedet . . .")
                    anchors.bottomMargin: 30
                    text: qsTr(windowCustomerUpdate.user.email)
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    inputMethodHints:Qt.ImhEmailCharactersOnly
                    selectByMouse: true
                }

                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Telefonszám")
                }

                TextField {
                    id: phoneTextField
                    placeholderText: qsTr("Add meg a telefonszámodat . . .")
                    anchors.bottomMargin: 30
                    text: qsTr(windowCustomerUpdate.user.phone)
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    inputMethodHints: Qt.ImhDigitsOnly
                    selectByMouse: true
                }

                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Jogosítványszám")
                }

                TextField {
                    id: licenceTextField
                    placeholderText: qsTr("Add meg a jogosítványod számát . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    text: qsTr(windowCustomerUpdate.user.licenseNumber)
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }
                Button {
                    Layout.preferredWidth: 300
                    text: "Frissítés"
                    onClicked: registrationClicked()
                }
                Button {
                    Layout.preferredWidth: 300
                    text: "Profil törlése"
                    onClicked: deleteClicked()
                }
        }

    }


    function deleteClicked()
    {
        API.get("/api/users/delete/"+windowCustomerUpdate.parameter.id);
        API.get("/api/users/logout");
        Qt.quit();
    }

    function registrationClicked()
    {

        if(nameTextField.text.toString() !== "" && userNameTextField.text.toString() !== "" && passwordTextField.text.toString() !== "" && emailTextField.text.toString() !== "" && phoneTextField.text.toString() !== "" &&  licenceTextField.text.toString() !== "")
        {
            //console.log("minden meg van adva!");
            var dataToSend = {
                "email": emailTextField.text.toString(),
                "password": passwordTextField.text.toString(),
                "username": userNameTextField.text.toString(),
                "name": nameTextField.text.toString(),
                "licenseNumber": licenceTextField.text.toString(),
                "phone": phoneTextField.text.toString(),
            };


            API.post("/api/users/update/"+windowCustomerUpdate.parameter.id,dataToSend, function (visszateresErtek){

                if(visszateresErtek === null)
                {
                    console.log("PROFIL FRISSITES: hiba volt");
                }
                else if(visszateresErtek === false)
                {
                    console.log("FALSAL TERT VISSZA");
                    dialogBox.prompt("Hibás adatok!", "Kérlek próbáld meg újra!");
                }
                else if(typeof visszateresErtek === "object")
                {
                    console.log("Visszatert___"+visszateresErtek.admin);
                    dialogBox.prompt("Sikeres módosítás!", "Sikeresen adatmódosítás!");
                }
                else
                {
                    //dialogBox.prompt("Ismeretlen hiba!", "Ismeretlen hiba lépett fel!");
                }

            });
        }
        else
        {
            dialogBox.prompt("Hiányos adatok!", "Kérlek adj meg minden információt!");
        }
    }
}

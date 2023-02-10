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
    id:windowCategoryRegistration
    title: qsTr("Banger - Car Sharing - Telephely hozzáadása")
    width: 500
    height: 500
    flags: Qt.Dialog
    modality: Qt.ApplicationModal

    DialogBox{id: dialogBox}

    //Telephely hozzaadasa
    Rectangle{
        color:  "transparent"
        width: 300
        height: 500
        anchors.centerIn: parent

        ColumnLayout
        {
            anchors.fill: parent
            Text {
               Layout.alignment: Qt.AlignHCenter
               font.family: "Helvetica"
               font.pointSize: 24
               color: "brown"
               text: qsTr("Telephely hozzáadása")
            }
            Text {
               Layout.leftMargin: 20
               text: qsTr("Cím")
            }
            TextField {
                id: adressTextField
                placeholderText: qsTr("Add meg a telephely címét . . .")
                anchors.bottomMargin: 30
                Layout.alignment: Qt.AlignRight
                Layout.rightMargin:  20
                Layout.preferredWidth: 250
                selectByMouse: true
            }


            Text {
               Layout.leftMargin: 20
               text: qsTr("Telefonszám")
            }
            TextField {
                id: phoneTextField
                placeholderText: qsTr("Add meg az telefonszámot . . .")
                anchors.bottomMargin: 30
                Layout.alignment: Qt.AlignRight
                Layout.rightMargin:  20
                Layout.preferredWidth: 250
                selectByMouse: true
            }
            Text {
               Layout.leftMargin: 20
               text: qsTr("E-mail")
            }
            TextField {
                id: emailTextField
                placeholderText: qsTr("Add meg az e-mailt . . .")
                anchors.bottomMargin: 30
                Layout.alignment: Qt.AlignRight
                Layout.rightMargin:  20
                Layout.preferredWidth: 250
                selectByMouse: true
            }
            Button {
                Layout.preferredWidth: 300
                text: "Felvétel"
                onClicked: registrationClicked()
            }
        }

    }
    function registrationClicked()
    {
        if(adressTextField.text.toString() !== "" && phoneTextField.text.toString() !== ""&& emailTextField.text.toString() !== "")
        {
            console.log("minden meg van adva!");
            var dataToSend = {
                "address": adressTextField.text,
                "phone": phoneTextField.text,
                "email": emailTextField.text,
            };


            API.post("/api/sites/admin/add",dataToSend, function (visszateresErtek){

                if(visszateresErtek === null)
                {
                    console.log("Telephely hozzaadas: hiba volt");
                }
                else if(visszateresErtek === false)
                {
                    console.log("FALSAL TERT VISSZA");
                    dialogBox.prompt("Hibás adatok!", "Kérlek próbáld meg újra!");
                }
                else if(typeof visszateresErtek === "object")
                {
                    console.log("Visszatert___"+visszateresErtek.admin);
                    if(visszateresErtek.admin !== true)
                    {
                         dialogBox.prompt("Sikeres regisztráció!", "Sikeresen regisztráltad a telephelyet!");
                    }
                    else
                    {
                        console.log("maradj itt");
                    }

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

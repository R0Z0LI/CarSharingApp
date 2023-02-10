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
    id:windowCarRegistration
    title: qsTr("Banger - Car Sharing - Autó hozzáadása")
    width: 500
    height: 600
    flags: Qt.Dialog
    modality: Qt.ApplicationModal

    DialogBox{id: dialogBox}
    //ComboBox adatainak tarolasa
    property var ertekek : [];
    property var tipusNevek: [];

    property var telephelyNevek: [];
    property var telephelyAdatok: [];

    Component.onCompleted: {
        //combobox kategoria
        API.get("/api/categories/", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("hiba volt");
                //dialogBox.prompt("Hiba!", "Nem érvényes adatok!");
            }
            else
            {
                console.log("Visszatert___"+visszateresErtek);
                ertekek = API.jsonParserForCategories(visszateresErtek);
                tipusNevek = API.jsonParserForCategoriesName(visszateresErtek);
            }

        });

        //COMBOX telephelyAdatok
        API.get("/api/sites", function (visszateresErtek){

            if(visszateresErtek === null)
            {
                console.log("Hiba volt API hivas utan");
            }
            else
            {
                telephelyAdatok = API.jsonParserForSites_Value(visszateresErtek);
                telephelyNevek = API.jsonParserForSites_Name(visszateresErtek);
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
                   color: "blue"
                   text: qsTr("Autó hozzáadása")
                }
                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Gyártó")
                }
                TextField {
                    id: gyartoTextField
                    placeholderText: qsTr("Add meg a gyártót . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }


                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Model")
                }
                TextField {
                    id: modelTextField
                    placeholderText: qsTr("Add meg a modelt . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }
                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Rendszám")
                }

                TextField {
                    id: licensePlateTextField
                    placeholderText: qsTr("Add meg a rendszámot . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    selectByMouse: true
                }

                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Gyártási év")
                }

                TextField {
                    id: yearProducedTextField
                    placeholderText: qsTr("Add meg a gyártás évét. . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    inputMethodHints:Qt.ImhEmailCharactersOnly
                    selectByMouse: true
                }

                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Férőhely")
                }

                TextField {
                    id: spaceTextField
                    placeholderText: qsTr("Add meg a férőhelyek számát . . .")
                    anchors.bottomMargin: 30
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                    inputMethodHints: Qt.ImhDigitsOnly
                    selectByMouse: true
                }
                Text {
                   Layout.leftMargin: 20
                   text: qsTr("Kategória")
                }

                ComboBox {
                    id: comboBoxID
                    width: 200
                    model: tipusNevek
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                }

                Text {

                    text: qsTr("Telephelye: ")
                }
                ComboBox {
                    id: comboBoxTelephelyID
                    model: telephelyNevek
                    Layout.alignment: Qt.AlignRight
                    Layout.rightMargin:  20
                    Layout.preferredWidth: 250
                }


                Button {
                    Layout.preferredWidth: 300
                    text: "Regisztrálás"
                    onClicked: registrationClicked()
                }


        }

    }


    //Kereses, hogy mi az index
    function check(age) {
      return age === comboBoxID.currentText;
    }

    function checkName(valami) {
      return valami === comboBoxTelephelyID.currentText;
    }

    function registrationClicked()
    {
        console.log(comboBoxID.currentText);
        var pozTelephely = telephelyNevek.findIndex(checkName);
        var poz = tipusNevek.findIndex(check);
        console.log(ertekek[poz].id + "   "+ertekek[poz].name)
        if(licensePlateTextField.text.toString() !== "" && modelTextField.text.toString() !== "" && gyartoTextField.text.toString() !== "" && yearProducedTextField.text.toString() !== "" && spaceTextField.text.toString() !== "")
        {
            //JSON Adat osszeallitasa
            console.log("minden meg van adva!");
            var dataToSend = {
                "licensePlate": licensePlateTextField.text.toString(),
                "model": modelTextField.text.toString(),
                "manufacturer": gyartoTextField.text.toString(),
                "yearProduced": yearProducedTextField.text,
                "categoryId": ertekek[poz].id.toString(),
                "siteId": telephelyAdatok[pozTelephely].id.toString(),
                "renter": "Nincs",
                "space": spaceTextField.text,
            };


            API.post("/api/cars/admin/add",dataToSend, function (visszateresErtek){

                if(visszateresErtek === null)
                {
                    console.log("kocsi hozzaadas: hiba volt");
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
                         dialogBox.prompt("Sikeres regisztráció!", "Sikeresen regisztráltad a kocsit! Jó utat!");
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

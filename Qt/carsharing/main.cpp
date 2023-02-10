#include "mainwindow.h"
#include "other.h"
#include <string>

#include <QtCharts>
#include <QApplication>
#include <QQmlApplicationEngine>

int main(int argc, char *argv[])
{
    //qmlRegisterType<APiWorker>("APiWorker",1,0,"APiWorker");
    //QGuiApplication app(argc, argv);
    //Backend backend;
    //QChart csak QApplication alatt megy
    QApplication app(argc, argv);
    //https://stackoverflow.com/questions/27913666/qml-closing-qquickwindow-closes-my-application
    //qApp->setQuitOnLastWindowClosed(false);

    QQmlApplicationEngine engine;
    // engine.rootContext()->setContextProperty("backend", &backend);

    engine.load(QUrl(QStringLiteral("qrc:/Window_Login.qml")));
        if (engine.rootObjects().isEmpty())
            return -1;

    return app.exec();
}

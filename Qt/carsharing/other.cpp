#include "other.h"
#include <QDebug>

APiWorker::APiWorker():QObject(){}

void APiWorker::proba()
{
      qDebug() << "proba";
}

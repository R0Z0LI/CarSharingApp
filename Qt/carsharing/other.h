#ifndef OTHER_H
#define OTHER_H

#include <QObject>

class APiWorker : public QObject
{
Q_OBJECT
public:
    explicit APiWorker();
    Q_INVOKABLE void proba();
};

#endif // OTHER_H

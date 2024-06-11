package olechka.lab8.server;

import lombok.Getter;

import java.net.SocketAddress;

//описывает удаленный объект
public class RemoteClient {
    @Getter
    private final SocketAddress address;
    @Getter
    private int lastRequestOrder;

    public RemoteClient(SocketAddress address) {
        this.address = address;
    }

    public boolean canExecuteCommandOfOrder(int requestOrder) {
        // Первый пакет, значит клиент только подключился, сбрасываем текущий lastRequestOrder и разрешаем выполнить команду
        if (requestOrder == 0) {
            lastRequestOrder = 0;
            return true;
        }
        // Иначе же разрешаем выполнить команду только если ее requestOrder больше lastRequestOrder, и обновляем значение lastRequestOrder
        if (requestOrder > lastRequestOrder) {
            lastRequestOrder = requestOrder;
            return true;
        }
        // Кажется, нам повторно пришла команда, которую мы уже выполнили. Нет надобности выполнять ее снова:
        return false;
    }
}

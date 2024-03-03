package olechka.lab5.interaction;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {
    private final Scanner scanner;
    private final PrintStream out;
    private boolean isClosed;

    public Console() {
        scanner = new Scanner(System.in);
        out = System.out;
    }

    public Console(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public PrintStream getOut() {
        return out;
    }

    public String next() {
        try {
            return scanner.next();
        } catch (NoSuchElementException e) {
            isClosed = true;
            throw new InterationClosedException();
        }
    }

    public String nextLine() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            isClosed = true;
            throw new InterationClosedException();
        }
    }

    public int nextInt() {
        try {
            return scanner.nextInt();
        } catch (NoSuchElementException e) {
            isClosed = true;
            throw new InterationClosedException();
        }
    }

    public boolean isInputClosed() {
        return isClosed;
    }

    public static PrintStream getNullStream() {
        // Мы создаем пустой поток, который ничего никуда не пишет
        // И передаем его в парс
        // Чтобы все запросы на "введите что-то" никуда не выводилисьб
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        });
    }
}

package school.sptech.crudrisecanvas.utils;

public class Queue <T> {
    private int tamanho, inicio, fim;
    private T[] fila;

    @SuppressWarnings("unchecked")
    public Queue(int capacidade) {
        this.fila = (T[]) new Object[capacidade];
        this.tamanho = 0;
        this.inicio = 0;
        this.fim = 0;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException();

        } else {
            fila[fim] = info;
            fim = (fim + 1) % fila.length;
            tamanho++;
        }
    }

    public T peek() {
        return fila[inicio];
    }

    public T poll() {
        if (isEmpty()) {
            System.out.println("Fila vazia");
            return null;

        } else {
            T valor = fila[inicio];
            fila[inicio] = null;

            inicio = (inicio + 1) % fila.length;
            tamanho--;
            return valor;
        }
    }

    public int getTamanho() {
        return this.tamanho;
    }

    public int getInicio() {
        return this.inicio;
    }

    public int getFim() {
        return this.fim;
    }
}


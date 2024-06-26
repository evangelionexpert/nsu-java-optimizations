package ru.nsu.fit.smolyakov.optimiaztcya;

import java.util.ArrayList;
import java.util.List;

enum Singleton {
    INSTANCE;

    private int a = 5;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}

// https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.mat.ui.help%2Fconcepts%2Fshallowretainedheap.html
class Bean {
    private final String name;
    private final byte[] byteArr = {0x0A, 0x1B, 0x2C, 0x3D, 0x4F, 0x5E, 0x6D};
    private final List<Object> list = new ArrayList<>(16);
    private Bean ref;
    private final int a = 100500;
    private final int b = 666;

    public Bean(String name, Bean ref) {
        this.name = name;
        this.ref = ref;
    }

    public void setRef(Bean ref) {
        this.ref = ref;
    }
}

class NormalThread extends Thread {
    private final Singleton singletonRef;
    private final int no;

    NormalThread(Singleton singletonRef, int no) {
        this.singletonRef = singletonRef;
        this.no = no;
    }

    @Override
    public void run() {
        var myBean = new Bean("bebean" + no, null);
        myBean.setRef(myBean);

        var singletonRef = Singleton.INSTANCE;
        System.out.println(singletonRef.getA());

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Main {
    public final static int N_THREADS = 10;

    public static void main(String[] args) {
        var bean1 = new Bean("odin", null);
        var bean2 = new Bean("dva", bean1);
        var bean3 = new Bean("tri", bean2);
        var bean4 = new Bean("chetyre", bean3);
        bean1.setRef(bean4);

        for (int i = 0; i < N_THREADS; i++) {
            new NormalThread(Singleton.INSTANCE, i).start();
        }

        if (args.length > 0 && args[0].equals("--vredno")) {
            new Thread(() -> {
                var list = new ArrayList<>(100000);
                for (;;) {
                    list.add(new byte[100000]);
                }
            }).start();
        }
    }
}

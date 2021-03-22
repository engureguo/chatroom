package com.engure.util;

import java.util.LinkedList;

public class MyTaskQueue<E> {

    LinkedList<E> list;

    public MyTaskQueue() {
        list = new LinkedList<>();
    }

    /**
     * get first element
     * @return
     */
    public E removeFirst() {
        E e;
        synchronized (list) {
            if (list.size()!=0) {
                e = list.removeFirst();
            } else {
                e = null;
            }
        }
        return e;
    }

    /**
     * add to last
     * @param e
     */
    public void addToLast(E e) {
        synchronized (list){
            list.add(e);
        }
    }

    /**
     * 是否有任务
     * @return
     */
    public boolean isEmpty() {
        boolean b;
        synchronized (list) {
            b = (list.size()==0?true:false);
        }
        return b;
    }

    /**
     * get and remove all elements
     * @return
     */
    public LinkedList<E> getAll() {
        int len;
        LinkedList<E> l =new LinkedList<>();

        synchronized (list) {
            len = list.size();
            if (len != 0)  {
                while (!list.isEmpty()) {
                    l.add(list.removeLast());
                }
            }
        }

        return l;
    }

    @Override
    public String toString() {
        return "{" +
                "list=" + list +
                '}';
    }
}

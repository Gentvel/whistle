package com.whistle.code.design.observer.eventreactor;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class Client {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        Boss boss = new Boss();
        Employee employee = new Employee();
        monitor.add(boss);
        monitor.add(employee);
        monitor.addEvent(new UpdateEvent(false));
        monitor.addEvent(new UpdateEvent(true));
        monitor.publish();
    }
}

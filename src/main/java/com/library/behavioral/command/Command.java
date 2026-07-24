package com.library.behavioral.command;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}
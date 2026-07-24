package com.library.behavioral.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandInvoker {
    private Stack<Command> commandHistory;
    private List<String> historyDescriptions;
    
    public CommandInvoker() {
        this.commandHistory = new Stack<>();
        this.historyDescriptions = new ArrayList<>();
    }
    
    public void executeCommand(Command command) {
        if (command == null) {
            System.out.println("❌ Command Error: Command is null!");
            return;
        }
        
        command.execute();
        commandHistory.push(command);
        historyDescriptions.add(command.getDescription());
        
        System.out.println("   [Invoker] Command stored in history. Total: " + commandHistory.size());
    }
    
    public void undoLastCommand() {
        if (commandHistory.isEmpty()) {
            System.out.println("⚠️ No commands to undo!");
            return;
        }
        
        Command lastCommand = commandHistory.pop();
        historyDescriptions.remove(historyDescriptions.size() - 1);
        
        System.out.println("\n   [Invoker] Undoing last command...");
        lastCommand.undo();
        System.out.println("   [Invoker] Command undone. Remaining: " + commandHistory.size());
    }
    
    public List<String> getCommandHistory() {
        return historyDescriptions;
    }
    
    public int getCommandCount() {
        return commandHistory.size();
    }
    
    public void clearHistory() {
        commandHistory.clear();
        historyDescriptions.clear();
        System.out.println("   [Invoker] Command history cleared.");
    }
}
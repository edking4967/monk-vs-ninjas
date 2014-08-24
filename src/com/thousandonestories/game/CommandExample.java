package com.thousandonestories.game;

public class CommandExample {

    public interface Command 
    {
        public void execute(Object data);
    }

    public static void callCommand(Command command, Object data) 
    {
        command.execute(data);
    }

    public static void main(String... args) 
    {
        callCommand(new PrintCommand(), "hello world");
    }
	
}

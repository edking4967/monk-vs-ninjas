package com.thousandonestories.game;

import com.thousandonestories.game.CommandExample.Command;

public class PrintCommand implements Command 
{
    public void execute(Object data) 
    {
        System.out.println(data.toString());
    }    
}
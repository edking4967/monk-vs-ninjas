package com.thousandonestories.game.tags;
import com.thousandonestories.game.tags.Tag;
import java.util.ArrayList;

public class TagStore
{
    ArrayList tags;

    public TagStore()
    {
        tags = new ArrayList<String>();
    }

    public boolean contains(String t)
    {
        return tags.contains(t);
    }

    public void add(String t)
    {
        tags.add(t);
    }
}

# Why are bitmaps global variables in OldPanel?

isRunning = True;
while(isRunning):
    InputMgr.takeInput() # Happens in OnTouchEvent()
    GameObjects.update()
    Canvas.draw()

# Player input and events in-game will both affect the game objects.
# Does input mgr take input and interpret as run / jump / fire?
# Then update() is if(player.jump())
# or 

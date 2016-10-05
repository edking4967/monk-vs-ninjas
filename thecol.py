skyorig = 0xff7ec0ee
skycolor = skyorig
skyStep = 0x0010101

def decColor(color, inc):
    color -= inc
    print hex(color)
    return color

def printColor(color):
    thecol = fg('#' + hex(color).split('x')[1])
    res = attr('reset')
    print(thecol + hex(color) + res)


def printColorString(colorstring):
    thecol = fg(colorstring)
    res = attr('reset')
    print(thecol + colorstring + res)



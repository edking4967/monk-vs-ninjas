#!/usr/bin/python
from os import path

# Strings and shortcuts
extn = '.colors'  # ".color" files define all the colors in a swatch

# A color.
class Color:
  name = "white"
  rgb= 0xffffff
# A swatch contains multiple colors.
class Swatch:
  color = Color

# Display output modules:
def displayInTerminal():
  return;  # Mind seems stuck in java
def outputInCSS():
  return;
# Enough display output modules

# The main:
def processColors( color ): # Now we are in C it seems

  fall = open( 'fall' + extn , 'r')
  lines = fall.read()
  lines_data = filter(None, lines.split('\n'))

  for line in lines_data:
    words = line.split(' ')
    rgb = words[0]
    name = words[1]
    print 'rgb= ' + rgb + ' ' + 'name= ' + name
    return;

# An example
color = open( 'fall' + extn , 'r')

processColors( 'fall' )


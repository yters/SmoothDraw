# SmoothDraw
Drawing using Bezier curves.

The user is presented with a blank canvas.  He drags his cursor on the canvas, and a curve is interpolated using the last 20 points to draw a line on the canvas.

From the base directory, compile the core source:

`javac src/smoothdraw/*.java`

Next, compile the test widget:

`javac -cp src src/smoothdrawtest/*.java`

Finally, run the test widget:

`java -cp src smoothdrawtest.SmoothDrawTest`

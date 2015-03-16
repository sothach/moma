# moma
Assigment for:
Programming Mobile Applications for Android Handheld Systems: Part 1
(March, 2015)

## Overview
This app displays a canvas of randomly-drawn colored rectangles.
 * Shaking the phone will cause a new image to be generated
 * The sparsity slider can be used to vary the  density of the image.
 * The Action bar menu provides a link to the Museum of Modern Art's web sites, plus a 'share' option, to send the  current image to another App (e.g., to email, post on social media, upload to cloud storage)
 * Should the user find a particular piece pleasing or intersting, the 'share' command can be used to send the image to another App, e.g., photo gallery, social media or email

## Acceptance Criteria

 * Does this app's user interface initially contain at least 5 colored rectangles, at least one of which is white/grey and at least one of which is not white/grey? 
 * Does this app's user interface include a seek bar?
 * If you drag this app's seek bar, do the colored rectangles change color? If there is no seek bar or there are no colored rectangles, then the answer for this question is No.
 * Does this app's user interface allow the user to view an options menu displaying the text, "More Information"? If there is no options menu, then the answer for this question is No.
 * When the user clicks on the "More Information" option, do the app present a Dialog with two Buttons labeled "Visit MOMA" and "Not Now?" If there is no options menu or option item, then the answer for this question is No.
 * If the user clicks on the Dialog Button labeled, "Not Now" does the Dialog disappear? If there is no Dialog or no "Not Now" button, then the answer for this question is No.
 * If the user clicks on the Dialog Button labeled, "Visit MOMA" does a browser open displaying a page at the www.moma.org web site? If there is no Dialog or no "Visit MOMA" button, then the answer for this question is No.
 
## Implementation
The art is created in a custom view, using this technique:
 * Starting at the top left, a spiral path is calculated (increasing both the angle 'theta', and radius 'r')
 * The increment of both thea and 'r' is randomized, influenced by the 'sparsity' slider value
 * At each point, a rectangle is drawn, of a random size and color

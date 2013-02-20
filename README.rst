Notes Trainer for Android
=========================

Notes Trainer is a simple tool to train oneself at reading music.  It
just shows a random note on a music score and waits for the trainee to
press the button with the note name.

Features:

* selection of music notation (english (A B C), latin (do re mi), german (A H C)

* selection of the range in which the notes are picked at random

* selection of the clef to use (treble, bass, alto, tenor)

* several operation modes:

  * training, no time limit
  * one minute test

* free/open-source software, which you can modify to suit your needs


How to build
------------

You must have the source for svgandroid in a ../svgandroid.

Get svgandroid with::
 svn clone http://svg-android.googlecode.com/svn/trunk/svgandroid ../svgandroid

Prepare svgandroid with::
 android update lib-project -p .

Build Notes Trainer with::
 ant clean debug


Credits
-------

Clef graphics are from the MediaWiki project.  G clef is public
domain, F and C clefs are GFDL/CC-by-sa.

* http://commons.wikimedia.org/wiki/File:GClef.svg
* http://commons.wikimedia.org/wiki/File:FClef.svg
* http://commons.wikimedia.org/wiki/File:CClef.svg

The SeekBarPreference is from http://robobunny.com (public domain).

* http://robobunny.com/wp/2011/08/13/android-seekbar-preference/

Most ideas came from using the LearnMusicNotes app.  It had however
too many structural issues, that made me opt for writing a new app
from scratch.

* http://sourceforge.net/projects/learnmusicnotes/

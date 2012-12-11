gapPlayer
=========
gapPlayer is an Android app, based on PhoneGap. Pretty much it is a plain
PhoneGap app, except that it pulls its Web content from external storage,
rather than from `assets/`. The objective is for gapPlayer to serve as a
test container for rapid prototyping and development of PhoneGap-based apps.

Usage
-----
Simply run gapPlayer. On its first run, it will unpack an initial set of
Apache Cordova Web content into your external storage and display it. Specifically,
it writes the files to the `www/` directory inside of gapPlayer's corner
of external storage (what Android developers know as `getExternalFilesDir(null)`.
If you run this on a device, then mount the device as a drive or volume on
your development machine, you should see gapPlayer's files in
`Android/data/com.commonsware.gapplayer/files`.

The contents of that `files/` directory should be separate PhoneGap directories,
simulating different projects. Initially, of course, there will only be the
`www` project, but you are welcome to add others (e.g., clone `www` into another
name and modify the contents). When the app is launched, it will populate a
drop-down list in the action bar with all the available projects &mdash;
choosing a different project in the drop-down list will load that project's
content into PhoneGap. Clicking the refresh icon in the action bar will reload
the current project in PhoneGap, to reflect any changes you may have made to
its files.

Version
-------
This is version v0.1.0 of this application, meaning it is brand-spankin' new.

License
-------
The code contributed by CommonsWare to this project is licensed under the Apache
Software License 2.0, per the terms of the included LICENSE
file.

The code from PhoneGap and Apache Cordova is covered by their licenses, which
should also be the Apache Software License 2.0.

Questions
---------
If you have questions regarding the use of this source code, please post a question
on [StackOverflow](http://stackoverflow.com/questions/ask) tagged with `commonsware` and `android`. Be sure to indicate
what app you are having issues with, and be sure to include source code 
and stack traces if you are encountering crashes.

If you have encountered what is clearly a bug, or if you have a feature request,
please post an [issue](https://github.com/commonsguy/LockscreenLocker/issues).
Be certain to include complete steps for reproducing the issue.

Do not ask for help via Twitter.

Also, if you plan on hacking
on the code with an eye for contributing something back,
please open an issue that we can use for discussing
implementation details. Just lobbing a pull request over
the fence may work, but it may not.

Release Notes
-------------
- v0.1.0: initial release

Who Made This?
--------------
<a href="http://commonsware.com">![CommonsWare](http://commonsware.com/images/logo.png)</a>
 

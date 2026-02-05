# meteor

Meteor is a modernized, and multi-platform rs2 client  
![Version](https://img.shields.io/badge/version-3.0.0-blue)  ![Version](https://img.shields.io/badge/Java-21-blue)  ![Version](https://img.shields.io/badge/Kotlin-2.3.0-blue)  ![Version](https://img.shields.io/badge/Compose-1.10.0-blue) ![Version](https://img.shields.io/badge/Android-36-blue)    
It has a similar api and tooling to RuneLite, but uses Kotlin/Compose rather than Java/Swing  
  
modules
```
api               (formerly runelite-api)
api-rs            (formerly runescape-api
client-android    (JVM 21)
client-common     (JVM 21)
client-desktop    (JVM 21)
common            (JVM 1.8) (can be used in rs module)
injected-client
injector
mixins            (JVM 1.8)
rs                (JVM 1.8) (formerly runescape-client) 
```

requirements  
```
JVM 21
Latest Android Studio / SDK 36
Latest IntelliJ Idea
```

setup
```
by default, rs will connect to 127.0.0.1 on desktop, or 10.0.2.2 on android
(10.0.2.2 routes to emulator host, or what you would expect 127.0.0.1 to do if not on an emulator)

You will need to manually change that if you are not running the server on the same pc

open the project in IntelliJ Idea, let it sync

run `injectMultiplatform build`

then you can do:

`rs:run`
`client-desktop:run`

or select your android device, and click the start button
(Idea will populate the android configuration automatically)
```
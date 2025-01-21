### Meteor - Multiplatform (Desktop/Android) 
---
**This connects to 2004scape.org by default and follows their rules diligently**
---

![image](https://github.com/user-attachments/assets/d9fffa2c-3008-42d2-b488-c26665eb8a38)
  
# Requirements  

Android 11 R device  
JDK 22  
Latest Android Studio / NDK  
(These all MUST BE INSTALLED)  
  
# Building  

you can build either platform using android:build or desktop:build  
You can run either platform via Android Studio or IntelliJ (with the Android plugin) with the included run configurations  
or you can use the output apk at android/build/outputs/apk  
or you can create a distributable using the included run configuration if you replace the jbr runtime with an oracle 21 runtime  

# Info
The project was entirely re-written using a shared code concept using Compose with no reflection / injection  
This unified code concept is still very new so a lot of things have regressed (input on android)  
expect a lot of things moving to the common module and bug fixes where they pop up  

```
Forbidden features (unless otherwise permitted by Lost-City):  
    artificial input of any kind  
    camera zoom / middle mouse rotate  
    menu entry swapping (including changing menu entry text)
    overlays of any kind over the game
    visual game state tracking in UI form be it swing or compose

    Per Pazaz:
    "in general: don’t give yourself an advantage that forces a new meta on the community.
        The original experience should be a viable option for people to play with"

    Per Zeruth:
    "We will absolutely be respecting the project here, if things change regarding content
        that is forbidden or not, it will be reflected here."
```
  
Please create an issue ONLY if your phone does not have one already, and ONLY for rendering issues.  

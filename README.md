### Meteor - Android  
---
**ALPHA - Device Testing**  
**This branch connects to 2004scape.org by default**
---
Galaxy S24 Ultra  
![image](https://github.com/user-attachments/assets/af862eff-61e6-4123-a645-20011111a4b8)  

Video  
[![Video](https://img.youtube.com/vi/cCQErZ1HsmU/0.jpg)](https://www.youtube.com/watch?v=cCQErZ1HsmU)

# Building  

Get latest Android Studio / NDK  
Execute bundled run configuration 'Inject' at clone and when modifying api/api-rs/deob/mixins  
You can now build the 'app' module  

# Info
You should be able to use the Account plugin to configure Auto User/Pass, and that will let you login when hitting existing user.  
(properties are stored in sandboxed app data, no other apps can access this data unless your device is rooted.)  
  
You should be able to rotate the camera, long press to open menu, and tap to click.  
  
Currently the game renders at 50fps in the Android Emulator and on my personal S24 Ultra.  
Performance has not been tested on other devices.  
  
Please create an issue ONLY if your phone does not have one already, and ONLY for rendering issues.  

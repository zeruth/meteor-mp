### Meteor - Android  
---
**ALPHA - Device Testing**  
**This branch connects to 2004scape.org by default**
---
Galaxy S24 Ultra  
![image](https://github.com/user-attachments/assets/af862eff-61e6-4123-a645-20011111a4b8)
![image](https://github.com/user-attachments/assets/98623e88-e750-429d-b8c6-14936295ce7f)

# Building  

Get latest Android Studio / NDK  
Execute bundled run configuration 'Inject' at clone and when modifying api/api-rs/deob/mixins  
You can now build the 'app' module  

# Info
You should be able to use the Account plugin to configure Auto User/Pass, and that will let you login when hitting existing user.  
(properties are stored in sandboxed app data, no other apps can access this data unless your device is rooted.)  
  
You should be able to rotate the camera, long press to open menu, and tap to click.  
  
Currently the AWT side of things is pretty heavy in-game, this can likely be improved substantially.  
OSRS can render at 50fps using the same native-awt lib  
  
Please create an issue ONLY if your phone does not have one already, and ONLY for rendering issues.  

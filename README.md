Repo created to store smartthings smartapps and device handlers I find. Also to play with and create my own

#Kodi Manager (Smartapp) & Kodi Client (Device Handler)
I wanted the ability to interact with and trigger events from Kodi. Having looked round, there did not seem to be many options that did what I was looking to do. The closest was <a href="https://github.com/Toliver182/SmartThings-Kodi">Toliver182's</a>, but there is a lot more I wanted and doesn't seem to be in active development. So I forked it and have re-written and added a lot to it.

##Key Features

Communicate to and from Kodi  
Get current state (Playing, Paused, Stopped, Shutdown, Startup)  
Get current type - this is taken directly from Kodi  
Get current category (Movie, TV Show, Sports)  
All above configurable in preferences within the DTH
A fully functioning remote control right within your Smartthings App  


<img src="https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/screenshot%201%20kodi-client%20v1.1.png" width="256">
<img src="https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/screenshot%202%20kodi-client%20v1.1.png" width="256">
<img src="https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/screenshot%203%20kodi-client%20v1.1.png" width="256">



Interact with Kodi, both ways, to and from Smartthings. Trigger lights etc from events on Kodi i.e. When a 'Movie' plays turn on Movie Lights. Alternatively, when the doorbell rings, pause Kodi. One of the key things I wanted was to switch off my TV when I leave the house. This is why I added the shutdown capability, I have Kodi running on a RPi and set CEC apater to turn the TV off when Kodi shuts down, perfect. There is no startup capability, as once off there is no way to start up. So I use a smart outlet to re power the RPi, which fires up Kodi and the TV.
 NB if you are using a different system to run Kodi (i.e. media centre) you may be able to use WOL to wake it.  
 I recommend using <a href="http://thingsthataresmart.wiki/index.php?title=CoRE">CoRE</a> to do the automation, I have ensured that the attributes are exposed to CoRE for easy integration. It can also handle the WOL for you if needed
 
###Installation  
 Kodi needs to be enabled for control over HTTP. So enable the following:  
 system>services>Web Server>Allow remote control via HTTP  
 **NB** You will need to know the port (required), username (if set) and password (if set) from this screen to set up Kodi Manager
 
 You will also need to set up the <a href="http://kodi.wiki/view/Add-on:Kodi_Callbacks">Callbacks</a> plugin. This is how Kodi talks to Smartthings, meaning you get real time updates. Once installed you need to create the tasks for play, pause, stop, resume, shutdown, startup and then add them to the appropriate events (read the Wiki in the link for help)  
 **NB** You only need this for two way comms and you can install this after installing the smartthings app & device handler
 
 Choose below what version you want (stable or beta) and the install method, I recommend adding the git repo to your IDE to enable you to get any updates easily.
  
 **For current stable release:**  
 Add the following to your IDE:  
 Owner:     north3221  
 Name:      north3221SmartThings  
 Branch:    master  
 
 Manual (copy and paste the code into your IDE):  
 Device Handler:    <a href="https://raw.githubusercontent.com/north3221/north3221SmartThings/master/devicetypes/north3221/kodi-client.src/kodi-client.groovy">Copy and paste this code into a new 'Device Handler'</a>  
 Smartapp:          <a href="https://raw.githubusercontent.com/north3221/north3221SmartThings/master/smartapps/north3221/kodi-manager-cbs.src/kodi-manager-cbs.groovy">Copy and paste this code into a new 'Smartapp'</a>
 
 **For the Beta version add the following to your IDE:**  
 Owner:     north3221  
 Name:      north3221SmartThings  
 Branch:    beta  
 
 Manual (copy and paste the code into your IDE):  
 Device Handler:    <a href="https://raw.githubusercontent.com/north3221/north3221SmartThings/beta/devicetypes/north3221/kodi-client.src/kodi-client.groovy">Copy and paste this code into a new 'Device Handler'</a>  
 Smartapp:          <a href="https://raw.githubusercontent.com/north3221/north3221SmartThings/beta/smartapps/north3221/kodi-manager-cbs.src/kodi-manager-cbs.groovy">Copy and paste this code into a new 'Smartapp'</a>
 
 **OAuth**: You must enable OAuth for the smartapp for any release  
  
 Once you have the smartapp and device handler then go to the smartthings mobile app  
 Marketplace>SmartApps>My Apps>KODI Manager CBs  
 Enter the details for your Kodi (you need the IP, port, username etc discussed above)  
 Client Name = Smartapp and Assign a name = Device Handler  
 Repeat for any Kodi instances you want to create. You will have a smartapp and device handler for each (named as per above step)

###Version 1.0

Initial release with a few bits added on top of the forked version I took.  
I've also added some custom attributes:
```groovy
attribute "currentPlayingType", "string"
attribute "currentPlayingCategory", "enum", ["Movie", "TV Show", "Sports", "None", "Unknown"]
attribute "currentPlayingName", "string"
```

**currentPlayingType**: This is taken directly from Kodi 'type' on now playing metadata. So if its your library you are
playing, its likely it will know its 'Movie' or 'TV show'. However in other cases it will be 'unknown' so I added...

**currentPlayingCategory**: I have added some code to try and work out what is playing. It uses some logic in order to
try and work it out. This is because I want to be able to trigger different lights etc from different playing types.
```
Logic:
    1   -   Check the kodi 'type' if its set then it knows best, so 'movie' = 'Movie' and 'episode' = 'TV Show'
    2   -   Then if kodi type is 'unknown' then I try and work it out in this order (stop at first that matches):
            i   Movie = 'Movie Label' check kodi label for any of the words in Movie label
            ii  Sports = 'Sports Label' check kodi label for any of the words in Sports labels
            iii TV Show = 'TV Show label' check kodi label for any of the words in TV Show labels
            iv  Movie = 'Minimum Movie Runtime' check if the runtime is longer than min movie runtime
            v   TV Show = Runtime is > 0 but less than minimum movie runtime
            vi  Movie = a 'plot' has been added by kodi
```
**NB** The above labels and runtime is exposed in a device manager preference. So you can update the lists to your
  own liking. Please **BE AWARE** that the default values on a preference do not really exist - see the <a href="http://docs.smartthings.com/en/latest/device-type-developers-guide/device-preferences.html#additional-notes">Smartthings Docs</a> so if you
  update **ANY** of them then you have to update them all (you can update to the same thing obviously)

 **currentPlayingName**: Calculated current playing title

With these attributes please also be aware that kodi tells the device handler that its changed (from the call backs
 you configure) then the device handler makes a call to ask kodi what's
 playing, then works out what it is (using above logic). So this may take some time (dependant on networks and traffic I
 guess a few ms to a couple of secs). Therefore, don't trigger on a state change then expect to be able to read
 the attribute, it may not be updated yet, I think I can fix this as part of many improvements I want to make, but
 for now be careful.
 I have got round this by adding the custom attribute as the trigger in my <a href="http://thingsthataresmart.wiki/index.php?title=CoRE">CoRE</a> Poston. **NB** you need to turn on
 'expert mode' in CoRE to do that.

**Custom Command - Shutdown**  
This allows you to call the shutdown command from Smartthings, which will shutdown Kodi



###Version 1.1
I've done quite an overhaul to the both smartapp and device handler. I've updated all the tiles to create a media player and Kodi remote, inside your smarthings mobile app, and unlocked full capability for interaction with Kodi.

Preference changes: I've made them all optional, so you can just update one you want to and not have to update the rest  
Also added new preference:  
**Shutdown as Quit**:   If you change this to true the shutdown command will quit Kodi instead of shutting down system  
**Big Skip**:           If true then skip is 10 mins if left false then skip is 30 secs

Because I made the app into a remote control, I've exposed all the commands it uses:
```groovy
command "shutdown"
command "up"
command "down"
command "left"
command "right"
command "back"
command "info"
command "fastforward"
command "rewind"
command "skipforward"
command "skipbackward"
```
So you can call any of these from Smartthings

The key one that I have added on top of this though is:
```groovy
command "executeAction" , ["string"]
```
This lets you send almost any command to Kodi. Here is a list of what you can send:
[kodi-client-actions](./resources/kodi-client-actions)


Hopefully you find it helpful. Please leave comments on the <a href="https://community.smartthings.com/t/release-kodi-manager-forked-and-updated/75153">Smartthings community page</a>
/**
 * Forked from https://github.com/Toliver182/SmartThings-Kodi who had
 * forked from a plex version: https://github.com/iBeech/SmartThings/tree/master/PlexManager
 *
 *  I added some stuff like 'shutdown' so you can tell kodi to shutdown (the idea being it can turn off your TV)
 *  Also added better tracking of whats playing, I want to control the lights differently so I added some customer attributes
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
//User customisation - update this method with your own preferences if you want.
//I will keep the preferences in order, so you can copy and past over them
//NB you do not need to update this and the prefs are overwritten by device handler prefs if you update there
def getDefaultTheme(){
    def userDefaultThemeMap = [:]
    //v1.2 START
    //ICONS
    userDefaultThemeMap.iconStop = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/stop-icon.png"
    userDefaultThemeMap.iconShutdown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/power-icon.png"
    userDefaultThemeMap.iconUp = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/up-icon.png"
    userDefaultThemeMap.iconDown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/down-icon.png"
    userDefaultThemeMap.iconLeft = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/left-icon.png"
    userDefaultThemeMap.iconRight = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/right-icon.png"
    userDefaultThemeMap.iconBack = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/back-icon.png"
    userDefaultThemeMap.iconInfo = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/info-icon.png"
    userDefaultThemeMap.iconSkipFwd = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/small-fwd-icon.png"
    userDefaultThemeMap.iconSkipRwd = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/small-rwd-icon.png"
    userDefaultThemeMap.iconNext = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/next-icon.png"
    userDefaultThemeMap.iconPrevious = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/prev-icon.png"
    userDefaultThemeMap.iconMenu = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/menu-icon.png"
    userDefaultThemeMap.iconHome = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/home-icon.png"
    userDefaultThemeMap.iconPgUp = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/pg-up-icon.png"
    userDefaultThemeMap.iconPgDown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/default/pg-down-icon.png"
    //DECORATION
    userDefaultThemeMap.decStop = "ring"
    userDefaultThemeMap.decShutdown = "flat"
    userDefaultThemeMap.decUp = "flat"
    userDefaultThemeMap.decDown = "flat"
    userDefaultThemeMap.decLeft = "flat"
    userDefaultThemeMap.decRight = "flat"
    userDefaultThemeMap.decPush = "ring"
    userDefaultThemeMap.decBack = "flat"
    userDefaultThemeMap.decInfo = "ring"
    userDefaultThemeMap.decSkipF = "flat"
    userDefaultThemeMap.decSkipB = "flat"
    userDefaultThemeMap.decNext = "flat"
    userDefaultThemeMap.decPrev = "flat"
    userDefaultThemeMap.decMenu = "flat"
    userDefaultThemeMap.decHome = "flat"
    userDefaultThemeMap.decPup = "flat"
    userDefaultThemeMap.decPdown = "flat"
    //COLOURS
    userDefaultThemeMap.colMainWaiting = "#ffffff"     //White
    userDefaultThemeMap.colMainStartup = "#90d2a7"     //Light Green
    userDefaultThemeMap.colMainPlaying = "#79b821"     //Green
    userDefaultThemeMap.colMainStopped = "#153591"     //Blue
    userDefaultThemeMap.colMainPaused = "#e86d13"      //Orange
    userDefaultThemeMap.colMainShutdown = "#e84e4e"    //Red
    userDefaultThemeMap.colSelectActive = "#22a3ec"    //Blue
    userDefaultThemeMap.colSelectInactive = "#ffffff"  //White
    //v1.2 END
    //v1.3 START
    //v1.3 END
    //Return
    return userDefaultThemeMap
}

def getDefaultPrefs(){
    def prefsMap = [:]
    //Main Icon
    prefsMap.iconMain = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png"
    //CATEGORY SETTINGS
    prefsMap.movieLabels = "cinema, movie, film"
    prefsMap.sportLabels = "sport"
    prefsMap.tvLabels = "bbc, itv, channel, sky, amc, fox"
    prefsMap.minMovieRuntime = 4200
    return prefsMap
}

metadata {
    definition (name: "Kodi-Client", namespace: "north3221", author: "north3221") {
        capability "musicPlayer"        //For playback etc
        capability "mediaController"    //Used for handling the action requests
        capability "Momentary"          //Added for 'push' command I use for 'select' on kodi
        //Custom Commands
        command "describeAttributes"
        command "executeAction" , ["string"]
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
        command "pageUp"
        command "pageDown"
        command "menu"
        command "home"

        //Custom attributes
        attribute "currentPlayingType", "string"
        attribute "currentPlayingCategory", "enum", ["Movie", "TV Show", "Sports", "None", "Unknown"]
        attribute "currentPlayingName", "string"
    }

    tiles(scale: 2) {
        valueTile("main", "device.status", width: 6, height: 2, canChangeIcon: false) {
            state "waiting", label:'Waiting', action:"push" ,icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainWaiting')}", defaultState: true
            state "startup", label:'Startup', action:"push" ,icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainStartup')}", nextState: "waiting"
            state "playing", label:'Playing', action:"pause", icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainPlaying')}", nextState: "waiting"
            state "stopped", label:'Stopped', action:"push", icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainStopped')}", nextState: "waiting"
            state "paused", label:'Paused', action:"play", icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainPaused')}", nextState: "waiting"
            state "shutdown", label:'Shutdown', action:"push", icon:"${getUserPref('iconMain')}", backgroundColor:"${getUserPref('colMainShutdown')}", nextState: "waiting"
        }

        multiAttributeTile(name: "mediaMulti", type:"mediaPlayer", width:6, height:4) {
            tileAttribute("device.status", key: "PRIMARY_CONTROL") {
                attributeState("paused", label:"Paused")
                attributeState("playing", label:"Playing")
                attributeState("stopped", label:"Stopped")
            }
            tileAttribute("device.status", key: "MEDIA_STATUS") {
                attributeState("paused", label:"Paused", action:"play", nextState: "waiting", defaultState: true)
                attributeState("playing", label:"Playing", action:"play", nextState: "waiting")
                attributeState("stopped", label:"Stopped", action:"play", nextState: "waiting")
            }
            tileAttribute("device.status", key: "PREVIOUS_TRACK") {
                attributeState("status", action:"rewind", defaultState: true)
            }
            tileAttribute("device.status", key: "NEXT_TRACK") {
                attributeState("status", action:"fastforward", defaultState: true)
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState("level", action:"music Player.setLevel")
            }
            tileAttribute ("device.mute", key: "MEDIA_MUTED") {
                attributeState("unmuted", action:"music Player.mute", nextState: "muted")
                attributeState("muted", action:"music Player.mute", nextState: "unmuted")
            }
            tileAttribute("device.trackDescription", key: "MARQUEE") {
                attributeState("trackDescription", label:'${currentValue}', defaultState: true)
            }
        }

        standardTile("stop", "device.status", width: 1, height: 1, decoration: "${getUserPref('decStop')}") {
            state "stopped", label:'', action:"music Player.stop", icon:"${getUserPref('iconStop')}", defaultState: true
        }

        standardTile("shutdown", "device.shutdown", width: 1, height: 1, decoration: "${getUserPref('decShutdown')}") {
            state "default", label:'', action:"shutdown", icon:"${getUserPref('iconShutdown')}", defaultState: true
        }

        standardTile("up", "device.up", width: 2, height: 1, decoration: "${getUserPref('decUp')}") {
            state "on", label:'', action:"up", icon:"${getUserPref('iconUp')}", defaultState: true
        }

        standardTile("down", "device.down", width: 2, height: 1, decoration: "${getUserPref('decDown')}") {
            state "on", label:'', action:"down", icon:"${getUserPref('iconDown')}", defaultState: true
        }

        standardTile("left", "device.left", width: 1, height: 2, decoration: "${getUserPref('decLeft')}") {
            state "on", label:'', action:"left", icon:"${getUserPref('iconLeft')}", defaultState: true
        }

        standardTile("right", "device.right", width: 1, height: 2, decoration: "${getUserPref('decRight')}") {
            state "on", label:'', action:"right", icon:"${getUserPref('iconRight')}", defaultState: true
        }

        standardTile("push", "device.status", width: 2, height: 2, decoration: "${getUserPref('decPush')}") {
            state "stopped", label:'Select', action:"push", backgroundColor:"${getUserPref('colSelectActive')}", defaultState: true
            state "playing", label:'Select', action:"push", backgroundColor:"${getUserPref('colSelectInactive')}"
            state "paused", label:'Select', action:"push", backgroundColor:"${getUserPref('colSelectInactive')}"
        }

        standardTile("back", "device.back", width: 1, height: 1, decoration: "${getUserPref('decBack')}") {
            state "back", label:'', action:"back", icon:"${getUserPref('iconBack')}", defaultState: true
        }

        standardTile("info", "device.info", width: 1, height: 1, decoration: "${getUserPref('decInfo')}") {
            state "info", label:'', action:"info", icon:"${getUserPref('iconInfo')}", defaultState: true
        }

        standardTile("skipforward", "device.skipforward", width: 1, height: 1, decoration: "${getUserPref('decSkipF')}") {
            state "skipforward", label:'', action:"skipforward", icon:"${getUserPref('iconSkipFwd')}", defaultState: true
        }

        standardTile("skipbackward", "device.skipbackward", width: 1, height: 1, decoration: "${getUserPref('decSkipB')}") {
            state "skipbackward", label:'', action:"skipbackward", icon:"${getUserPref('iconSkipRwd')}", defaultState: true
        }

        standardTile("next", "device.next", width: 1, height: 1, decoration: "${getUserPref('decNext')}") {
            state "next", label:'', action:"nextTrack", icon:"${getUserPref('iconNext')}", defaultState: true
        }

        standardTile("previous", "device.previous", width: 1, height: 1, decoration: "${getUserPref('decPrev')}") {
            state "previous", label:'', action:"previousTrack", icon:"${getUserPref('iconPrevious')}", defaultState: true
        }

        standardTile("menu", "device.menu", width: 1, height: 1, decoration: "${getUserPref('decMenu')}") {
            state "menu", label:'', action:"menu", icon:"${getUserPref('iconMenu')}", defaultState: true
        }

        standardTile("home", "device.home", width: 1, height: 1, decoration: "${getUserPref('decHome')}") {
            state "home", label:'', action:"home", icon:"${getUserPref('iconHome')}", defaultState: true
        }

        standardTile("pgUp", "device.pgUp", width: 1, height: 1, decoration: "${getUserPref('decPup')}") {
            state "pgUp", label:'', action:"pageUp", icon:"${getUserPref('iconPgUp')}", defaultState: true
        }
        standardTile("pgDown", "device.pgDown", width: 1, height: 1, decoration: "${getUserPref('decPdown')}") {
            state "phDown", label:'', action:"pageDown", icon:"${getUserPref('iconPgDown')}", defaultState: true
        }

        standardTile("1x1", "device.status", width: 1, height: 1, decoration: "flat") {
            state "on", label:'', action:"", icon:"", defaultState: true
        }

        main("main")
        details(["mediaMulti",
                 "previous", "skipbackward", "up", "skipforward", "next",
                 "info", "left", "push", "right", "pgUp",
                 "menu", "pgDown",
                 "shutdown", "stop", "down", "home","back"
        ])
    }

    preferences {
        input "inputMovieLabel", "text", required: false, title: "Movie labels: search kodi label for:", defaultValue: "${getUserPref('movieLabels')}", displayDuringSetup: false
        input "inputSportLabel", "text", required: false, title: "Sport labels: search kodi label for:", defaultValue: "${getUserPref('sportLabels')}", displayDuringSetup: false
        input "inputTVLabel", "text", required: false, title: "TV labels: search kodi label for:", defaultValue: "${getUserPref('tvLabels')}", displayDuringSetup: false
        input "inputMinMovieRuntime", "number", required: false, title: "Min Runtime to class as Movie (secs):", defaultValue: "${getUserPref('minMovieRuntime')}", displayDuringSetup: false
        input "inputShutdownAsQuit", "bool", required: false, title: "Shutdown as Quit:", defaultValue: false, displayDuringSetup: false
        input "inputBigSkip", "bool", required: false, title: "Big Skip: Big (10m) Small (30s)", defaultValue: false, displayDuringSetup: false
        input "inputTheme", "enum", options:["Default", "Glyphs"], description: "Select a theme for the device handler", required: false, title: "Theme", defaultValue: "Default", displayDuringSetup: true
    }
}

def installed() {
    log.debug "Installed"
}

def initialize() {
log.debug "Initialised"
}

def updated() {
    log.debug "updated"
}

// parse events into attributes
def parse(evt) {
    log.debug "Event :" + evt.value

    def msg = parseLanMessage(evt);

    if(msg.header){
        if(msg.header.contains("Unauthorized")){
            log.debug "Cannot authenticate: Please check kodi username and password"
            return
        }
    }

    if (!msg.body){
        return
    }

    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":\"OK\"}"){
        log.debug "received ok"
        return
    }

    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"speed\":0}}"){
        log.debug "received speed 0"
        return
    }
    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"speed\":1}}"){
        log.debug "received speed 1"
        return
    }
    if (msg.body == "{\"error\":{\"code\":-32100,\"message\":\"Failed to execute method.\"},\"id\":1,\"jsonrpc\":\"2.0\"}")
    {
        log.debug "error returned from kodi"
        return
    }

    if (msg.body.startsWith("{\"id\":\"VideoGetItem\"")) {
        parseNowPlaying(msg.body)
    }

}

def parseNowPlaying(msgBody){
    log.info "Parsing Now Playing"
    //Lists to check 'type' against to set category - I think this is the best way to validate type as this means kodi knows the type
    def tvShowType = ["episode"]
    def movieType = ["movie"]

    //start
    def slurper = new groovy.json.JsonSlurper().parseText(msgBody)
    def type = slurper.result.item.type
    def title = slurper.result.item.title

    //initialise category - Unknown so if not set stays as Unknown
    def category = "Unknown"
    def playingTitle = ""

    //If kodi doesnt know then let me try and work it out - else use what kodi says
    if (type == "unknown"){
        def label = slurper.result.item.label
        def runtime = slurper.result.item.runtime
        def plot = slurper.result.item.plot

        log.info "unknown type so checking label (" + label + ") contains Movie " + movieLabels + " or Sport " + sportLabels + " or TV Show " + tvLabels + " if not will check runtime and finally will check plot"
        //Check labels
        if(movieLabels.any {label.toLowerCase().contains(it)}){
            category = "Movie"
            log.info "Matched " + category
        }else if(sportLabels.any {label.toLowerCase().contains(it)}) {
            category = "Sports"
            log.info "Matched " + category
        }else if(tvLabels.any {label.toLowerCase().contains(it)}) {
            category = "TV Show"
            log.info "Matched " + category
        }else if (runtime >= minMovieRuntime){
            category = "Movie"
            log.info "Runtime (" + runtime + ") is greater than (" + minMovieRuntime + ") so category set to (" + category + ")"
        }else if (runtime > 0){
            category = "TV Show"
            log.info "Runtime (" + runtime + ") is greater than (0) but less than movie min runtime (" + minMovieRuntime + ") so category set to (" + category + ")"
        }else if (plot.length() > 0){
            category = "Movie"
            log.info "Plot (" + plot + ") exists so category set to (" + category + ")"
        }
        playingTitle = label
    } else if (movieType.any {type.toLowerCase().contains(it)}){
        category = "Movie"
        log.info "Type is (" + type + ") so category set to (" + category + ")"
        playingTitle = title
    } else if (tvShowType.any {type.toLowerCase().contains(it)}){
        def showTitle = slurper.result.item.showtitle
        category = "TV Show"
        log.info "Type is (" + type + ") so category set to (" + category + ")"
        playingTitle = showTitle + " : " + title
    }

    setPlaybackTitle(type, category, playingTitle)
    log.info "Current Playing type (" + type + ") category (" + category + ") title (" + playingTitle + ")"
}

def executeAction(action) {
    log.debug "Execute Action Request = " + action
    sendEvent(name: "currentActivity", value: device.deviceNetworkId + "." + action);
    //Need to reset the command as hib wont accept duplicates
    sendEvent(name: "currentActivity", value: "RESETACTION");
}

def push() {
    executeAction("select")
}
//Play pause for action button
def play() {
    executeAction("playpause")
}

def stop() {
    executeAction("stop")
}

def shutdown() {
    executeAction(inputShutdownAsQuit ? "quit" : "shutdown")
}

def fastforward(){
    executeAction("fastforward")
}

def rewind(){
    executeAction("rewind")
}

def skipbackward(){
    executeAction(inputBigSkip ? "skip.bigbackward" : "skip.smallbackward")
}

def skipforward(){
    executeAction(inputBigSkip ? "skip.bigforward" : "skip.smallforward")
}

def up(){
    executeAction("up")
}

def down(){
    executeAction("down")
}

def left(){
    executeAction("left")
}

def right(){
    executeAction("right")
}

def back(){
    executeAction("back")
}

def info(){
    executeAction("info")
}

def mute(){
    executeAction("mute")
}

def nextTrack(){
    executeAction("skipnext")
}

def previousTrack(){
    executeAction("skipprevious")
}

def pageUp(){
    executeAction("pageup")
}

def pageDown(){
    executeAction("pagedown")
}

def menu(){
    executeAction("contextmenu")
}

def home(){
    executeAction("home")
}


def setLevel(level) {
    sendEvent(name: "level", value: level);
    executeAction("setVolume." + level);
}

def setPlaybackState(state) {
    log.debug "Setting playback state to: " + state
    switch(state) {
        case "stopped":
            sendEvent(name: "status", value: "stopped");
            setPlaybackTitle("","","")
            break;
        case "playing":
            sendEvent(name: "status", value: "playing");
            break;
        case "paused":
            sendEvent(name: "status", value: "paused");
            break;
        case "shutdown":
            sendEvent(name: "status", value: "shutdown");
            setPlaybackTitle("","", "")
            break;
        case "startup":
            sendEvent(name: "status", value: "startup");
            setPlaybackTitle("","", "")
            break;
    }
}

def setPlaybackTitle(type, category, name) {
    def track = ""

    if(type == ""){
        type = 'None'
    } else {
        track = "Kodi Type: " + type
    }
    if(category == ""){
        category = 'None'
    } else {
        if (track != ""){
            track = track + "\n"
        }
        track = track + "Category: " + category
    }
    if (track != ""){
        track = track + "\n"
    }
    track = track + name
    if(name == ""){
        name = 'Nothing Playing'
        track = name
    }

    log.debug "Setting title to :" + name
    log.debug "Track = " + track
    sendEvent(name: "currentPlayingType", value: type)
    sendEvent(name: "currentPlayingCategory", value: category)
    sendEvent(name: "currentPlayingName", value: name)
    sendEvent(name: "trackDescription", value: track)
}

def setPlaybackIcon(iconUrl) {
    log.debug "Executing 'setPlaybackIcon'"

    state.icon = iconUrl;

    //sendEvent(name: "scanNewClients", icon: iconUrl)
    //sendEvent(name: "scanNewClients", icon: iconUrl)

    log.debug "Icon set to " + state.icon
}

//TOOLS
//define attributes for CoRE
def describeAttributes(payload) {
    payload.attributes = [
            [ name: "currentPlayingType", type: "string"],
            [ name: "currentPlayingCategory", type: "enum", options: ["Movie", "TV Show", "Sports", "None", "Unknown"]],
            [ name: "currentPlayingName", type: "string"]
    ]
    return null
}
//Getters
def getMovieLabels() {
    return (inputMovieLabel ?: getUserPref("movieLabels")).replaceAll("\\s","").toLowerCase().split(',').toList()
}
def getSportLabels() {
    return (inputSportsLabel ?: getUserPref("sportLabels")).replaceAll("\\s","").toLowerCase().split(',').toList()
}
def getTvLabels() {
    return (inputTVLabel ?: getUserPref("tvLabels")).replaceAll("\\s","").toLowerCase().split(',').toList()
}
def getMinMovieRuntime(){
    return inputMinMovieRuntime ?: getUserPref("minMovieRuntime")
}
//User Prefs - Build maps based on settings
def getUserPref(pref){
    def userPrefsMap = [:]
    //Build prefs Map based on settings
    log.debug "inPitTheme = " + inputTheme
    switch (inputTheme){
        case "Glyphs":
            log.debug "Theme = Glumphs"
            userPrefsMap = glyphsTheme
            break
        default:
            log.debug "Theme = Default"
            userPrefsMap = defaultTheme
    }

    //Add settings into prefs
    userPrefsMap = userPrefsMap + defaultPrefs
    //Return requested pref
    return userPrefsMap[pref]
}
//Themes
def getGlyphsTheme(){
    def userGlyphsThemeMap = [:]
    //ICONS
    userGlyphsThemeMap.iconMain = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/main-icon.png"
    userGlyphsThemeMap.iconStop = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/stop-icon.png"
    userGlyphsThemeMap.iconShutdown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/power-icon.png"
    userGlyphsThemeMap.iconUp = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/up-icon.png"
    userGlyphsThemeMap.iconDown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/down-icon.png"
    userGlyphsThemeMap.iconLeft = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/left-icon.png"
    userGlyphsThemeMap.iconRight = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/right-icon.png"
    userGlyphsThemeMap.iconBack = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/back-icon.png"
    userGlyphsThemeMap.iconInfo = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/info-icon.png"
    userGlyphsThemeMap.iconSkipFwd = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/small-fwd-icon.png"
    userGlyphsThemeMap.iconSkipRwd = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/small-rwd-icon.png"
    userGlyphsThemeMap.iconNext = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/next-icon.png"
    userGlyphsThemeMap.iconPrevious = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/prev-icon.png"
    userGlyphsThemeMap.iconMenu = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/menu-icon.png"
    userGlyphsThemeMap.iconHome = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/home-icon.png"
    userGlyphsThemeMap.iconPgUp = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/pg-up-icon.png"
    userGlyphsThemeMap.iconPgDown = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/themes/glyphs/pg-down-icon.png"
    //DECORATION
    userGlyphsThemeMap.decStop = "flat"
    userGlyphsThemeMap.decShutdown = "flat"
    userGlyphsThemeMap.decUp = "flat"
    userGlyphsThemeMap.decDown = "flat"
    userGlyphsThemeMap.decLeft = "flat"
    userGlyphsThemeMap.decRight = "flat"
    userGlyphsThemeMap.decPush = "ring"
    userGlyphsThemeMap.decBack = "flat"
    userGlyphsThemeMap.decInfo = "flat"
    userGlyphsThemeMap.decSkipF = "flat"
    userGlyphsThemeMap.decSkipB = "flat"
    userGlyphsThemeMap.decNext = "flat"
    userGlyphsThemeMap.decPrev = "flat"
    userGlyphsThemeMap.decMenu = "flat"
    userGlyphsThemeMap.decHome = "flat"
    userGlyphsThemeMap.decPup = "flat"
    userGlyphsThemeMap.decPdown = "flat"
    //COLOURS
    userGlyphsThemeMap.colMainWaiting = "#ffffff"     //White
    userGlyphsThemeMap.colMainStartup = "#90d2a7"     //Light Green
    userGlyphsThemeMap.colMainPlaying = "#79b821"     //Green
    userGlyphsThemeMap.colMainStopped = "#153591"     //Blue
    userGlyphsThemeMap.colMainPaused = "#e86d13"      //Orange
    userGlyphsThemeMap.colMainShutdown = "#e84e4e"    //Red
    userGlyphsThemeMap.colSelectActive = "#22a3ec"    //Blue
    userGlyphsThemeMap.colSelectInactive = "#ffffff"  //White
    return userGlyphsThemeMap
}
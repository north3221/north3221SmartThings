/**
 * Forked from https://github.com/Toliver182/SmartThings-Kodi who had
 * forked from a pelx version: https://github.com/iBeech/SmartThings/tree/master/PlexManager
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
def getUserPref(type){
    userPrefsMap = [:]
    //v1.2 START
    userPrefsMap.appListIcon = "https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png"
    userPrefsMap.movieLabels = "cinema, movie, film"
    userPrefsMap.sportLabels = "sport"
    userPrefsMap.tvLabels = "bbc, itv, channel, sky, amc, fox"
    userPrefsMap.minMovieRuntime = 4200
    //v1.2 END
    //v1.3 START
    //v1.3 END
    //Return
    return userPrefsMap[type]
}

//ICONS
def getAppListIcon(){
    return getUserPref("appListIcon")
}


//DEFAULTS
//Used for checking the kodi current playing metadata 'label' if word exists in teh label then 'movie category assigned
def getDefaultMovieLabels() {
    return "movie"
    //return getUserPref("movieLabels")
}
def getDefaultSportLabels() {
    return "sport"
    //return getUserPref("sportLabels")
}
def getDefaultTVLabels() {
    return "bbc, itv, channel, sky, amc, fox"
    //return getUserPref("tvLabels")
}
def getDefaultMinMovieRuntime() {
    return 4200
    //return getUserPref("minMovieRuntime")
}
//Colours
def getTileRed(){
    //return "#ff0000"
    return "#e84e4e"
}
def getTileGreen() {
    return "#79b821"
}
def getTileLightGreen(){
    return "#90d2a7"
}
def getTileOrange(){
    return "#e86d13"
}
def getTileBlue(){
    return "#153591"
}
def getTileWhite(){
    return "#ffffff"
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

        //Custom attributes
        attribute "currentPlayingType", "string"
        attribute "currentPlayingCategory", "enum", ["Movie", "TV Show", "Sports", "None", "Unknown"]
        attribute "currentPlayingName", "string"
    }

    tiles(scale: 2) {
        valueTile("main", "device.status", width: 6, height: 2, canChangeIcon: false) {
            state "waiting", label:'Waiting', action:"push" ,icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileWhite, defaultState: true
            state "startup", label:'Startup', action:"push" ,icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileLightGreen, nextState: "waiting"
            state "playing", label:'Playing', action:"pause", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileGreen, nextState: "waiting"
            state "stopped", label:'Stopped', action:"push", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileBlue, nextState: "waiting"
            state "paused", label:'Paused', action:"play", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileOrange, nextState: "waiting"
            state "shutdown", label:'Shutdown', action:"push", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/main-icon.png", backgroundColor:tileRed, nextState: "waiting"
        }

        multiAttributeTile(name: "mediaMulti", type:"mediaPlayer", width:6, height:4) {
            tileAttribute("device.status", key: "PRIMARY_CONTROL") {
                attributeState("paused", label:"Paused")
                attributeState("playing", label:"Playing")
                attributeState("stopped", label:"Stopped")
            }
            tileAttribute("device.status", key: "MEDIA_STATUS") {
                attributeState("paused", label:"Paused", action:"play", nextState: "playing")
                attributeState("playing", label:"Playing", action:"play", nextState: "paused")
                attributeState("stopped", label:"Stopped", action:"play", nextState: "playing")
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

        standardTile("stop", "device.status", width: 1, height: 1) {
            state "stopped", label:'', action:"music Player.stop", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/stop-red-icon.png", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("shutdown", "device.shutdown", width: 1, height: 1, decoration: "ring") {
            state "default", label:'', action:"shutdown", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/shutdown-icon.jpg", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("up", "device.up", width: 2, height: 1, decoration: "flat") {
            state "on", label:'', action:"up", icon:"st.samsung.da.oven_ic_up", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("down", "device.down", width: 2, height: 1, decoration: "flat") {
            state "on", label:'', action:"down", icon:"st.samsung.da.oven_ic_down", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("left", "device.left", width: 2, height: 2, decoration: "flat") {
            state "on", label:'', action:"left", icon:"st.samsung.da.RAC_4line_01_ic_left", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("right", "device.right", width: 2, height: 2, decoration: "flat") {
            state "on", label:'', action:"right", icon:"st.samsung.da.RAC_4line_03_ic_right", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("push", "device.status", width: 2, height: 2) {
            state "stopped", label:'Select', action:"push", backgroundColor:tileGreen, defaultState: true
            state "playing", label:'Select', action:"push", backgroundColor:tileWhite
            state "paused", label:'Select', action:"push", backgroundColor:tileWhite
        }

        standardTile("back", "device.back", width: 1, height: 1, decoration: "flat") {
            state "back", label:'', action:"back", icon:"http://4.bp.blogspot.com/-OVSmk6zGEOc/Uy50I_FEVqI/AAAAAAAABL0/hfwYhWNViSY/s1600/back+key+assistant+menu+in+Galaxy+S4+Android+Kitkat.png", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("info", "device.info", width: 1, height: 1) {
            state "info", label:'', action:"info", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/cb3da7df6e0fb6c578460c88293895d7868cc343/resources/info-icon.png", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("1x1", "device.status", width: 1, height: 1, decoration: "flat") {
            state "on", label:'', action:"", icon:"", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("skipforward", "device.skipforward", width: 1, height: 1) {
            state "skipforward", label:'', action:"skipforward", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/small-fwd-icon.png", backgroundColor:tileWhite, defaultState: true
        }

        standardTile("skipbackward", "device.skipbackward", width: 1, height: 1) {
            state "skipbackward", label:'', action:"skipbackward", icon:"https://raw.githubusercontent.com/north3221/north3221SmartThings/master/resources/small-rwd-icon.png", backgroundColor:tileWhite, defaultState: true
        }

        main("main")
        details(["mediaMulti",
                 "skipbackward", "stop", "up", "info", "skipforward",
                 "left", "push", "right",
                 "shutdown", "1x1", "down", "1x1","back"
        ])
    }

    preferences {
        input "inputMovieLabel", "text", required: false, title: "Movie labels: search kodi label for:", defaultValue: defaultMovieLabels, displayDuringSetup: false
        input "inputSportLabel", "text", required: false, title: "Sport labels: search kodi label for:", defaultValue: defaultSportLabels, displayDuringSetup: false
        input "inputTVLabel", "text", required: false, title: "TV labels: search kodi label for:", defaultValue: defaultTVLabels, displayDuringSetup: false
        input "inputMinMovieRuntime", "number", required: false, title: "Min Runtime to class as Movie (secs):", defaultValue: defaultMinMovieRuntime, displayDuringSetup: false
        input "inputShutdownAsQuit", "bool", required: false, title: "Shutdown as Quit:", defaultValue: false, displayDuringSetup: false
        input "inputBigSkip", "bool", required: false, title: "Big Skip: Big(10m) Small(30s)", defaultValue: false, displayDuringSetup: false
    }
}

// parse events into attributes
def parse(evt) {
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

def getMovieLabels() {
    def returnList = defaultMovieLabels
    if (inputMovieLabel) {
        returnList = inputMovieLabel
    }
    returnList = returnList.replaceAll("\\s","").toLowerCase().split(',').toList()
    return returnList
}

def getSportLabels() {
    def returnList = defaultSportLabels
    if (inputSportsLabel) {
        returnList = inputSportsLabel
    }
    returnList = returnList.replaceAll("\\s","").toLowerCase().split(',').toList()
    return returnList
}

def getTvLabels() {
    def returnList = defaultTVLabels
    if (inputTVLabel) {
        returnList = inputTVLabel
    }
    returnList = returnList.replaceAll("\\s","").toLowerCase().split(',').toList()
    return returnList
}

def getMinMovieRuntime(){
    if (inputMinMovieRuntime){
        return inputMinMovieRuntime
    }
    return defaultMinMovieRuntime
}

def executeAction(action) {
    log.debug "Execute Action Request = " + action
    sendEvent(name: "currentActivity", value: device.deviceNetworkId + "." + action);
    //Need to reset the command as hib wont accept duplicates
    sendEvent(name: "currentActivity", value: "RESETACTION");
}

def push() {
    log.debug "user pref movie = " + getUserPref("movieLabels")
    //executeAction("select")
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

//define attributes for CoRE
def describeAttributes(payload) {
    payload.attributes = [
            [ name: "currentPlayingType", type: "string"],
            [ name: "currentPlayingCategory", type: "enum", options: ["Movie", "TV Show", "Sports", "None", "Unknown"]],
            [ name: "currentPlayingName", type: "string"]
    ]
    return null
}